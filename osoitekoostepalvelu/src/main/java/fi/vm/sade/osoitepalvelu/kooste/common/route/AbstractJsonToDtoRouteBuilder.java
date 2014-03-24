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
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketCache;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.LazyCasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.UsernamePasswordCasClientTicketProvider;
import org.apache.camel.*;
import org.apache.camel.model.LoadBalanceDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.util.ExchangeHelper;
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
 * joilla voi lukea HTTP pyynnöllä JSON datan halutusta URL:sta ja
 * automaattisesti konvertoida datan tietyksi DTO-luokiksi.
 *
 * Tämä luokka on toteutettu helpottamaan ja nopeuttamaan JSON Camel-reittien
 * rakentamista Osoitepalvelussa.
 *
 * @see HeaderBuilder
 */
public abstract class AbstractJsonToDtoRouteBuilder extends SpringRouteBuilder {
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private static final int DEFAULT_RETRY_LIMIT = 10;
    public static final String CAS_TICKET_CACHE_PROPERTY = "CasTicketCache";

    @Value("${web.url.cas}")
    protected String casService;

    @Autowired
    protected ObjectMapperProvider mapperProvider;

    @Autowired
    protected CasTicketProvider authenticatedUserCasTicketProvider;

    /**
     * @param process the process to authenticate
     * @param service the CAS service
     * @param <T> the process type
     * @return a process where CAS authentication by logged in user is applied as headers to the message
     * @see CasTicketDefinitionProcessor
     */
    protected <T extends ProcessorDefinition<? extends T>> T casByAuthenticatedUser(T process, String service) {
        return casByAuthenticatedUser(service).process(process);
    }

    /**
     * @param service the CAS service
     * @return a CasTicketDefinitionProcessor for given service with authenticated user CAS authentication applied
     * by CasTicketProvider found from the context (CasProxyTicketProvider should be used in actual application mode)
     * @see fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasProxyTicketProvider
     * @see ProcessDefinitionProcessor
     */
    protected CasTicketDefinitionProcessor casByAuthenticatedUser(String service) {
        return new CasTicketDefinitionProcessor(authenticatedUserCasTicketProvider, service);
    }

    /**
     * @param process the process to authenticate
     * @param service the CAS service
     * @param username the system username to authenticate the process with
     * @param password the system password to authenticate
     * @param <T>
     * @return a process where CAS authentication by system user is applied as headers to the message
     * @see UsernamePasswordCasClientTicketProvider
     */
    protected <T extends ProcessorDefinition<? extends T>> T casBydSystemUser(
            T process, String service, String username, String password) {
        return casBydSystemUser(service, username, password).process(process);
    }

    /**
     * @param service the CAS service
     * @param username the system username to authenticate the process with
     * @param password the system password to authenticate
     * @return a CasTicketDefinitionProcessor for given service with authenticated user CAS authentication applied
     * by CasTicketProvider found from the context (CasProxyTicketProvider should be used in actual application mode)
     * @see UsernamePasswordCasClientTicketProvider
     * @see ProcessDefinitionProcessor
     */
    protected CasTicketDefinitionProcessor casBydSystemUser(String service, String username, String password) {
        return new CasTicketDefinitionProcessor(
                new UsernamePasswordCasClientTicketProvider(casService, username, password), service);
    }

    /**
     * @see #casByAuthenticatedUser(String) to construct
     * @see #casBydSystemUser(String, String, String) to construct
     */
    protected class CasTicketDefinitionProcessor implements ProcessDefinitionProcessor {
        private CasTicketProvider ticketProvider;
        private String service;

        public CasTicketDefinitionProcessor(CasTicketProvider ticketProvider, String service) {
            this.ticketProvider = ticketProvider;
            this.service = service;
        }

