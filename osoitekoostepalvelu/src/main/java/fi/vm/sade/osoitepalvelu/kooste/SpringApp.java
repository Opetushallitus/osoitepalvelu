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

package fi.vm.sade.osoitepalvelu.kooste;

import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasProxyTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.config.MongoConfig;
import fi.vm.sade.osoitepalvelu.kooste.config.OsoitepalveluCamelConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:09 PM
 */
@Configuration
@ComponentScan(basePackageClasses = SpringApp.class)
@ImportResource("classpath:spring/application-context.xml")
@Import(value = {MongoConfig.class, OsoitepalveluCamelConfig.class})
public class SpringApp {
    private static final int SECONDS_TO_MS_FACTOR = 1000;

    @Value("${koodisto.cache.livetime.seconds}")
    private int koodistoCacheLiveTimeSeconds;

    @Value("${web.url.cas:'prod'}")
    private String casService;

    @Value("${auth.mode}")
    private String authMode;

    public static class Config {
        private int cacheTimeoutMillis;

        public int getCacheTimeoutMillis() {
            return cacheTimeoutMillis;
        }

        public void setCacheTimeoutMillis(int cacheTimeoutMillis) {
            this.cacheTimeoutMillis = cacheTimeoutMillis;
        }
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Messages");
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }

    @Bean
    public Config config() {
        Config config = new Config();
        config.setCacheTimeoutMillis(koodistoCacheLiveTimeSeconds * SECONDS_TO_MS_FACTOR);
        return config;
    }

    @Bean
    public CasTicketProvider proxyTicketProvider() {
        return new CasProxyTicketProvider(casService, authMode);
    }
}
