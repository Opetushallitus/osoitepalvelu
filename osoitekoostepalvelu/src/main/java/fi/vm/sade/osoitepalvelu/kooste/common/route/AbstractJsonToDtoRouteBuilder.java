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

package fi.vm.sade.osoitepalvelu.kooste.common.route;

import java.util.Map.Entry;

import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.UsernamePasswordCasClientTicketProvider;
import org.apache.camel.*;
import org.apache.camel.model.LoadBalanceDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.support.ExpressionAdapter;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

import fi.vm.sade.osoitepalvelu.kooste.common.ObjectMapperProvider;
import org.springframework.beans.factory.annotation.Value;

/**
 * Abstrakti kantaluokka, joka tarjoaa peruspalvelut Camel-reittien luomiseen,
 * joilla voi lukea HTTP GET pyynnöllä JSON datan halutusta URL:sta ja
 * automaattisesti konvertoida datan tietyksi DTO-luokiksi.
 * 
 * Tämä luokka on toteutettu helpottamaan ja nopeuttamaan JSON Camel-reittien
 * rakentamista osoitepalvelussa.
 */
public abstract class AbstractJsonToDtoRouteBuilder extends SpringRouteBuilder {

    private static final int DEFAULT_RETRY_LIMIT = 10;

    @Value("${web.url.cas}")
    private String casService;

    @Autowired
    protected ObjectMapperProvider mapperProvider;

    @Autowired
    protected CasTicketProvider authenticatedUserCasTicketProvider;


    protected<T extends ProcessorDefinition<T>> ProcessorDefinition<T> casByAuthenticatedUser(T process, final String service) {
        return process.setHeader(CasTicketProvider.CAS_HEADER, new ExpressionAdapter() {
            @Override
            public Object evaluate(Exchange exchange) {
                return authenticatedUserCasTicketProvider.provideTicket(service);
            }
        });
    }

    protected<T extends ProcessorDefinition<T>> ProcessorDefinition<T> casBydSystemUser(T process, final String service,
                                                                                        final String username,
                                                                                        final String password) {
        return process.setHeader(CasTicketProvider.CAS_HEADER, new ExpressionAdapter() {
            @Override
            public Object evaluate(Exchange exchange) {
                return new UsernamePasswordCasClientTicketProvider(casService, username, password)
                        .provideTicket(service);
            }
        });
    }

    protected <T> RouteDefinition fromHttpGetToDtos(String routeId, String url, TypeReference<T> targetDtoType) {
        JacksonJsonProcessor jsonToDtoConverter = new JacksonJsonProcessor(mapperProvider, targetDtoType);
        return from(routeId).setHeader(Exchange.HTTP_METHOD, constant("GET")).to(url).process(jsonToDtoConverter);
    }

    protected <T> RouteDefinition fromHttpGetToDtos(String routeId, String url, String headerName,
            Expression headerValue, TypeReference<T> targetDtoType) {
        JacksonJsonProcessor jsonToDtoConverter = new JacksonJsonProcessor(mapperProvider, targetDtoType);
        return from(routeId).setHeader(Exchange.HTTP_METHOD, constant("GET")).setHeader(headerName, headerValue)
                .to(url).process(jsonToDtoConverter);
    }

    protected <T> RouteDefinition fromHttpGetToDtos(String routeId, String url, HeaderBuilder headers,
            TypeReference<T> targetDtoType) {
        JacksonJsonProcessor jsonToDtoConverter = new JacksonJsonProcessor(mapperProvider, targetDtoType);
        RouteDefinition route = from(routeId).setHeader(Exchange.HTTP_METHOD, constant("GET"));
        // Asetetaan tässä käyttäjän antamat headereiden arvot
        for (Entry<String, Expression> header : headers.getHeaders().entrySet()) {
            route.setHeader(header.getKey(), header.getValue());
        }
        return route.to(url).process(jsonToDtoConverter);
    }

    protected LoadBalanceDefinition addRouteErrorHandlers(RouteDefinition route) {
        boolean roundRobin = true;
        boolean inheritErrorHandler = true;
        return route.loadBalance().failover(DEFAULT_RETRY_LIMIT, inheritErrorHandler, roundRobin);
    }

    protected ProducerTemplate getCamelTemplate() {
        return this.getApplicationContext().getBean(ProducerTemplate.class);
    }

    protected String trim(String value) {
        if (value != null) {
            return value.trim();
        }
        return null;
    }
}
