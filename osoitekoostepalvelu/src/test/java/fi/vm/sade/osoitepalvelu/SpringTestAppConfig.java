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

import fi.vm.sade.osoitepalvelu.kooste.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

/**
 * User: ratamaa
 * Date: 12/30/13
 * Time: 9:27 AM
 */
@Configuration
@ComponentScan(basePackages = {
        "fi.vm.sade.osoitepalvelu.kooste.common",
        "fi.vm.sade.osoitepalvelu.kooste.domain",
        "fi.vm.sade.osoitepalvelu.kooste.dao",
        "fi.vm.sade.osoitepalvelu.kooste.service"
})
@ImportResource("classpath:spring/test-application-context.xml")
@Import(value={MongoTestConfig.class})
@PropertySource({"classpath:/osoitekoostepalvelu.properties", "classpath:/test.properties"})
public class SpringTestAppConfig {
    private static final int SECONDS_TO_MS_FACTOR = 1000;
    
    @Autowired
    private Environment env;

    @Bean
    public Config config() {
        Config config = new Config();
        config.setCacheTimeoutMillis(Integer.parseInt(env.getProperty("koodisto.cache.livetime.seconds")) * SECONDS_TO_MS_FACTOR);
        return config;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Messages");
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }
}
