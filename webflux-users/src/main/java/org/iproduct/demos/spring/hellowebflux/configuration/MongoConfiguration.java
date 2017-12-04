package org.iproduct.demos.spring.hellowebflux.configuration;

import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.iproduct.demos.spring.hellowebflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@Configuration
//@EnableMongoRepositories(basePackageClasses = org.iproduct.demos.spring.hellowebflux.repository.UserRepository.class)
@EnableReactiveMongoRepositories(basePackageClasses = UserRepository.class)
public class MongoConfiguration extends AbstractReactiveMongoConfiguration { // extends AbstractMongoConfiguration {

    @Autowired
    private List<Converter<?, ?>> converters;

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        return "content";
    }


    public @Bean
    ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory() {
        return new SimpleReactiveMongoDatabaseFactory(MongoClients.create("mongodb://localhost"), getDatabaseName());
    }

    public @Bean ReactiveMongoTemplate reactiveMongoTemplate() throws Exception {
        ReactiveMongoTemplate template =
                new ReactiveMongoTemplate(reactiveMongoDatabaseFactory(), mappingMongoConverter());
        template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        template.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        return template;
    }

    @Override
    public CustomConversions customConversions() {
        return new MongoCustomConversions(converters);
    }

}