        @Override
        public <T extends ProcessorDefinition<? extends T>> T process(T process) {
            Debugger debug = debug("CasTicketDefinitionProcessor");
            return process.process(debug)
                    .process(new SetOutHeadersProcessor() {
                protected Map<String, String> getHeaders(Exchange exchange) {
                    CasTicketCache cache = exchange.getProperty(CAS_TICKET_CACHE_PROPERTY, CasTicketCache.class);
                    return new LazyCasTicketProvider(cache, ticketProvider).provideTicketHeaders(service);
                }
            }).process(debug);
        }
    }

    /**
     * @return a ProcessDefinitionProcessor that marshals the Request body to JSON using Jackson implementation
     */
    protected ProcessDefinitionProcessor bodyAsJson() {
        return new ProcessDefinitionProcessor() {
            @Override
            public <T extends ProcessorDefinition<? extends T>> T process(T process) {
                return process.marshal().json(JsonLibrary.Jackson)
                        .process(inToOut());
            }
        };
    }

    /**
     * A Camel's Processor that sets a number of headers to the Exchange and
     * internally calls #inToOut
     * @see HeaderBuilder
     * @see #inToOut()
     */
    protected abstract class SetOutHeadersProcessor implements Processor {

        /**
         * @return the headers to set
         */
        protected abstract Map<String,String> getHeaders(Exchange exchange);

