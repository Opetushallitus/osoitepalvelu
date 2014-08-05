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

package fi.vm.sade.osoitepalvelu;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.io.IOException;

/**
 * User: ratamaa
 * Date: 12/30/13
 * Time: 9:33 AM
 */
@Configuration
@ComponentScan(basePackages  =  {
        "fi.vm.sade.osoitepalvelu.kooste.dao"
})
@PropertySource({"classpath:/test.properties" })
public class MongoTestConfig extends AbstractMongoConfiguration {
    private static Logger logger = LoggerFactory.getLogger(MongoTestConfig.class);

    private static final int DEFAULT_MONGO_DB_PORT  =  23456;
    private static final int MAX_DB_START_RETRY_COUNT  =  4;
    private static final long MAX_DB_START_RETRY_INTERVAL_MS  =  1000L;
    private static final long MAX_DB_STOP_RETRY_INTERVAL_MS  =  2000L;

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;

    @Autowired
    protected Environment env;

    public static synchronized void initMongo() throws IOException {
        logger.info("initMongo requested");
        if (mongodExe != null) {
            logger.warn("Skipping initMongo. Already running!");
            return;
        }
        logger.info("Initializing Mongo");
        MongodStarter runtime  =  MongodStarter.getDefaultInstance();
        mongodExe  =  runtime.prepare(new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(DEFAULT_MONGO_DB_PORT, Network.localhostIsIPv6())).build());
        boolean started  =  false;
        int tried  =  0, tries  =  MAX_DB_START_RETRY_COUNT;
        while (!started && ++tried < tries) {
            try {
                mongod  =  mongodExe.start();
                logger.info("Mongo up and running");
                started  =  true;
            } catch (IOException e) {
                logger.warn("Unable to init Mongo: " + e.getMessage());
                if (tried >= tries) {
                    logger.error("Failed to init Mongo after " + tried + " tries: " + e.getMessage(), e);
                    throw e;
                }
                try {
                    Thread.sleep(MAX_DB_START_RETRY_INTERVAL_MS);
                } catch (InterruptedException er) {
                    // Poikkeus ohitetaan
                    continue;
                }
            }
        }
    }

    public static synchronized void stopMongo() {
        logger.info("stopMongo requested.");
        if (mongodExe != null) {
            logger.info("Stopping Mongo");
            mongod.stop();
            mongodExe.stop();
            mongodExe  =  null;
            try {
                Thread.sleep(MAX_DB_STOP_RETRY_INTERVAL_MS);
            } catch (InterruptedException e) {
                // Poikkeus ohitetaan
                return;
            }
        }
    }

    @Bean
    public ApplicationListener<ContextStoppedEvent> contextStopListener() {
        return new ApplicationListener<ContextStoppedEvent>() {
            @Override
            public void onApplicationEvent(ContextStoppedEvent event) {
                stopMongo();
            }
        };
    }

    protected String hostAndPort() {
        String port  =  env.getProperty("mongodb.port");
        return env.getProperty("mongodb.host")  +  (port !=null && port.length() > 0 ? ":"  +  port : "");
    }

    @Bean
    @Override
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate  =  new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    @Bean
    @Override
    public SimpleMongoDbFactory mongoDbFactory() throws Exception {
        initMongo();
        return new SimpleMongoDbFactory(new MongoClient(hostAndPort()), getDatabaseName());
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        initMongo();
        return new Mongo(hostAndPort());
    }

    @Override
    public String getDatabaseName() {
        return env.getProperty("osoitepalvelu.mongodb.dbname");
    }

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory() throws Exception {
        initMongo();
        return new MongoRepositoryFactory(mongoTemplate());
    }
}
