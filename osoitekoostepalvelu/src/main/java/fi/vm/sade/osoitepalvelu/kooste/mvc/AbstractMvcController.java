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
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * User: ratamaa
 * Date: 12/11/13
 * Time: 4:32 PM
 */
public abstract class AbstractMvcController {
    public static final Locale DEFAULT_UI_LOCALE = new Locale("fi", "FI");

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Locale parseLocale(String locale) {
        return LocaleHelper.parseLocale(locale, DEFAULT_UI_LOCALE);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found by primary key.") // 404
    @ExceptionHandler(NotFoundException.class)
    public void notFound() {
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Not authorized.") // 401
    @ExceptionHandler(AuthorizationException.class)
    public void notAuthorized() {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(Throwable.class)
    public ModelAndView otherCheckedException(HttpServletRequest req, Throwable exception) {
        logger.error("Request: " + req.getRequestURL() + " raised " + exception);

        ModelAndView mav = new ModelAndView();
        String messageKey = "internal_error";
        Object[] params = new Object[0];
        String errno = "";
        if( exception instanceof SelfExplainingException) {
            SelfExplainingException e = (SelfExplainingException) exception;
            messageKey = e.getMessageKey();
            params = e.getMessageParams();
            errno = e.getErrorCode();
        }
        mav.addObject("exceptionMessage", exception.getMessage());
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
