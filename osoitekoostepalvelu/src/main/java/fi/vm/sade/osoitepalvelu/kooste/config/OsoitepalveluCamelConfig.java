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

import fi.vm.sade.osoitepalvelu.kooste.common.util.IgnorantTrustManager;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


/**
 * Apache Camel ohjelmistokehyksen alustava luokka. Ajetaan, kun palvelu käynnistyy.
 */
@Configuration
@ComponentScan(basePackages  =  {
        "fi.vm.sade.osoitepalvelu.kooste.route"
})
public class OsoitepalveluCamelConfig {
    @Value("${ignore.self.signed.certificates:false}")
    private boolean ignoreSelfSignedCertificates=false;
    private ProducerTemplate producerTemplate;

    /**
     * Alustaa Camel-kontekstin ja lisää kaikki tunnetut reitit tähän
     * kontekstiin. Palvelussa täytyy olla ainakin yksi SpringRouteBuilder
     * -luokasta periytetty luokka, jotta palvelin ohjelma suostuu
     * käynnistymään.
     */
    @Bean
    public CamelContext getCamelContext(RoutesBuilder[] builders, ApplicationContext appContext) throws Exception {
        SpringCamelContext context  =  new SpringCamelContext(appContext);
        for (RoutesBuilder route : builders) {
            context.addRoutes(route);
        }
        context.setAutoStartup(true);
        return context;
    }

    /**
     * Camel ProducerTemplate luonti metodi. Luodaan yksi template yleisesti
     * osoitepalvelun käyttöön.
     * 
     * @param context
     *            Camel konteksti.
     * @return Viite Camel ProducerTemplate -luokkaan, jonka kautta voi välittää
     *         dataa Camel-reitteihin.
     */
    @Bean(name  =  "camelTemplate")
    public ProducerTemplate getProducerTemplate(CamelContext context) {
        if (producerTemplate == null) {
            producerTemplate = context.createProducerTemplate();
        }
        return producerTemplate;
    }

    @PostConstruct
    public void setupTrustManager() {
        if (ignoreSelfSignedCertificates) {
            // Accept any Certificate (for self-signed dev instances):
            IgnorantTrustManager.setup();
        }
    }
}
