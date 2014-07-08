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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoDto.KoodistoTyyppi;


@Api("Koodiston valintakriteerit")
@Controller
@RequestMapping(value  =  "/koodisto")
public class KoodistoMvcController extends AbstractMvcController implements Serializable {
    private static final long serialVersionUID  =  4201472261383514311L;

    @Autowired
    private KoodistoService koodistoService;

    @RequestMapping(method  =  RequestMethod.GET)
    @ResponseBody
    public Map<KoodistoTyyppi, List<UiKoodiItemDto>> findAllOptions(@RequestParam("lang") String lang) {
        return koodistoService.findAllKoodistos(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/oppilaitostyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findOppilaitosTyyppiOptions(@RequestParam("lang") String lang) {
        return koodistoService.findOppilaitosTyyppiOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/omistajatyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findOmistajaTyyppiOptions(@RequestParam("lang") String lang) {
        return koodistoService.findOmistajaTyyppiOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/vuosiluokka")
    @ResponseBody
    public List<UiKoodiItemDto> findVuosiluokkaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findVuosiluokkaOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/maakunta")
    @ResponseBody
    public List<UiKoodiItemDto> findMaakuntaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findMaakuntaOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/kunta")
    @ResponseBody
    public List<UiKoodiItemDto> findKuntaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKuntaOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/tutkintotyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintoTyyppiOptions(@RequestParam("lang") String lang) {
        return koodistoService.findTutkintoTyyppiOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/tutkinto")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintoOptions(@RequestParam("lang") String lang) {
        return koodistoService.findTutkintoOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/koulutus")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutusOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutusOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/opetuskieli")
    @ResponseBody
    public List<UiKoodiItemDto> findOppilaitoksenOpetuskieliOptions(@RequestParam("lang") String lang) {
        return koodistoService.findOppilaitoksenOpetuskieliOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/kielivalikoima")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutuksenKieliOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutuksenKieliOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/koulutusaste")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutusAsteOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutusAsteOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/koulutuksenjarjestaja")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutuksenJarjestejaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutuksenJarjestejaOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/opintoala")
    @ResponseBody
    public List<UiKoodiItemDto> findOpintoAlaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findOpintoAlaOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/avi")
    @ResponseBody
    public List<UiKoodiItemDto> findAlueHallintoVirastoOptions(@RequestParam("lang") String lang) {
        return koodistoService.findAlueHallintoVirastoOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/kayttoikeusryhma")
    @ResponseBody
    public List<UiKoodiItemDto> findKayttooikeusryhmas(@RequestParam("lang") String lang) {
        return koodistoService.findKayttooikeusryhmas(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/tutkintotoimikuntaRoolis")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintotoimikuntaRoolis(@RequestParam("lang") String lang) {
        return koodistoService.findTutkintotoimikuntaRooliOptions(parseLocale(lang));
    }

    @RequestMapping(method  =  RequestMethod.GET, value  =  "/tutkintotoimikuntas")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintotoimikuntas(@RequestParam("lang") String lang) {
        return koodistoService.findTutkintotoimikuntaOptions(parseLocale(lang));
    }

}