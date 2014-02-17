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

package fi.vm.sade.osoitepalvelu.kooste.service.koodisto.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import fi.vm.sade.osoitepalvelu.kooste.common.ObjectMapperProvider;

/**
 * Camel-viestiketjun prosessori, jonka avulla voi helposti tehdä JSON konversion 
 * tavalliseksi (Pojo) Java -luokkasi. JSON -> Pojo mappauksen käytetään
 * Jackson kirjastoa, joka on jo valmiiksi konfiguroitu palveluun. 
 */
public class JacksonJsonProcessor implements Processor {

    private ObjectMapperProvider mapperProvider;

    @SuppressWarnings("rawtypes")
    private TypeReference targetClassType;

    public <T> JacksonJsonProcessor(ObjectMapperProvider mapperProvider, TypeReference<T> targetClassType) {
        this.targetClassType = targetClassType;
        this.mapperProvider = mapperProvider;
    }

    @SuppressWarnings("rawtypes")
    public TypeReference getTargetClassType() {
        return targetClassType;
    }

    /**
     * Metodi, joka lukee Camel -viestin merkkijonona ja konvertoi JSON datan
     * TypeReference tyyppiseksi olioksi.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        ObjectMapper objectMapper = mapperProvider.getContext(targetClassType.getClass());
        String jsonData = exchange.getIn().getBody(String.class);
        exchange.getOut().setBody(objectMapper.readValue(jsonData, targetClassType));
    }
}
