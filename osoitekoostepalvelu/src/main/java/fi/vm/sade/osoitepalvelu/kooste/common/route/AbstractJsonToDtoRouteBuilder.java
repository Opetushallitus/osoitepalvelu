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

import fi.vm.sade.osoitepalvelu.kooste.common.ObjectMapperProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.UsernamePasswordCasClientTicketProvider;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.LoadBalanceDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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


    protected <T extends ProcessorDefinition<? extends T>> T casByAuthenticatedUser(T process, final String service) {
        return process.process(new SetOutHeadersProcessor() {
            @Override
            protected Map<String, String> getHeaders() {
                return authenticatedUserCasTicketProvider.provideTicketHeaders(service);
            }
        });
    }

    protected <T extends ProcessorDefinition<? extends T>> T casBydSystemUser(T process, final String service,
                                                                                        final String username,
                                                                                        final String password) {
        return process.process(new SetOutHeadersProcessor() {
            @Override
            protected Map<String, String> getHeaders() {
                return new UsernamePasswordCasClientTicketProvider(casService, username, password)
                        .provideTicketHeaders(service);
            }
        });
    }

    protected abstract static class SetOutHeadersProcessor implements Processor {

        protected abstract Map<String,String> getHeaders();

        @Override
        public void process(Exchange exchange) throws Exception {
            if( exchange.getOut() != null ) {
                Map<String,String> headers = getHeaders();
                for( Map.Entry<String,String> kv : headers.entrySet() ) {
                    exchange.getOut().setHeader(kv.getKey(), kv.getValue());
                }
            } else {
                throw new IllegalStateException("No Out message in Exchange. " +
                        "Perhaps you added process after call to to?");
            }
        }
    }

    protected HeaderBuilder headers() {
        return new HeaderBuilder();
    }

    protected <T extends ProcessorDefinition<? extends T>> T headers( T route, HeaderBuilder headers ) {
        for (Entry<String, Expression> header : headers.getHeaders().entrySet()) {
            route.setHeader(header.getKey(), header.getValue());
        }
        return route;
    }

    protected <T> RouteDefinition fromHttpGetToDtos(String routeId, String url, HeaderBuilder headers,
                                                    TypeReference<T> targetDtoType) {
        return headers(
                from(routeId),
                headers
                    .get()
            )
            .to(url)
            .process(jsonToDto(targetDtoType));
    }

    protected <T> JacksonJsonProcessor jsonToDto(TypeReference<T> targetDtoType) {
        return new JacksonJsonProcessor(mapperProvider, targetDtoType);
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

    protected class HeaderBuilder {
        private Map<String, Expression> headers = new HashMap<String, Expression>();

        public HeaderBuilder() {
        }

        public HeaderBuilder(String headerName, Expression expr) {
            add(headerName, expr);
        }

        public HeaderBuilder add(String headerName, Expression expr) {
            headers.put(headerName, expr);
            return this;
        }

        public HeaderBuilder get() {
            return add(Exchange.HTTP_METHOD, constant("GET"));
        }

        public HeaderBuilder post() {
            return add(Exchange.HTTP_METHOD, constant("POST"));
        }

        public HeaderBuilder path(String path) {
            return add(Exchange.HTTP_PATH, simple(path));
        }

        public HeaderBuilder query(String query) {
            return add(Exchange.HTTP_QUERY, simple(query));
        }

        public Expression getHeaderValue(String headerName) {
            return headers.get(headerName);
        }

        public Map<String, Expression> getHeaders() {
            return headers;
        }
    }

    protected HeaderValueBuilder headerValues() {
        return new HeaderValueBuilder();
    }

    protected static class HeaderValueBuilder {
        private Map<String,Object> map = new HashMap<String, Object>();

        public HeaderValueBuilder add(String header, Object value) {
            this.map.put(header, value);
            return this;
        }

        public Map<String,Object> map() {
            return this.map;
        }
    }
}
