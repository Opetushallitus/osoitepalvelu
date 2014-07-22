/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.osoitepalvelu.kooste.config;

import java.net.UnknownHostException;

import com.mongodb.Mongo;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:16 PM
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${osoitepalvelu.mongodb.dbname}")
    protected String dbName;

    @Value("${osoitepalvelu.mongodb.uri}")
    protected String mongoUri;

    @Bean
    @Override
    public MongoTemplate mongoTemplate() throws MongoException, UnknownHostException {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    @Override
    public SimpleMongoDbFactory mongoDbFactory() throws MongoException, UnknownHostException {
        return new SimpleMongoDbFactory(new MongoURI(new MongoClientURI(mongoUri)));
    }

    @Bean
    @Override
    public Mongo mongo() throws UnknownHostException {
        return new Mongo(new MongoURI(new MongoClientURI(mongoUri)));
    }

    @Override
    public String getDatabaseName() {
        return dbName;
    }

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory() throws MongoException, UnknownHostException {
        return new MongoRepositoryFactory(mongoTemplate());
    }
}