        @Override
        public final void process(Exchange exchange) throws Exception {
            if( exchange.getIn() != null ) {
                Map<String,String> headers = getHeaders(exchange);
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

    /**
     * @param route to apply a HeaderBuilder to
     * @param processor to apply to the route
     * @param <T>
     * @return the route with given header processor applied
     * @see HeaderBuilder
     */
    protected <T extends ProcessorDefinition<? extends T>> T headers( T route, HeaderBuilder processor ) {
        return apply(route, processor);
    }

    /**
     * @param route to apply a ProcessDefinitionProcessor to
     * @param processor to apply to the route
     * @param <T>
     * @return the route with given processor applied
     * @see HeaderBuilder
     */
    protected <T extends ProcessorDefinition<? extends T>> T apply( T route, ProcessDefinitionProcessor processor ) {
        return processor.process(route);
    }

    /**
     * @param routeId the route id for from(URI)
     * @param url the target URL for the RouteDefinition#to(URI) call
     * @param headers to apply
     * @param targetDtoType the target DTO type to convert to (use anonymous style)
     * @param <T>
     * @return the Camel route with BET method to the given URL processed to given DTO type from JSON with
     * Debugging enabled with routeId.ServiceCall name
     * @see #from(String)
     * @see RouteDefinition#to(String)
     * @see HeaderBuilder
     * @see #debug(String)
     */
    protected <T> RouteDefinition fromHttpGetToDtos(String routeId, String url, HeaderBuilder headers,
                                                    TypeReference<T> targetDtoType) {
        Debugger debug = debug(routeId+".ServiceCall");
        return headers(
                from(routeId),
                headers
                        .get()
        )
            .process(debug)
            .to(url)
            .process(debug)
            .process(jsonToDto(targetDtoType));
    }

    /**
     * @param targetDtoType the type to convert JSON to
     * @param <T> the type
     * @return a JacksonJsonProcessor for JSON -> DTO conversion produced by a ObjectMapperProvider bound into
     * Spring's context
     * @see ObjectMapperProvider
     */
    protected <T> JacksonJsonProcessor jsonToDto(TypeReference<T> targetDtoType) {
        return new JacksonJsonProcessor(mapperProvider, targetDtoType);
    }

    /**
     * @param route to load balance
     * @return route with DEFAULT_RETRY_LIMIT applied as failover with roundRobin and inheritedErrorHandler
     */
    protected LoadBalanceDefinition addRouteErrorHandlers(RouteDefinition route) {
        boolean roundRobin = true;
        boolean inheritErrorHandler = true;
        return route.loadBalance().failover(DEFAULT_RETRY_LIMIT, inheritErrorHandler, roundRobin);
    }

    /**
     * @return a ProducerTemplate from Spring's ApplicationContext
     */
    protected ProducerTemplate getCamelTemplate() {
        return this.getApplicationContext().getBean(ProducerTemplate.class);
    }

    /**
     * A null-safe String.trim
     * @param value to be trimmed
     * @return a trimmed value
     */
    protected String trim(String value) {
        if (value != null) {
            return value.trim();
        }
        return null;
    }

    /**
     * Tracks the time elapsed between the two processes phases in Camel route the (same) instance of this Debugger
     * is associated to. Also calls #inToOut at the end. Override doProcess.
     *
     * @see #doProcess(org.apache.camel.Exchange) to do the actual logging
     */
    protected abstract class Debugger implements Processor {
        private Long beginMoment=null;
        private String name;

        protected Debugger(String name) {
            this.name = name;
        }

        protected Debugger() {
        }

        public String getName() {
            if (this.name != null) {
                return this.name;
            }
            return "Camel";
        }

        @Override
        public final void process(Exchange exchange) throws Exception {
            if (beginMoment == null) {
                beginMoment = System.currentTimeMillis();
            } else {
                long now = System.currentTimeMillis();
                long duration = now-beginMoment;
                log.info(getName()+" execution took: "+duration+"ms since last call.");
                beginMoment = null;
            }
            inToOut(exchange);
            doProcess(exchange);
        }

        /**
         * @param exchange to wrapped call to process as in a normal Camel Processor
         * @throws Exception
         * @see Processor#process(org.apache.camel.Exchange)
         */
        public abstract void doProcess(Exchange exchange) throws Exception;
    }

    /**
     * Additional Processors introduced into Camel route definition seem to null the set the out part of the Exchange
     * to in and set the out part as null. After execution of a custom Processor, we therefore want to reverse this.
     *
     * @param exchange for which to set Excahge.out = Exchange.in
     */
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

    /**
     * @param name for the debugger to identify it in logs
     * @return a new logger that logs the Body and Headers for In and Out part of the Exchange every time called and
     * act as a time tracking Debugger
     * @see Debugger
     */
    protected Debugger debug(String name) {
        return new Debugger(name) {
            @Override
            public void doProcess(Exchange exchange) throws Exception {
                logIn(exchange, getName());
                //logOut(exchange);
            }
        };
    }

    /**
     * @param exchange the In part of which to be logged
     * @param name the prefix used in logging
     */
    protected void logIn(Exchange exchange, String name) {
        if (exchange.getIn() != null) {
            log.info(name + " Exchange in: " + debug(exchange.getIn()) );
        } else {
            log.warn(name + " Exchange in is null!");
        }
    }

    /**
     * @param exchange the Out part of which to be logged
     * @param name the prefix used in logging
     */
    protected void logOut(Exchange exchange, String name) {
        if (exchange.getOut() != null) {
            log.info(name + " Exchange out: " + debug(exchange.getOut()) );
        } else {
            log.warn(name + " Exchange out is null!");
        }
    }

    /**
     * @return a Processor which converts input to output
     * @see #inToOut(org.apache.camel.Exchange)
     */
    protected Processor inToOut() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                inToOut(exchange);
            }
        };
    }

    /**
     * @param message the message
     * @return
     */
    protected String debug(Message message) {
        try {
            return stringify(message.getBody())
                    + " with headers: " + mapperProvider.getContext(ObjectMapper.class).writeValueAsString(message.getHeaders());
        } catch (IOException e) {
            return stringify(message.getBody()) + " (Headers could not be converted to JSON cause: " +e.getMessage()+")";
        }
    }

    /**
     * @param object to stringify, null-safe
     * @return object as string, converting byte arrays to String, using  .toString() for other objects and "null"
     * for nulls
     */
    protected String stringify(Object object) {
        if( object == null ) return "null";
        if( object.getClass().isArray() && Byte.TYPE.equals(object.getClass().getComponentType()) ) {
            return new String((byte[]) object);
        }
        return object.toString();
    }

