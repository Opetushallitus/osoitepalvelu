package fi.vm.sade.osoitepalvelu.kooste;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import com.mongodb.Mongo;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@PropertySource("classpath:/mongo.properties")
public class MongoConfig extends AbstractMongoConfiguration {

    @Autowired
    private Environment env;

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        return new Mongo(env.getProperty("host"));
    }

    @Override
    public String getDatabaseName() {
        return env.getProperty("databaseName");
    }

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory() throws Exception {
        return new MongoRepositoryFactory(mongoTemplate());
    }
}
