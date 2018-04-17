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
import fi.vm.sade.osoitepalvelu.kooste.common.util.StringHelper;

import org.apache.camel.*;
import org.apache.camel.model.ExpressionNode;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.util.ExchangeHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.camel.http.common.HttpOperationFailedException;

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
    protected static final String CONTENT_TYPE_JSON  =  "application/json;charset=UTF-8";
    protected static final int DEFAULT_RETRY_LIMIT  =  2;
    protected static final String CAS_TICKET_CACHE_PROPERTY  =  "CasTicketCache";
    protected static final String CAS_TICKET_CACHE_SERVICE_PROPERTY  =  "CasTicketCache.service";
    protected static final String URL_ENCODING = "UTF-8";
    // According to http://camel.apache.org/http4.html
    protected static final String HTTP_CLIENT_TIMEOUT_PARAM_NAME = "httpClient.socketTimeout";
    protected static final long MILLIS_IN_SECOND = 1000L;
    protected static final long SECONDS_IN_MINUTE = 60L;
    protected static final long DEFAULT_HTTP_TIMEOUT_MILLIS = 15L*MILLIS_IN_SECOND;


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
            this.ticketProvider  =  ticketProvider;
            this.service  =  service;
        }

        @Override
        public <T extends ProcessorDefinition<? extends T>> T process(T process) {
            Debugger debug  =  debug("CasTicketDefinitionProcessor");
            return process.process(debug)
                    .process(new SetOutHeadersProcessor() {
                protected Map<String, Object> getHeaders(Exchange exchange) {
                    CasTicketCache cache  =  exchange.getProperty(CAS_TICKET_CACHE_PROPERTY, CasTicketCache.class);
                    exchange.setProperty(CAS_TICKET_CACHE_SERVICE_PROPERTY, service);
                    return new LazyCasTicketProvider(cache, ticketProvider).provideTicketHeaders(service);
                }

                @Override
                protected String getDescription() {
                    return "CasTicketDefinitionProcessor";
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
        protected abstract Map<String, Object> getHeaders(Exchange exchange);

        @Override
        public final void process(Exchange exchange) {
            if(exchange.getIn() != null) {
                Map<String, Object> headers  =  getHeaders(exchange);
                for(Map.Entry<String, Object> kv : headers.entrySet()) {
                    exchange.getIn().setHeader(kv.getKey(), kv.getValue());
                }
            } else {
                throw new IllegalStateException("No Out message in Exchange. "
                           + "Perhaps you added process after call to to?");
            }
            inToOut(exchange);
        }

        protected String getDescription() {
            return "";
        }

        @Override
        public String toString() {
            return "setOutHeaders["+getDescription()+"]";
        }
    }

    /**
     * @param route to apply a HeaderBuilder to
     * @param processor to apply to the route
     * @param <T>
     * @return the route with given header processor applied
     * @see HeaderBuilder
     */
    protected <T extends ProcessorDefinition<? extends T>> T headers(T route, HeaderBuilder processor) {
        return apply(route, processor);
    }

    /**
     * @param route to apply a ProcessDefinitionProcessor to
     * @param processor to apply to the route
     * @param <T>
     * @return the route with given processor applied
     * @see HeaderBuilder
     */
    protected <T extends ProcessorDefinition<? extends T>> T apply(T route, ProcessDefinitionProcessor processor) {
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
        Debugger debug  =  debug(routeId  +  ".ServiceCall");
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

    /*
     * Alternative to {fromHttpGetToDtos} that uses recipientList EIP pattern that allows dynamically specified
     * recipients. Used for enabling Simple Expression in URLs.
     */
    protected <T> ExpressionNode fromHttpGetToDtosWithRecipientList(String routeId, String url, HeaderBuilder headers,
                                                                    TypeReference<T> targetDtoType) {
        Debugger debug  =  debug(routeId  +  ".ServiceCall");
        return headers(
                    from(routeId),
                    headers
                            .get()
        )
        .process(debug)
        .recipientList(simple(url))
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
     * @return a processor to be applied after the response is fetched to save possible service specific HTTP session
     * cookie to cache
     */
    protected Processor saveSession() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) {
                CasTicketCache cache  =  exchange.getProperty(CAS_TICKET_CACHE_PROPERTY, CasTicketCache.class);
                if (cache != null && exchange.getIn() != null) {
                    Map<String,Object> inHeaders = exchange.getIn().getHeaders();
                    String service = exchange.getProperty(CAS_TICKET_CACHE_SERVICE_PROPERTY, String.class);
                    cache.store(service, inHeaders);
                }
                inToOut(exchange);
            }
        };
    }

    /**
     * @return a ProducerTemplate from Spring's ApplicationContext
     */
    protected ProducerTemplate getCamelTemplate() {
        return this.getApplicationContext().getBean(ProducerTemplate.class);
    }

    /**
     * @param url to the endpoint
     * @param timeoutMillis timeout in milliseconds to wait for the HTTP client response
     * @return a URI value with given timeout
     */
    protected String uri(String url, long timeoutMillis) {
        if (url != null) {
            // use camel-http4 component (http->http4)
            url = url.trim().replaceFirst("^http(s?)://", "http$14://");
            if (url.contains("?")) {
                url = url + "&" + HTTP_CLIENT_TIMEOUT_PARAM_NAME + "=" + timeoutMillis;
            }
            else {
                url = url + "?" + HTTP_CLIENT_TIMEOUT_PARAM_NAME + "=" + timeoutMillis;
            }
        }
        return url;
    }

    /**
     * @param url to the endpoint
     * @return a URI value with the default timeout
     */
    protected String uri(String url) {
        return uri(url, DEFAULT_HTTP_TIMEOUT_MILLIS);
    }

    /**
     * Tracks the time elapsed between the two processes phases in Camel route the (same) instance of this Debugger
     * is associated to. Also calls #inToOut at the end. Override doProcess.
     *
     * @see #doProcess(org.apache.camel.Exchange) to do the actual logging
     */
    protected abstract class Debugger implements Processor {
        private String name;

        protected Debugger(String name) {
            this.name  =  name;
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
        public final void process(Exchange exchange) {
            Long beginMoment = exchange.getProperty("DebuggerBeginMoment_"+this.name, Long.TYPE);
            if (beginMoment == null) {
                beginMoment = System.currentTimeMillis();
                exchange.setProperty("DebuggerBeginMoment_"+this.name, beginMoment);
            } else {
                long now = System.currentTimeMillis();
                long duration = now - beginMoment;
                log.debug(getName()  +  " execution took: "  +  duration  +  "ms since last call.");
                exchange.setProperty("DebuggerBeginMoment_"+this.name, null);
            }
            inToOut(exchange);
            doProcess(exchange);
        }

        /**
         * @param exchange to wrapped call to process as in a normal Camel Processor
         * @see Processor#process(org.apache.camel.Exchange)
         */
        public abstract void doProcess(Exchange exchange);

        @Override
        public String toString() {
            return "debugger["+name+"]";
        }
    }

    /**
     * Additional Processors introduced into Camel route definition seem to null the set the out part of the Exchange
     * to in and set the out part as null. After execution of a custom Processor, we therefore want to reverse this.
     *
     * @param exchange for which to set Excahge.out  =  Exchange.in
     */
    protected void inToOut(Exchange exchange) {
        if (exchange.getIn() != null) {
            if (exchange.getOut() == null) {
                // Logically enough, this copies the message as empty
                exchange.setOut(exchange.getIn());
            }
            // Have to set body and headers separately:
            exchange.getOut().setBody(exchange.getIn().getBody());
            exchange.getOut().setHeaders(exchange.getIn().getHeaders());
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
            public void doProcess(Exchange exchange) {
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
            log.debug(name  +  " Exchange in: "  +  debug(exchange.getIn()));
        } else {
            log.warn(name  +  " Exchange in is null!");
        }
    }

    /**
     * @param exchange the Out part of which to be logged
     * @param name the prefix used in logging
     */
    protected void logOut(Exchange exchange, String name) {
        if (exchange.getOut() != null) {
            log.debug(name  +  " Exchange out: "  +  debug(exchange.getOut()));
        } else {
            log.warn(name  +  " Exchange out is null!");
        }
    }

    /**
     * @return a Processor which converts input to output
     * @see #inToOut(org.apache.camel.Exchange)
     */
    protected Processor inToOut() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) {
                inToOut(exchange);
            }

            @Override
            public String toString() {
                return "inToOut[]";
            }
        };
    }

    /**
     * @param message the message
     * @return
     */
    protected String debug(Message message) {
        if (log.isDebugEnabled()) {
            try {
                return stringify(message.getBody())
                    +  " with headers: "
                    +  mapperProvider.getContext(ObjectMapper.class).writeValueAsString(message.getHeaders());
            } catch (IOException e) {
                return stringify(message.getBody())  +  " (Headers could not be converted to JSON cause: "
                        +  e.getMessage()  +  ")";
            }
        }
        return "*DEBUG LOGGING DISABLED*";
    }

    /**
     * @param object to stringify, null-safe
     * @return object as string, converting byte arrays to String, using  .toString() for other objects and "null"
     * for nulls
     */
    protected String stringify(Object object) {
        if(object == null) { 
            return "null";
        }
        if(object.getClass().isArray() && Byte.TYPE.equals(object.getClass().getComponentType())) {
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
     * An utility class to provide a StringBuffer like Expression aware join capability.
     *
     * Implements Expression for convenience (but can be turned to an immutable presentation)
     * @see #toExpression()
     */
    protected class ExpressionBuffer implements Expression {
        private List<Expression> expressions = new ArrayList<Expression>();

        /**
         * @param expression to add to this buffer
         * @return this ExpressionBuffer
         */
        public ExpressionBuffer append(Expression expression) {
            this.expressions.add(expression);
            return this;
        }

        /**
         * @param constant to add to this buffer
         * @return this ExpressionBuffer
         */
        public ExpressionBuffer append(String constant) {
            return append(constant(constant));
        }

        /**
         * @return the number of Expressions in this buffer
         */
        public int size() {
            return this.expressions.size();
        }

        /**
         * @return true iff this buffer is empty
         */
        public boolean isEmpty() {
            return this.expressions.isEmpty();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            if (!type.isAssignableFrom(String.class)) {
                throw new IllegalArgumentException("ExpressionBuffer can not evaluate to type " + type);
            }
            return (T) joined(this.expressions, exchange);
        }

        /**
         * @return an immutable expression presentation of this expression
         */
        public Expression toExpression() {
            final List<Expression> buffer = new ArrayList<Expression>(this.expressions);
            return new Expression() {
                @SuppressWarnings("unchecked")
                @Override
                public <T> T evaluate(Exchange exchange, Class<T> type) {
                    if (!type.isAssignableFrom(String.class)) {
                        throw new IllegalArgumentException("ExpressionBuffer can not evaluate to type " + type);
                    }
                    return (T) joined(buffer, exchange);
                }
            };
        }
    }

    /**
     * @param expressions to join to a String
     * @param exchange to evaluate against
     * @return the evaluated result as a String
     */
    protected static String joined(List<Expression> expressions, Exchange exchange) {
        StringBuffer b = new StringBuffer();
        for (Expression expression : expressions) {
            b.append(expression.evaluate(exchange, String.class));
        }
        return b.toString();
    }

    /**
     * @param expression to URL encode (to a List of String's or String)
     * @return the URL encoded version of the Expression's evaluated value (List of Strings or a String)
     */
    protected Expression urlEncoded(final Expression expression) {
        return new Expression() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                if (List.class.isAssignableFrom(type)) {
                    List<String> values = expression.evaluate(exchange, List.class);
                    List<String> encodedValues = new ArrayList<String>();
                    if (values != null) {
                        for (String value : values) {
                            encodedValues.add(encoded(value));
                        }
                    }
                    return (T) encodedValues;
                }
                if (!type.isAssignableFrom(String.class)) {
                    throw new IllegalArgumentException("URLEncoding can not be evaluated to type " + type);
                }
                String value = expression.evaluate(exchange, String.class);
                if (value != null) {
                    return (T) encoded(value);
                }
                return (T) value;
            }

            private String encoded(String value) {
                try {
                    value = URLEncoder.encode(value, URL_ENCODING);
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }
                return value;
            }
        };
    }

    /**
     * Factor for an expression buffer
     * @return a new ExpressionBuffer
     */
    protected ExpressionBuffer buffer() {
        return new ExpressionBuffer();
    }

    /**
     * @param listExpression an expression evaluating to a List of Strings
     * @param concat the concatenator expression evaluating to a String that is appended between the elements
     *               in the listExpression
     * @param begin the begin expression evaluating to a String which is
     * @return an Expression which evaluates the concatenator as a List of Strings and given that it is not empty or
     * null returns an expression where values in the list are concatenated by the evaluated String value of
     * concat prefixed with possible begin (if given)
     */
    protected Expression concatenatedList(final Expression listExpression, final Expression concat,
                                          final Expression begin) {
        return new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                ExpressionBuffer b = new ExpressionBuffer();
                @SuppressWarnings("unchecked")
                List<String> values = listExpression.evaluate(exchange, List.class);
                if (values != null && !values.isEmpty()) {
                    if (begin != null) {
                        b.append(begin);
                    }
                    b.append(StringHelper.join(concat.evaluate(exchange, String.class), values.toArray(new String[0])));
                }
                return b.evaluate(exchange, type);
            }
        };
    }

    /**
     * @param headerName the name of the header value
     * @return a simple ${in.headers.headerName} expression.
     */
    protected Expression headerInValue(String headerName) {
        return simple("${in.headers."+headerName+"}");
    }

    /**
     * Can be used to avoid some repeating .setHeader-calls when building a Camel route.
     *
     * @see #headers() to construct
     * @see #headers(org.apache.camel.model.ProcessorDefinition,
     *  fi.vm.sade.osoitepalvelu.kooste.common.route.AbstractJsonToDtoRouteBuilder.HeaderBuilder) to apply
     */
    protected class HeaderBuilder implements ProcessDefinitionProcessor {
        private Expression body;
        private Integer retryTimes;
        private Map<String, Expression> headers  =  new HashMap<String, Expression>();
        protected Map<String, Parameter> parameters = new HashMap<String, Parameter>();
        private List<ProcessDefinitionProcessor> additionalProcessors  =  new ArrayList<ProcessDefinitionProcessor>();

        protected HeaderBuilder() {
        }

        /**
         * @param headerName
         * @param expr the value for the header
         * @return this HeaderBuilder with given header set to given Expression value
         * @see Exchange for header constants
         * @see #simple(String) for simple ${ } containing expressions that may change for each call
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
            return query(simple(query));
        }

        /**
         * @param body to set as body to the Message.
         * @return this HeaderBuilder
         */
        public HeaderBuilder body(Expression body) {
            this.body = body;
            return this;
        }

        /**
         * @param query to set as HTTP_QUERY header value.
         * @return this HeaderBuilder
         */
        public HeaderBuilder query(Expression query) {
            return add(Exchange.HTTP_QUERY, query);
        }

        /**
         * @param name of the parameter
         * @return this Parameter for building the parameter value
         */
        public Parameter param(String name) {
            if (!this.parameters.containsKey(name)) {
                return new Parameter(name);
            }
            return this.parameters.get(name);
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
         * and adds an ProcessDefinitionProcessor which marshals the
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
         * @param times to retryOnCamelError at maximum
         * @return this HeaderBuilder
         */
        public HeaderBuilder retry(int times) {
            this.retryTimes = times;
            return this;
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
            for (ProcessDefinitionProcessor defProcessor : additionalProcessors) {
                process  =  defProcessor.process(process);
            }
            if (!this.parameters.isEmpty()) {
                ExpressionBuffer query = buffer(),
                                body = buffer();
                for (Parameter param : this.parameters.values()) {
                    if (param.bodyParameter) {
                        param.buildTo(body);
                    } else {
                        param.buildTo(query);
                    }
                }
                if (!query.isEmpty()) {
                    query(query);
                }
                if (!body.isEmpty()) {
                    body(body);
                }
            }
            for (Entry<String, Expression> header : headers.entrySet()) {
                process  =  process.setHeader(header.getKey(), header.getValue());
            }
            if (this.body != null) {
                process  =  process.setBody(this.body);
            }
            if (this.retryTimes == null) {
                this.retryTimes = DEFAULT_RETRY_LIMIT;
            }
            add(new ProcessDefinitionProcessor() {
                @Override
                @SuppressWarnings({ "rawtypes", "unchecked" })
                public ProcessorDefinition process(ProcessorDefinition process) {
                    return process.onException(HttpOperationFailedException.class)
                            .log(LoggingLevel.ERROR, "HttpOperationFailedException occured.")
                            .retryWhile(
                                    header(Exchange.REDELIVERY_COUNTER)
                                            .isLessThan(retryTimes))
                       .end();
                }
            });
            return process;
        }

        /**
         * Wrapper for query URI and POST body parameters with possibility for optional parameters and multi value
         * parameters for the the sane. Applies URLEncoding to the values.
         */
        public class Parameter {
            private String name;
            private List<Expression> values = new ArrayList<Expression>();
            private Expression listValueContainer;
            private boolean optional=false;
            private boolean bodyParameter=false;

            protected Parameter(String name) {
                this.name = name;
            }

            /**
             * Marks this container optional, meaning that if the values evaluate to null, they are
             * discarded.
             *
             * @return this Parameter
             */
            public Parameter optional() {
                this.optional = true;
                return this;
            }

            /**
             * Sets an expression that evaluates to a List of values of which to be added as single parameters
             * with URL encoding applied
             *
             * @param listValue the expression evaluating to a List
             * @return this Parameter
             */
            public Parameter list(Expression listValue) {
                this.listValueContainer = listValue;
                return this;
            }

            /**
             * Sets this parameter to be result of ${headers.in.<aparameterName>} evaluated into a List
             * @see #list(org.apache.camel.Expression)
             * @see #headerInValue(String)
             *
             * @return this Parameter
             */
            public Parameter listFromHeader() {
                return list(headerInValue(this.name));
            }

            /**
             * Add a value to this parameter container. This will be appended to the query string as
             * name=value1&name=value2. Where the given value parameter evaluates to a single value.
             * If this container is marked optional, msissing/null values are discarded.
             *
             * @see #optional()
             * @see #list(org.apache.camel.Expression) for values that evaluate into List
             *
             * @param value to be added
             * @return this Parameter
             */
            public Parameter value(Expression value) {
                this.values.add(value);
                return this;
            }

            /**
             * Acts as a value with simple-wrapped expression:
             * @see #value(org.apache.camel.Expression)
             *
             * @param value as simple expression to
             * @return this Parameter
             */
            public Parameter value(String value) {
                return value(simple(value));
            }

            /**
             * Acts as a value with simple-wrapped expression:
             * @see #value(org.apache.camel.Expression)
             *
             * @param value the set as constant expression first converted into a string
             * @return this Parameter
             */
            public Parameter value(Object value) {
                return value(constant(""+value));
            }

            /**
             * Adds parameter to be result of ${in.headers.<aparameterName>} evaluated into a single value
             * @see #value(org.apache.camel.Expression)
             * @see #headerInValue(String)
             *
             * @return this Parameter
             */
            public Parameter valueFromHeader() {
                return value(headerInValue(this.name));
            }

            /**
             * Adds parameter to be result of ${in.body} evaluated into a single value
             * @see #value(org.apache.camel.Expression)
             * @see #headerInValue(String)
             *
             * @return this Parameter
             */
            public Parameter valueFromBody() {
                return value(simple("${in.body}"));
            }

            /**
             * Thread this parameter as a query parameter
             *
             * @return the HeaderBuilder assocated with this parameter
             */
            public HeaderBuilder toQuery() {
                this.bodyParameter = false;
                return attachToHeader();
            }

            /**
             * Thread this parameter as a body parameter. NOTE: this overwrites otherwise set body.
             *
             * @return the HeaderBuilder assocated with this parameter
             */
            public HeaderBuilder toBody() {
                this.bodyParameter = true;
                return attachToHeader();
            }

            /**
             * @return true iff any value is appended or list value container is set to this parameter
             */
            public boolean hasValue() {
                return !this.values.isEmpty() || listValueContainer != null;
            }

            private HeaderBuilder attachToHeader() {
                if (!parameters.containsKey(this.name)) {
                    parameters.put(this.name, this);
                }
                return HeaderBuilder.this;
            }

            /**
             * Build the Parameter to a query "string". If the query is not empty, adds & to the beginning.
             *
             * @param query to build this parameter to
             */
            public void buildTo(ExpressionBuffer query){
                for (final Expression value : values) {
                    final boolean first = query.isEmpty();
                    if (optional) {
                        query.append(new Expression() {
                            @SuppressWarnings("unchecked")
                            @Override
                            public <T> T evaluate(Exchange exchange, Class<T> type) {
                                Object evaluated = value.evaluate(exchange, type);
                                if (evaluated == null) {
                                    return (T) "";
                                }
                                return buffer().append(!first ? "&" : "")
                                        .append(name).append("=").append(urlEncoded(constant(evaluated)))
                                        .evaluate(exchange, type);
                            }
                        });
                    } else {
                        if (!first) {
                            query.append("&");
                        }
                        query.append(this.name).append("=").append(urlEncoded(value));
                    }
                }
                if (this.listValueContainer != null) {
                    ExpressionBuffer infix = buffer().append(name).append("=");
                    ExpressionBuffer begin = buffer();
                    if (!query.isEmpty()) {
                        begin.append("&");
                    }
                    begin.append(infix);
                    query.append(concatenatedList(urlEncoded(this.listValueContainer),
                            buffer().append("&").append(infix), begin));
                }
            }
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
        private Map<String, Object> map  =  new HashMap<String, Object>();

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
        public Map<String, Object> map() {
            return this.map;
        }

        /**
         * @return a copy of this HeaderValueBuilder
         */
        public HeaderValueBuilder copy() {
            HeaderValueBuilder builder = new HeaderValueBuilder();
            builder.map = new HashMap<String,Object>(this.map);
            return builder;
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
        Map<String, Object> properties  =  new HashMap<String, Object>();
        CasTicketCache cache  =  requestContext.getTicketCache();
        if (cache != null) {
            properties.put(CAS_TICKET_CACHE_PROPERTY, cache);
        }
        requestContext.requestPerformed();
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
        ExchangePattern pattern  =  ExchangePattern.InOut;
        Endpoint endpoint = template.getCamelContext().getEndpoint(endpointUri);
        if (endpoint == null) {
            throw new NoSuchEndpointException(endpointUri);
        }
        Exchange exchange  =  template.send(endpoint, pattern, new Processor() {
            public void process(Exchange exchange) {
                for (Map.Entry<String, Object> property : properties.entrySet()) {
                    exchange.setProperty(property.getKey(), property.getValue());
                }
                Message in  =  exchange.getIn();
                for (Map.Entry<String, Object> header : headers.entrySet()) {
                    in.setHeader(header.getKey(), header.getValue());
                }
                in.setBody(body);
            }

            @Override
            public String toString() {
                return "setInitialHeadersAndProperties[sendBodyHeadersAndProperties]";
            }
        });
        Object result  =  ExchangeHelper.extractResultBody(exchange, pattern);
        if (pattern.isOutCapable()) {
            return template.getCamelContext().getTypeConverter().convertTo(type, result);
        } else {
            // return null if not OUT capable
            return null;
        }
    }
}