    /**
     * @return a new HeaderBuilder
     * @see HeaderBuilder
     */
    protected HeaderBuilder headers() {
        return new HeaderBuilder();
    }

    /**
     * Can be used to avoid some repeating .setHeader-calls when building a Camel route.
     *
     * @see #headers() to construct
     * @see #headers(org.apache.camel.model.ProcessorDefinition,
     *  fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder.HeaderBuilder) to apply
     */
    protected class HeaderBuilder implements ProcessDefinitionProcessor {
        private Map<String, Expression> headers = new HashMap<String, Expression>();
        private List<ProcessDefinitionProcessor> additionalProcessors = new ArrayList<ProcessDefinitionProcessor>();

        public HeaderBuilder() {
        }

        /**
         * @param headerName
         * @param expr the value for the header
         * @return this HeaderBuilder with given header set to given Expression value
         * @see Exchange for header constants
         * @see #simple(String) for simple ${} containing expressions that may change for each call
         * @see #constant(Object) for constant header values
         */
        public HeaderBuilder add(String headerName, Expression expr) {
            headers.put(headerName, expr);
            return this;
        }

        /**
         * @param additionalProcessor to add after the other definitions
         * @return this HeaderBuilder with additionalProcessor applied
         */
        public HeaderBuilder add(ProcessDefinitionProcessor additionalProcessor) {
            this.additionalProcessors.add(additionalProcessor);
            return this;
        }

        /**
         * @return this HeaderBuilder with GET HTTP_METHOD
         */
        public HeaderBuilder get() {
            return add(Exchange.HTTP_METHOD, constant("GET"));
        }

        /**
         * @return this HeaderBuilder with POST HTTP_METHOD
         */
        public HeaderBuilder post() {
            return add(Exchange.HTTP_METHOD, constant("POST"));
        }

        /**
         * @param path to set as a simple HTTP_PATH header value
         * @return this HeaderBuilder with given HTTP_PATH
         */
        public HeaderBuilder path(String path) {
            return add(Exchange.HTTP_PATH, simple(path));
        }

        /**
         * @param query to set as HTTP_QUERY simple header value.
         * @return this HeaderBuilder
         */
        public HeaderBuilder query(String query) {
            return add(Exchange.HTTP_QUERY, simple(query));
        }

        /**
         * @param type the CONTENT_TYPE constant header value
         * @return this HeaderBuilder
         */
        public HeaderBuilder contentType(String type) {
            return add(Exchange.CONTENT_TYPE, constant(type));
        }

        /**
         * Sets the CONTENT_TYPE type to application/json;charset=UTF-8
         * and adds an ProcessDefinitionProcessor which marshalls the
         * Message Body in the request as JSON.
         *
         * @return this HeaderBuilder
         * @see #bodyAsJson()
         */
        public HeaderBuilder jsonRequstBody() {
            return add(bodyAsJson())
                    .contentType(CONTENT_TYPE_JSON);
        }

        /**
         * @param service the CAS service
         * @return this HeaderBuilder with CAS authentication by authenticated user applied
         * @see #casByAuthenticatedUser(String)
         */
        public HeaderBuilder casAuthenticationByAuthenticatedUser(String service) {
            return add(casByAuthenticatedUser(service));
        }

        /**
         * @param service the CAS service
         * @param username the username for a CAS system user
         * @param password the password for a CAS system user
         * @return this HeaderBuilder with CAS authentication by given system user applied
         * @see #casBydSystemUser(String, String, String)
         */
        public HeaderBuilder casAuthenticationBySystemUser(String service, String username, String password) {
            return add(casBydSystemUser(service, username, password));
        }

        @Override
        public <T extends ProcessorDefinition<? extends T>> T process(T process) {
            for (Entry<String, Expression> header : headers.entrySet()) {
                process = process.setHeader(header.getKey(), header.getValue());
            }
            for (ProcessDefinitionProcessor defProcessor : additionalProcessors) {
                process = defProcessor.process(process);
            }
            return process;
        }
    }

