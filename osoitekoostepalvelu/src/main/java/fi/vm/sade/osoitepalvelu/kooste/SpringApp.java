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

import fi.vm.sade.auditlog.ApplicationType;
import fi.vm.sade.auditlog.Audit;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasProxyTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.config.OsoitepalveluCamelConfig;
import fi.vm.sade.osoitepalvelu.kooste.config.SpringMvcApp;
import fi.vm.sade.properties.OphProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.nio.file.Paths;

@Configuration
@ComponentScan(basePackages  =  {
        "fi.vm.sade.osoitepalvelu.kooste.common",
        "fi.vm.sade.osoitepalvelu.kooste.domain",
        "fi.vm.sade.osoitepalvelu.kooste.dao",
        "fi.vm.sade.osoitepalvelu.kooste.service",
        "fi.vm.sade.osoitepalvelu.kooste.webapp"
})
// @ImportResource("classpath:spring/application-context.xml")
@Import(value  =  { OsoitepalveluCamelConfig.class, SpringMvcApp.class})
@PropertySource("classpath:osoitekoostepalvelu.properties")
@SpringBootApplication
public class SpringApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringApp.class);

    private static final int SECONDS_IN_HOUR = 3600;

    @Value("${web.url.cas}")
    private String casService;

    @Value("${auth.mode:'cas'}")
    private String authMode;

    @Bean
    public OphProperties properties() {
        OphProperties properties = new OphProperties("/osoitekoostepalvelu-oph.properties");
        properties.addOptionalFiles("/osoitekoostepalvelu.properties");
        properties.addOptionalFiles(Paths.get(System.getProperties().getProperty("user.home"), "/oph-configuration/common.properties").toString());
        properties.addOptionalFiles("/ui.app.properties");
        properties.addOptionalFiles("/ui.env.properties");
        return properties;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource  =  new ResourceBundleMessageSource();
        messageSource.setBasename("Messages");
        messageSource.setCacheSeconds(SECONDS_IN_HOUR);
        return messageSource;
    }

    @Bean
    public CasTicketProvider proxyTicketProvider() {
        return new CasProxyTicketProvider(casService, authMode);
    }

    @Bean
    @Scope("singleton")
    public Audit audit() {
        return new Audit(LOGGER::info, "osoitepalvelu", ApplicationType.VIRKAILIJA);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

}
