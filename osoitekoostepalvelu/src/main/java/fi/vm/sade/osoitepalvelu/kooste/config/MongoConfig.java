package fi.vm.sade.osoitepalvelu.kooste.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@PropertySource({"classpath:/mongo.properties", "file://${user.home}/oph-configuration/common.properties"})
public class MongoConfig extends AbstractMongoConfiguration {

    @Autowired
    protected Environment env;

    @Bean
    @Override
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    @Bean
    @Override
    public SimpleMongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(), getDatabaseName());
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        String port = env.getProperty("mongodb.port");
        return new Mongo(env.getProperty("mongodb.host") + (port != null && port.length() > 0 ? ":"+port : ""));
    }

    @Override
    public String getDatabaseName() {
        return env.getProperty("mongodb.osoitepalvelu.databaseName");
    }

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory() throws Exception {
        return new MongoRepositoryFactory(mongoTemplate());
    }
}
