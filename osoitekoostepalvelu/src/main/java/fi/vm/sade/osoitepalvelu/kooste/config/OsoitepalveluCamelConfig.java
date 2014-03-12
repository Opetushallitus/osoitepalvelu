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

import org.apache.camel.*;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.processor.DelegateAsyncProcessor;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.spi.InterceptStrategy;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.util.StopWatch;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Apache Camel ohjelmistokehyksen alustava luokka. Ajetaan, kun palvelu käynnistyy.
 */
@Configuration
public class OsoitepalveluCamelConfig {

    private ProducerTemplate producerTemplate;

    /**
     * Alustaa Camel-kontekstin ja lisää kaikki tunnetut reitit tähän
     * kontekstiin. Palvelussa täytyy olla ainakin yksi SpringRouteBuilder
     * -luokasta periytetty luokka, jotta palvelin ohjelma suostuu
     * käynnistymään.
     */
    @Bean
    public CamelContext getCamelContext(RoutesBuilder[] builders, ApplicationContext appContext) throws Exception {
        SpringCamelContext context = new SpringCamelContext(appContext);
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
    @Bean(name = "camelTemplate")
    public ProducerTemplate getProducerTemplate(CamelContext context) {
        if (producerTemplate == null) {
            producerTemplate = context.createProducerTemplate();
        }
        return producerTemplate;
    }
}
