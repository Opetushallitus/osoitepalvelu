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

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.sade.osoitepalvelu.kooste.common.ObjectMapperProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.UsernamePasswordCasClientTicketProvider;
import org.apache.camel.*;
import org.apache.camel.model.LoadBalanceDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private static final int DEFAULT_RETRY_LIMIT = 10;

    @Value("${web.url.cas}")
    private String casService;

    @Autowired
    protected ObjectMapperProvider mapperProvider;

    @Autowired
    protected CasTicketProvider authenticatedUserCasTicketProvider;


    protected <T extends ProcessorDefinition<? extends T>> T casByAuthenticatedUser(T process, String service) {
        return casByAuthenticatedUser(service).process(process);
    }

    protected CasTicketDefinitionProcessor casByAuthenticatedUser(String service) {
        return new CasTicketDefinitionProcessor(authenticatedUserCasTicketProvider, service);
    }

    protected <T extends ProcessorDefinition<? extends T>> T casBydSystemUser(
            T process, String service, String username, String password) {
        return casBydSystemUser(service, username, password).process(process);
    }

    protected CasTicketDefinitionProcessor casBydSystemUser(String service, String username, String password) {
        return new CasTicketDefinitionProcessor(
                new UsernamePasswordCasClientTicketProvider(casService, username, password), service);
    }

    protected class CasTicketDefinitionProcessor implements ProcessDefinitionProcessor {
        private CasTicketProvider ticketProvider;
        private String service;

        public CasTicketDefinitionProcessor(CasTicketProvider ticketProvider, String service) {
            this.ticketProvider = ticketProvider;
            this.service = service;
        }

        @Override
        public <T extends ProcessorDefinition<? extends T>> T process(T process) {
            return process.process(new SetOutHeadersProcessor() {
                protected Map<String, String> getHeaders() {
                    return ticketProvider.provideTicketHeaders(service);
                }
            });
        }
    }

    protected ProcessDefinitionProcessor bodyAsJson() {
        return new ProcessDefinitionProcessor() {
            @Override
            public <T extends ProcessorDefinition<? extends T>> T process(T process) {
                return process.marshal().json(JsonLibrary.Jackson)
                        .process(inToOut());
            }
        };
    }

    protected abstract class SetOutHeadersProcessor implements Processor {

        protected abstract Map<String,String> getHeaders();

        @Override
        public void process(Exchange exchange) throws Exception {
            if( exchange.getIn() != null ) {
                Map<String,String> headers = getHeaders();
                for( Map.Entry<String,String> kv : headers.entrySet() ) {
                    exchange.getIn().setHeader(kv.getKey(), kv.getValue());
                }
            } else {
                throw new IllegalStateException("No Out message in Exchange. " +
                        "Perhaps you added process after call to to?");
            }
            inToOut(exchange);
        }
    }

    protected HeaderBuilder headers() {
        return new HeaderBuilder();
    }

    protected <T extends ProcessorDefinition<? extends T>> T headers( T route, HeaderBuilder headers ) {
        for (Entry<String, Expression> header : headers.getHeaders().entrySet()) {
            route.setHeader(header.getKey(), header.getValue());
        }
        for (ProcessDefinitionProcessor defProcessor : headers.getAdditionalProcessors()) {
            route = defProcessor.process(route);
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

    protected Processor debug() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                logIn(exchange);
                logOut(exchange);
                inToOut().process(exchange);
            }
        };
    }

    protected Processor debugIn() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                logIn(exchange);
                inToOut().process(exchange);
            }
        };
    }

    protected Processor debugOut() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                logOut(exchange);
                inToOut().process(exchange);
            }
        };
    }

    protected void logIn(Exchange exchange) {
        if (exchange.getIn() != null) {
            log.info("EXCHANGE IN: " + debug(exchange.getIn()) );
        } else {
            log.warn("EXCHANGE IN Body is null!");
        }
    }

    protected void logOut(Exchange exchange) {
        if (exchange.getOut() != null) {
            log.info("EXCHANGE OUT: " + debug(exchange.getOut()) );
        } else {
            log.warn("EXCHANGE OUT Body is null!");
        }
    }

    protected void inToOut(Exchange exchange) {
        if (exchange.getIn() != null) {
            if (exchange.getOut() == null) {
                // Logically enough, this copies the message as empty
                exchange.setOut(exchange.getIn());
            }
            // Have to set body and headers separately:
            exchange.getOut().setBody( exchange.getIn().getBody() );
            exchange.getOut().setHeaders( exchange.getIn().getHeaders() );
        }
    }

    protected Processor inToOut() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                inToOut(exchange);
            }
        };
    }

    protected String debug(Message m) {
        try {
            return stringify(m.getBody())
                    + " WITH HEADERS " + mapperProvider.getContext(ObjectMapper.class).writeValueAsString(m.getHeaders());
        } catch (IOException e) {
            return stringify(m.getBody()) + " (Headers could not be converted to JSON cause: " +e.getMessage()+")";
        }
    }

    protected String stringify(Object o) {
        if( o == null ) return "null";
        if( o.getClass().isArray() && Byte.TYPE.equals(o.getClass().getComponentType()) ) {
            return new String((byte[]) o);
        }
        return o.toString();
    }

    protected class HeaderBuilder {
        private Map<String, Expression> headers = new HashMap<String, Expression>();
        private List<ProcessDefinitionProcessor> additionalProcessors = new ArrayList<ProcessDefinitionProcessor>();

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

        public HeaderBuilder contentType(String type) {
            return add(Exchange.CONTENT_TYPE, constant(type));
        }

        public HeaderBuilder casAuthenticationByAuthenticatedUser(String service) {
            return add(casByAuthenticatedUser(service));
        }

        public HeaderBuilder casAuthenticationBySystemUser(String service, String username, String password) {
            return add(casBydSystemUser(service, username, password));
        }

        public HeaderBuilder jsonRequstBody() {
            return add(bodyAsJson())
                    .contentType(CONTENT_TYPE_JSON);
        }

        public HeaderBuilder add(ProcessDefinitionProcessor additionalProcessor) {
            this.additionalProcessors.add(additionalProcessor);
            return this;
        }

        public Expression getHeaderValue(String headerName) {
            return headers.get(headerName);
        }

        public Map<String, Expression> getHeaders() {
            return headers;
        }

        public List<ProcessDefinitionProcessor> getAdditionalProcessors() {
            return additionalProcessors;
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
