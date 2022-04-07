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

package fi.vm.sade.osoitepalvelu.kooste.service.settings;

import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.settings.dto.AppSettingsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DefaultAppSettingsService extends AbstractService implements AppSettingsService,
        EmbeddedValueResolverAware {
    private final Logger logger  =  LoggerFactory.getLogger(getClass());

    private static final Pattern EXPRESSION  =  Pattern.compile("\\$\\{(.*?)\\}");

    @Autowired
    @Qualifier("uiAppProperties")
    private Properties uiAppProperties;

    @Autowired
    @Qualifier("uiEnvProperties")
    private Properties uiEnvProperties;

    private StringValueResolver resolver;

    @Override
    public AppSettingsDto getUiSettings() {
        AppSettingsDto settings  =  new AppSettingsDto();
        for (Map.Entry<Object, Object> value : uiEnvProperties.entrySet()) {
            settings.getEnv().put(value.getKey().toString(), parseExpression(value.getValue()));
        }
        for (Map.Entry<Object, Object> value : uiAppProperties.entrySet()) {
            settings.getApp().put(value.getKey().toString(), parseExpression(value.getValue()));
        }
        return settings;
    }

    protected Object parseExpression(Object exp) {
        if(exp instanceof String) {
            String expressionString  =  (String)exp;
            Matcher m  =  EXPRESSION.matcher(expressionString);
            while (m.find()) {
                String expContainer  =  m.group(0);
                String returnValue  =  resolver.resolveStringValue(expContainer);
                if (returnValue == null || expContainer.equals(returnValue)) {
                    logger.warn("No value for env property: "  +  expContainer);
                    returnValue = "";
                }
                expressionString  =  expressionString.replace(expContainer, returnValue);
            }
            return expressionString;
        }
        return exp;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver  =  resolver;
    }
}
