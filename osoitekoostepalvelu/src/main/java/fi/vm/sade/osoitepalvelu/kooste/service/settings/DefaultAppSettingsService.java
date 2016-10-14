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
import fi.vm.sade.osoitepalvelu.kooste.service.settings.dto.UrlPropertiesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ratamaa
 * Date: 3/18/14
 * Time: 12:55 PM
 */
@Service
public class DefaultAppSettingsService extends AbstractService implements AppSettingsService,
        EmbeddedValueResolverAware {
    private final Logger logger  =  LoggerFactory.getLogger(getClass());

    private static final Pattern EXPRESSION  =  Pattern.compile("\\$\\{(.*?)\\}");

    @Resource(name  =  "uiAppProperties")
    private Properties uiAppProperties;

    @Resource(name  =  "uiEnvProperties")
    private Properties uiEnvProperties;

    @Resource(name  =  "uiUrlProperties")
    private Properties uiUrlProperties;

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

    @Override
    public UrlPropertiesDto getUrlProperties() {
        UrlPropertiesDto urlPropertiesDto = new UrlPropertiesDto();
        for (Map.Entry<Object, Object> value : uiUrlProperties.entrySet()) {
            urlPropertiesDto.getUrls().put(value.getKey().toString(), parseExpression(value.getValue()));
        }
        return urlPropertiesDto;
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
