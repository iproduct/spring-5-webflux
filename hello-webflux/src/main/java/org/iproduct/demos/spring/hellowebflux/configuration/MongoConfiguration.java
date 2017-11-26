package org.iproduct.demos.spring.hellowebflux.configuration;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@Configuration()
//@ImportResource("app-context.xml")
@EnableMongoRepositories(basePackageClasses = org.iproduct.demos.spring.hellowebflux.repository.UserRepository.class)
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Autowired
    private List<Converter<?, ?>> converters;

    @Override
    protected String getDatabaseName() {
        return "content";
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient();
    }

    @Override
    public CustomConversions customConversions() {
        return new MongoCustomConversions(converters);
    }

}
