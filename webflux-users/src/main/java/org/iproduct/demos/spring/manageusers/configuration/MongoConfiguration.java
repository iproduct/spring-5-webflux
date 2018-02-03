package org.iproduct.demos.spring.manageusers.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.WriteConcern;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.iproduct.demos.spring.manageusers.domain.Role;
import org.iproduct.demos.spring.manageusers.domain.User;
import org.iproduct.demos.spring.manageusers.repository.UserRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Mono;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
//@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@EnableReactiveMongoRepositories(basePackageClasses = UserRepository.class)
@Slf4j
public class MongoConfiguration extends AbstractReactiveMongoConfiguration implements ApplicationContextAware{ // extends AbstractMongoConfiguration {
    private static final User defaultAdmin = new User("admin",
            "{bcrypt}$2a$10$y3WZ2gUVsYkgTurMIIDQueSKtEI3nQtUl6Y4VH3vk0izY8gFcfexe",
            "Default", "Admin", Role.ADMIN);

    @Value("${mongo.server.selection.timeout.ms:15000}")
    private long serverSelectionTimeout;

    @Value("${mongo.dbpath:c:\\mongo-data}")
    private String mongoDbPath;

    @Value("${mongo.connection.string:mongodb://localhost}")
    private String mongodbConnectionString;

    @Value("${mongo.db.name:users}")
    private String mongodbDbName;

    @Autowired
    private List<Converter<?, ?>> converters;

    private volatile Process runMongo;
    ReactiveMongoTemplate template;

    private ApplicationContext applicationContext;


    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        log.debug("Mongo connection string: {}", mongodbConnectionString);
        return MongoClients.create(MongoClientSettings.builder()
                .clusterSettings(ClusterSettings.builder()
                        .serverSelectionTimeout(serverSelectionTimeout, TimeUnit.MILLISECONDS)
                        .applyConnectionString(new ConnectionString(mongodbConnectionString)).build())
                .build());

    }

    @Override
    protected String getDatabaseName() {
        return mongodbDbName;
    }


    public @Bean
    ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory() {
        return new SimpleReactiveMongoDatabaseFactory(reactiveMongoClient(), getDatabaseName());
    }

    public @Bean ReactiveMongoTemplate reactiveMongoTemplate() throws Exception {
        template = new ReactiveMongoTemplate(reactiveMongoDatabaseFactory(), mappingMongoConverter());
        template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        template.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        template.collectionExists(User.class)
                    .doOnSuccess(exists -> successfulConnectionCreateDefaultAdminIfNeeded(template, exists))
                    .onErrorResume(error -> {
                        log.warn("Can't connect to MongoDB. Did you forget to start it with 'mongod --dbpath=<path-to-mongo-data-directory>'?");
                        log.debug(error.toString());
                        log.debug("Property mongo.dbpath = " + mongoDbPath);
                        try {
                            runMongo = new ProcessBuilder("mongod", "--dbpath=" + mongoDbPath).start();
                            log.info("Trying to start MongoDB: \"mongod --dbpath=" + mongoDbPath + "\"");
                            return template.collectionExists(User.class)
                                    .flatMap(exists -> successfulConnectionCreateDefaultAdminIfNeeded(template, exists))
                                    .doOnError(err -> {
                                        log.error("FATAL ERROR: Impossible to start MongoDB at dbpath: \"{}\". Please check if dbpath directory exists.\nError was: {}.\nExiting application.",
                                                mongoDbPath, err.getMessage());
                                        System.exit(1);
                                    });
                        } catch (IOException ex) {
                            log.error("Error when executing: mongod --dbpath=\"C:\\mongo-data\". \nException was: " + ex.toString());
                            return Mono.error(ex);
                        }
                    })
                    .block();
        return template;
    }

    @PreDestroy void cleanUp() {
            if(runMongo != null) {
                log.debug("Trying to destroy mongodb process: {}", runMongo);
                runMongo.destroy();
            }
            log.info("Exiting the application.");
    }


    @Override
    public CustomConversions customConversions() {
        return new MongoCustomConversions(converters);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Mono<Boolean> successfulConnectionCreateDefaultAdminIfNeeded(ReactiveMongoTemplate template, Boolean exists) {
        log.info("MongoDB started successfully.");
        if (!exists) return createDefaultAdminUser(template);
        return Mono.just(false);
    }

    /* Pre-populate MongoDB with default admin user */
    private Mono<Boolean> createDefaultAdminUser(ReactiveMongoTemplate template) {
        return template.save(defaultAdmin)
                .log()
                .doOnNext(user -> log.info("No users found - adding default Admin: " + user))
                .hasElement();
    }

}
