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
import com.wordnik.swagger.annotations.ApiOperation;
import fi.vm.sade.osoitepalvelu.kooste.common.ObjectMapperProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.scheduled.ScheduledAituDataFetchTask;
import fi.vm.sade.osoitepalvelu.kooste.scheduled.ScheduledOrganisaatioCacheTask;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.OrganisaatioService;
import fi.vm.sade.osoitepalvelu.kooste.service.settings.AppSettingsService;
import fi.vm.sade.osoitepalvelu.kooste.service.settings.dto.AppSettingsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.sade.properties.OphProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.apache.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(value="app",
    description = "Tuottaa käyttöliittymää varten aikaisessa latausvaiheessa (mm. lokalisointipavlvelun sijainti) " +
            "tarvittavat asetukset ja tarjoaa kehityksen tueksi toimenpiteitä välimuistin hallintaan.")
@Controller
@Scope(value =  WebApplicationContext.SCOPE_APPLICATION)
@RequestMapping(value  =  "/app")
public class AppSettingsController extends AbstractMvcController {
    @Autowired
    private AppSettingsService appSettingsService;

    @Autowired
    private ObjectMapperProvider objectMapperProvider;

    @Autowired
    private ScheduledOrganisaatioCacheTask organisaatioCacheTask;

    @Autowired
    private ScheduledAituDataFetchTask aituDataFetchTask;

    @Autowired
    private OrganisaatioService organisaatioService;

    @Autowired
    private KoodistoService koodistoService;
    private OphProperties ophProperties= new OphProperties();

    @ApiOperation("Palauttaa sovelluksen polkuviittauksia sisältävät, aikaisessa käyttöliittymän alustuksen "
            + "vaiheessa tarpeelliset asetukset käyttöliittymälle JavaScriptinä, joka tuottaa asetukset" +
            " window.CONFIG -olioon.")
    @RequestMapping(value  =  "/settings.js", method  =  RequestMethod.GET, produces  =  "text/javascript")
    @ResponseBody
    public String settingsJs() throws IOException {
        AppSettingsDto settings  =  appSettingsService.getUiSettings();
        ObjectMapper mapper  =  objectMapperProvider.getContext(ObjectMapper.class);
        return "window.CONFIG  =  "  +  mapper.writeValueAsString(settings)  +  ";";
    }

    @ApiOperation("Palauttaa URL-propertyt javascript-muodossa.")
    @RequestMapping(value  =  "/url-props.js", method  =  RequestMethod.GET, produces  =  "application/javascript")
    @ResponseBody
    public String urlProperties() throws IOException {
        return "window.urls.addOverrides("+ ophProperties.frontPropertiesToJson()+")";
    }

    @ApiOperation("Päivittää Organisaatiovälimuistin. Käytettävissä vain paikallisena pyyntönä kehityksen tukena. " +
            "Normaalisti tätä ei ole tarpeen kutsua, sillä päivitys suoritetaan automaattisesti eräajona.")
    @PreAuthorize("hasIpAddress('127.0.0.1/24')")
    @RequestMapping(value = "/refereshOrganisaatioCache", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public int buildOrganisaatioCache() {
        organisaatioCacheTask.refreshOrganisaatioCache();
        return HttpStatus.SC_OK;
    }

    @ApiOperation("Päivittää Organisaatiovälimuistin siltä osin kuin tarpeellista." +
        "Käytettävissä vain paikallisena pyyntönä kehityksen tukena. " +
        "Normaalisti tätä ei ole tarpeen kutsua, sillä operaatio suoritetaan sovelluksen käynnistymisen yhteydessä.")
    @PreAuthorize("hasIpAddress('127.0.0.1/24')")
    @RequestMapping(value = "/ensureOrganisaatioCacheFresh", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public int ensureOrganisaatioCacheFresh() {
        organisaatioCacheTask.ensureOrganisaatioCacheFresh();
        return HttpStatus.SC_OK;
    }

    @ApiOperation("Poistaa Koodistoon liittyvät välimuistit sovelluksen ajonaikaisesta muistista " +
            "sekä välimuistista MongoDB-tietokannasta. Käytettävissä vain paikallisena pyyntönä kehityksen tukena. "+
            "Normaalisti tätä ei ole tarpeen kutsua, sillä koodiston välimuistissa on elinaika ja se päivitetään " +
            "automaattisesti.")
    @PreAuthorize("hasIpAddress('127.0.0.1/24')")
    @RequestMapping(value = "/purgeKoodistoCaches", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public int purgeKoodistoCaches() {
        koodistoService.purgeCaches();
        return HttpStatus.SC_OK;
    }

    @ApiOperation("Käy uudelleen Y-tunnustiedot organisaatioille hierarkiahaun kautta. " +
        "Käytettävissä vain paikallisena pyyntönä kehityksen tukena. Normaalisti tätä ei ole tarpeen kutsua, sillä " +
        "operaatio suoritetaan automaattisesti ajastettuna organisaatiovälimuistin päivittämisen yhteydessä ja " +
        "sovelluksen käynnistymisen yhteydessä.")
    @PreAuthorize("hasIpAddress('127.0.0.1/24')")
    @RequestMapping(value = "/updateOrganisaatioYtunnusDetails", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public int updateYtunnusDetails() {
        organisaatioService.updateOrganisaatioYtunnusDetails(new DefaultCamelRequestContext());
        return HttpStatus.SC_OK;
    }

    @ApiOperation("Noutaa tiedot AITU-palvelusta. Käytettävissä vain paikallisena pyyntönä kehityksen tukena. " +
            "Normaalisti tätä ei ole tarpeen kutsua, sillä sovellus käy tiedot automaattisesti ajastetusti sekä " +
            "käynnistymisen yhteydessä.")
    @PreAuthorize("hasIpAddress('127.0.0.1/24')")
    @RequestMapping(value = "/fetchAituData", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public int fetchAituData() {
        aituDataFetchTask.refreshAituData();
        return HttpStatus.SC_OK;
    }

}
