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

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mangofactory.swagger.configuration.JacksonSwaggerSupport;
import com.mangofactory.swagger.configuration.SwaggerApiListingJsonSerializer;
import com.mangofactory.swagger.configuration.SwaggerResourceListingJsonSerializer;
import com.mangofactory.swagger.models.DefaultModelPropertiesProvider;
import com.wordnik.swagger.model.ApiListing;
import com.wordnik.swagger.model.ResourceListing;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 7/21/14
 * Time: 3:48 PM
 */
public class CustomJacksonSwaggerSupport extends JacksonSwaggerSupport {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public ObjectMapper getSpringsMessageConverterObjectMapper() {
        return objectMapper;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    @PostConstruct
    public void setup() {
        this.objectMapper.registerModule(swaggerSerializationModule());

        Map<String, DefaultModelPropertiesProvider> beans =
                this.applicationContext.getBeansOfType(DefaultModelPropertiesProvider.class);

        for (DefaultModelPropertiesProvider defaultModelPropertiesProvider : beans.values()) {
            defaultModelPropertiesProvider.setObjectMapper(this.objectMapper);
        }
    }

    private Module swaggerSerializationModule() {
        SimpleModule module = new SimpleModule("SwaggerJacksonModule");
        module.addSerializer(ApiListing.class, new SwaggerApiListingJsonSerializer());
        module.addSerializer(ResourceListing.class, new SwaggerResourceListingJsonSerializer());
        return module;
    }
}
