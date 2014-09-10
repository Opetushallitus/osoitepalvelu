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

package fi.vm.sade.osoitepalvelu.kooste.mvc;

import fi.vm.sade.osoitepalvelu.kooste.common.exception.AuthorizationException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.SelfExplainingException;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.RuntimeExchangeException;
import org.apache.camel.component.http.HttpOperationFailedException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.util.Locale;

/**
 * User: ratamaa
 * Date: 12/11/13
 * Time: 4:32 PM
 */
public abstract class AbstractMvcController {
    public static final Locale DEFAULT_UI_LOCALE  =  new Locale("fi", "FI");

    protected Logger logger  =  LoggerFactory.getLogger(getClass());

    @Value("${produce.error.message.stack.traces:false}")
    protected boolean produceErrorStackTraces = false;

    protected Locale parseLocale(String locale) {
        return LocaleHelper.parseLocale(locale, DEFAULT_UI_LOCALE);
    }

    @ResponseStatus(value  =  HttpStatus.NOT_FOUND) // 404 Entity not found by primary key.
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFound(HttpServletRequest req, NotFoundException exception) {
        return handeException(req, exception, "not_found_error", exception);
    }

    @ResponseStatus(value  =  HttpStatus.UNAUTHORIZED) // 401 Not authorized
    @ExceptionHandler(AuthorizationException.class)
    public ModelAndView notAuthorized(HttpServletRequest req, AuthorizationException exception) {
        return handeException(req, exception, "not_authorized_error", exception);
    }

    @ResponseStatus(value  =  HttpStatus.BAD_REQUEST) // 400 Bad request.
    @ExceptionHandler(ServletException.class)
    public ModelAndView badRequest(HttpServletRequest req, ServletException exception) {
        return handeException(req, exception, "bad_request_error", exception, exception.getMessage());
    }

    @ResponseStatus(value  =  HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(RuntimeCamelException.class)
    public ModelAndView camelException(HttpServletRequest req, RuntimeCamelException exception) {
        Exchange exchange = null;
        if (exception instanceof RuntimeExchangeException) {
            exchange = ((RuntimeExchangeException) exception).getExchange();
        }
        String service = null;
        if (exchange != null) {
            service = exchange.getFromEndpoint().getEndpointUri();
        }
        ModelAndView byCause = handleByCause(req, exception, service);
        if (byCause != null) {
            return byCause;
        }
        return handeException(req, exception, "camel_error", service);
    }

    private ModelAndView handleByCause(HttpServletRequest req, Throwable exception, String service) {
        Throwable cause = exception.getCause();
        while (cause != null && cause.getCause() != null) {
            if (service == null && cause instanceof RuntimeExchangeException) {
                service = ((CamelExecutionException) cause).getExchange().getFromEndpoint().getEndpointUri();
            }
            cause = cause.getCause();
        }
        if (cause != null) {
            if (cause instanceof HttpOperationFailedException) {
                int statusCode = ((HttpOperationFailedException) cause).getStatusCode();
                String url = ((HttpOperationFailedException) cause).getUri();
                return handeException(req, exception, "camel_http_error", service, statusCode, url);
            }
            if (cause instanceof SocketTimeoutException) {
                return handeException(req, exception, "camel_http_call_timeout_error", service);
            }
        }
        return null;
    }

    @ResponseStatus(value  =  HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(Throwable.class)
    public ModelAndView otherException(HttpServletRequest req, Throwable exception) {
        ModelAndView byCause = handleByCause(req, exception, null);
        if (byCause != null) {
            return byCause;
        }
        return handeException(req, exception, "internal_error");
    }

    protected ModelAndView handeException(HttpServletRequest req, Throwable exception, String defaultMessgeKey,
                                          Object... defaultMessageParams) {
        logger.error("Request: "  +  req.getRequestURL()  +  " raised "  +  exception, exception);
        ModelAndView mav  =  new ModelAndView();
        String messageKey  =  defaultMessgeKey;
        Object[] params  =  defaultMessageParams;
        String errno  =  "";
        if (exception instanceof SelfExplainingException) {
            SelfExplainingException e  =  (SelfExplainingException) exception;
            messageKey  =  e.getMessageKey();
            params  =  e.getMessageParams();
            errno  =  e.getErrorCode();
        }
        mav.addObject("exceptionMessage", exception.getMessage());
        if (produceErrorStackTraces) {
            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));
            mav.addObject("exceptionStackTrace",stackTrace.toString());
        }
        mav.addObject("messageKey", messageKey);
        mav.addObject("messageParams", params);
        mav.addObject("errorCode", errno);
        mav.addObject("errorType", exception.getClass().getSimpleName());
        mav.addObject("time", new DateTime());
        mav.addObject("url", req.getRequestURL());
        mav.addObject("method", req.getMethod());
        mav.addObject("parameters", req.getParameterMap());
        mav.setView(new MappingJacksonJsonView());
        return mav;
    }
}
