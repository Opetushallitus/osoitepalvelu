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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.mangofactory.swagger.core.SwaggerCache;
import com.wordnik.swagger.model.ApiListing;
import com.wordnik.swagger.model.ResourceListing;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.mvc.view.Jackson2View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.Map;

/**
 * Based on DefaultSwaggerController.  Swagger 0.8.x requires Jackson 2 through the use of Scala object in MVC interface.
 * (Manual use of Jackson 2 because both versions can't be used at the same time in the project)
 *
 * User: ratamaa
 * Date: 7/21/14
 * Time: 4:58 PM
 */
@Controller
public class CustomSwaggerController extends AbstractMvcController {
    public static final String DOCUMENTATION_BASE_PATH = "/docs";

    @Autowired
    private SwaggerCache swaggerCache;

    @Autowired
    private ObjectMapper objectMapper;

    @ApiIgnore
    @ResponseBody
    @RequestMapping(value = {DOCUMENTATION_BASE_PATH }, method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public View getResourceListing(@RequestParam(value = "group", required = false)
                String swaggerGroup) throws NotFoundException, JsonProcessingException {
        return new Jackson2View(objectMapper, getSwaggerResourceListing(swaggerGroup));
    }

    @ApiIgnore
    @ResponseBody
    @RequestMapping(value = {DOCUMENTATION_BASE_PATH + "/{swaggerGroup}/{apiDeclaration}"}, method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public View getApiListing(@PathVariable("swaggerGroup") String swaggerGroup,
                                @PathVariable("apiDeclaration") String apiDeclaration)
            throws NotFoundException, JsonProcessingException {
        return new Jackson2View(objectMapper, getSwaggerApiListing(swaggerGroup, apiDeclaration));
    }

    private ApiListing getSwaggerApiListing(String swaggerGroup, String apiDeclaration) throws NotFoundException {
        Map<String, ApiListing> apiListingMap = swaggerCache.getSwaggerApiListingMap().get(swaggerGroup);
        if (null != apiListingMap) {
            ApiListing apiListing = apiListingMap.get(apiDeclaration);
            if (null != apiListing) {
                return apiListing;
            }
        }
        throw new NotFoundException("Swagger API listing not found for " + swaggerGroup + " and decl: " + apiDeclaration);
    }

    private ResourceListing getSwaggerResourceListing(String swaggerGroup) throws NotFoundException {
        ResourceListing resourceListing = null;

        if (null == swaggerGroup) {
            resourceListing = swaggerCache.getSwaggerApiResourceListingMap().values().iterator().next();
        } else {
            if (swaggerCache.getSwaggerApiResourceListingMap().containsKey(swaggerGroup)) {
                resourceListing = swaggerCache.getSwaggerApiResourceListingMap().get(swaggerGroup);
            }
        }
        if (resourceListing == null) {
            throw new NotFoundException("Resource listing for group " +swaggerGroup + " not found.");
        }
        return resourceListing;
    }
}
