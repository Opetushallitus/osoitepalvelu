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

package fi.vm.sade.osoitepalvelu.kooste.config.swagger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangofactory.swagger.configuration.JacksonSwaggerSupport;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.wordnik.swagger.model.ResponseMessage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.mangofactory.swagger.ScalaUtils.toOption;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * User: ratamaa
 * Date: 7/21/14
 * Time: 2:30 PM
 */
@Configuration
public class CustomSwaggerConfig extends SpringSwaggerConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper  =  new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return objectMapper;
    }

    @SuppressWarnings("unchecked")
    @Bean
    public Map<RequestMethod, List<ResponseMessage>> defaultResponseMessages() {
        LinkedHashMap<RequestMethod, List<ResponseMessage>> responses = newLinkedHashMap();
        responses.put(GET, asList(
                new ResponseMessage(OK.value(), OK.getReasonPhrase(), toOption(null)),
                new ResponseMessage(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(), toOption(null)),
                new ResponseMessage(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase(), toOption(null)),
                new ResponseMessage(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), toOption(null))
        ));

        responses.put(PUT, asList(
                new ResponseMessage(CREATED.value(), CREATED.getReasonPhrase(), toOption(null)),
                new ResponseMessage(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(), toOption(null)),
                new ResponseMessage(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase(), toOption(null)),
                new ResponseMessage(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), toOption(null))
        ));

        responses.put(POST, asList(
                new ResponseMessage(CREATED.value(), CREATED.getReasonPhrase(), toOption(null)),
                new ResponseMessage(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(), toOption(null)),
                new ResponseMessage(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase(), toOption(null)),
                new ResponseMessage(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), toOption(null))
        ));

        responses.put(DELETE, asList(
                new ResponseMessage(NO_CONTENT.value(), CREATED.getReasonPhrase(), toOption(null)),
                new ResponseMessage(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase(), toOption(null)),
                new ResponseMessage(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), toOption(null))
        ));

        // This needed to be commented because the enum value PATCH does not exists in Spring 3.1.x:
//        responses.put(PATCH, asList(
//                new ResponseMessage(NO_CONTENT.value(), CREATED.getReasonPhrase(), toOption(null)),
//                new ResponseMessage(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase(), toOption(null)),
//                new ResponseMessage(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), toOption(null))
//        ));

        responses.put(TRACE, asList(
                new ResponseMessage(NO_CONTENT.value(), CREATED.getReasonPhrase(), toOption(null)),
                new ResponseMessage(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase(), toOption(null)),
                new ResponseMessage(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), toOption(null))
        ));

        responses.put(OPTIONS, asList(
                new ResponseMessage(NO_CONTENT.value(), CREATED.getReasonPhrase(), toOption(null)),
                new ResponseMessage(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase(), toOption(null)),
                new ResponseMessage(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), toOption(null))
        ));
        responses.put(HEAD, asList(
                new ResponseMessage(NO_CONTENT.value(), CREATED.getReasonPhrase(), toOption(null)),
                new ResponseMessage(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase(), toOption(null)),
                new ResponseMessage(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), toOption(null))
        ));
        return responses;
    }

    private List<ResponseMessage> asList(ResponseMessage... responseMessages) {
        List<ResponseMessage> list = new ArrayList<ResponseMessage>();
        for (ResponseMessage responseMessage : responseMessages) {
            list.add(responseMessage);
        }
        return list;
    }


    @Bean
    @Override
    public JacksonSwaggerSupport jacksonScalaSupport() {
        return new CustomJacksonSwaggerSupport();
    }
}