    /**
     * @return a HeaderValueBuilder that may be used with ProducerTemplate.requestBodyAndHeaders
     * @see ProducerTemplate#requestBodyAndHeaders(org.apache.camel.Endpoint, Object, java.util.Map)
     * @see ProducerTemplate#requestBodyAndHeaders(org.apache.camel.Endpoint, Object, java.util.Map, Class)
     */
    protected HeaderValueBuilder headerValues() {
        return new HeaderValueBuilder();
    }

    /**
     * May be used with ProducerTemplate.requestBodyAndHeaders
     *
     * @see ProducerTemplate#requestBodyAndHeaders(org.apache.camel.Endpoint, Object, java.util.Map)
     * @see ProducerTemplate#requestBodyAndHeaders(org.apache.camel.Endpoint, Object, java.util.Map, Class)
     * @see fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder.HeaderValueBuilder#map()
     */
    protected static class HeaderValueBuilder {
        private Map<String,Object> map = new HashMap<String, Object>();

        /**
         * @param header
         * @param value
         * @return this builder with header set to value
         */
        public HeaderValueBuilder add(String header, Object value) {
            this.map.put(header, value);
            return this;
        }

        /**
         * @return a Map value for the builder
         */
        public Map<String,Object> map() {
            return this.map;
        }
    }

    /**
     * @param template the ProducerTemplate
     * @param endpointUri the endpoint URI
     * @param body the Exchange In body
     * @param headers the Exchange In headers
     * @param requestContext the context within the application in which this Camel request is performed
     * @param type to convert the output to
     * @param <T> the type
     * @return the results
     * @throws CamelExecutionException
     */
    public<T> T sendBodyHeadersAndProperties(ProducerTemplate template, String endpointUri,
                                             final Object body, final Map<String, Object> headers,
                                             final CamelRequestContext requestContext,
                                             Class<T> type) throws CamelExecutionException {
        Map<String,Object> properties = new HashMap<String, Object>();
        CasTicketCache cache = requestContext.getTicketCache();
        if (cache != null) {
            properties.put(CAS_TICKET_CACHE_PROPERTY, cache);
        }
        return sendBodyHeadersAndProperties(template, endpointUri, body, headers, properties, type);
    }

    /**
     * Extension to ProducerTemplate's requestBodyAndHeaders with properties based on the implementation of
     * org.apache.camel.impl.DefaultProducerTemplate (no such method exists in the ProducerTemplate)
     *
     * @param template the ProducerTemplate
     * @param endpointUri the endpoint URI
     * @param body the Exchange In body
     * @param headers the Exchange In headers
     * @param properties the properties to be set to the exchange
     * @param type to convert the output to
     * @param <T> the type
     * @return the results
     * @throws CamelExecutionException
     */
    public<T> T sendBodyHeadersAndProperties(ProducerTemplate template, String endpointUri,
                 final Object body, final Map<String, Object> headers,
                 final Map<String, Object> properties,
                 Class<T> type) throws CamelExecutionException {
        ExchangePattern pattern = ExchangePattern.InOut;
        Endpoint endpoint = template.getCamelContext().getEndpoint(endpointUri);
        if (endpoint == null) {
            throw new NoSuchEndpointException(endpointUri);
        }
        Exchange exchange = template.send(endpoint, pattern, new Processor() {
            public void process(Exchange exchange) throws Exception {
                for (Map.Entry<String, Object> property : properties.entrySet()) {
                    exchange.setProperty(property.getKey(), property.getValue());
                }
                Message in = exchange.getIn();
                for (Map.Entry<String, Object> header : headers.entrySet()) {
                    in.setHeader(header.getKey(), header.getValue());
                }
                in.setBody(body);
            }
        });
        Object result = ExchangeHelper.extractResultBody(exchange, pattern);
        if (pattern.isOutCapable()) {
            return template.getCamelContext().getTypeConverter().convertTo(type, result);
        } else {
            // return null if not OUT capable
            return null;
        }
    }
}
