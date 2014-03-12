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
import java.util.Locale;
import java.util.Map;

import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodistoDto.KoodistoTyyppi;


@Api("Koodiston valintakriteerit")
@Controller
@RequestMapping(value = "/koodisto")
public class KoodistoMvcController extends AbstractMvcController implements Serializable {
    private static final long serialVersionUID = 4201472261383514311L;

    /// TODO:
    public static final Locale UI_LOCALE = new Locale("fi", "FI");

    @Autowired
    private KoodistoService koodistoService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<KoodistoTyyppi, List<UiKoodiItemDto>> findAllOptions() {
        return koodistoService.findAllKoodistos(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/oppilaitostyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findOppilaitosTyyppiOptions() {
        return koodistoService.findOppilaitosTyyppiOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/omistajatyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findOmistajaTyyppiOptions() {
        return koodistoService.findOmistajaTyyppiOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/vuosiluokka")
    @ResponseBody
    public List<UiKoodiItemDto> findVuosiluokkaOptions() {
        return koodistoService.findVuosiluokkaOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/maakunta")
    @ResponseBody
    public List<UiKoodiItemDto> findMaakuntaOptions() {
        return koodistoService.findMaakuntaOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kunta")
    @ResponseBody
    public List<UiKoodiItemDto> findKuntaOptions() {
        return koodistoService.findKuntaOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tutkintotyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintoTyyppiOptions() {
        return koodistoService.findTutkintoTyyppiOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tutkinto")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintoOptions() {
        return koodistoService.findTutkintoOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/opetuskieli")
    @ResponseBody
    public List<UiKoodiItemDto> findOppilaitoksenOpetuskieliOptions() {
        return koodistoService.findOppilaitoksenOpetuskieliOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kielivalikoima")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutuksenKieliOptions() {
        return koodistoService.findKoulutuksenKieliOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/koulutusaste")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutusAsteOptions() {
        return koodistoService.findKoulutusAsteOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/koulutuksenjarjestaja")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutuksenJarjestejaOptions() {
        return koodistoService.findKoulutuksenJarjestejaOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/opintoala")
    @ResponseBody
    public List<UiKoodiItemDto> findOpintoAlaOptions() {
        return koodistoService.findOpintoAlaOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/avi")
    @ResponseBody
    public List<UiKoodiItemDto> findAlueHallintoVirastoOptions() {
        return koodistoService.findAlueHallintoVirastoOptions(UI_LOCALE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kayttoikeusryhma")
    @ResponseBody
    public List<UiKoodiItemDto> findKayttooikeusryhmas() {
        return koodistoService.findKayttooikeusryhmas(UI_LOCALE);
    }
}
