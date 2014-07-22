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

package fi.vm.sade.osoitepalvelu.kooste.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Use this to be able to use Jackson 2 mapper instead of 1.x mapper with charset forced to UTF-8 (which would not
 * happen with plain String @ResponseBody).
 *
 * User: ratamaa
 * Date: 7/21/14
 * Time: 6:09 PM
 */
public class Jackson2View implements View {
    protected Logger logger  =  LoggerFactory.getLogger(getClass());
    private ObjectMapper objectMapper;
    private Object model;

    public Jackson2View(ObjectMapper objectMapper, Object model) {
        this.objectMapper = objectMapper;
        this.model = model;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(getContentType());
        response.setHeader("Cache-Control", "no-cache");
        try {
            if (this.model == null) {
                response.getWriter().write("{}");
            } else {
                objectMapper.writeValue(response.getWriter(), this.model);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error prosessing JSON: " + e.getMessage(), e);
            response.setStatus(500);
        } finally {
            response.getWriter().close();
        }
    }
}
