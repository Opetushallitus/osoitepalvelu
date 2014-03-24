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

import com.wordnik.swagger.annotations.Api;
import fi.vm.sade.osoitepalvelu.kooste.common.ObjectMapperProvider;
import fi.vm.sade.osoitepalvelu.kooste.service.settings.AppSettingsService;
import fi.vm.sade.osoitepalvelu.kooste.service.settings.dto.AppSettingsDto;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

/**
 * User: ratamaa
 * Date: 3/18/14
 * Time: 12:41 PM
 */
@Api("Sovelluksen asetukset")
@Controller
@Scope(value= WebApplicationContext.SCOPE_APPLICATION)
@RequestMapping(value = "/app")
public class AppSettingsController extends AbstractMvcController {
    @Autowired
    private AppSettingsService appSettingsService;

    @Autowired
    private ObjectMapperProvider objectMapperProvider;

    @RequestMapping(value = "/settings.js", method = RequestMethod.GET,
            produces = "text/javascript")
    public @ResponseBody String settingsJs() throws IOException {
        AppSettingsDto settings = appSettingsService.getUiSettings();
        ObjectMapper mapper = objectMapperProvider.getContext(ObjectMapper.class);
        return "window.CONFIG = " + mapper.writeValueAsString(settings) + ";";
    }
}