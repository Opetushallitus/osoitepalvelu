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
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoDto.KoodistoTyyppi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Api(value="koodisto",
    description = "Tarjoaa yksinkertaistetun ja Osoitepalvelun välimuistissa hallitun rajapinnan Osoitepalvelun " +
            " Koodistopalvelusta tarvitsemiin tietoihin sekä muista palveluista saataviin listamaisiin valintoihin.")
@Controller
@RequestMapping(value  =  "/koodisto")
public class KoodistoMvcController extends AbstractMvcController implements Serializable {
    private static final long serialVersionUID  =  4201472261383514311L;

    @Autowired
    private KoodistoService koodistoService;

    @ApiOperation("Palauttaa kaikki koodit tyypeittäin. Voidaan käyttää käyttöliittymässä vaihtoehtoisena tapana "
        + " yksittäisille noudoille.")
    @RequestMapping(method  =  RequestMethod.GET)
    @ResponseBody
    public Map<KoodistoTyyppi, List<UiKoodiItemDto>> findAllOptions(@RequestParam("lang") String lang) {
        return koodistoService.findAllKoodistos(parseLocale(lang));
    }

    @ApiOperation("Palauttaa oppilaitostyypit.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/oppilaitostyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findOppilaitosTyyppiOptions(@RequestParam("lang") String lang) {
        return koodistoService.findOppilaitosTyyppiOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa omistajatyypit.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/omistajatyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findOmistajaTyyppiOptions(@RequestParam("lang") String lang) {
        return koodistoService.findOmistajaTyyppiOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa vuosiluokat.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/vuosiluokka")
    @ResponseBody
    public List<UiKoodiItemDto> findVuosiluokkaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findVuosiluokkaOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa maakunnat.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/maakunta")
    @ResponseBody
    public List<UiKoodiItemDto> findMaakuntaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findMaakuntaOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa kunnat.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/kunta")
    @ResponseBody
    public List<UiKoodiItemDto> findKuntaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKuntaOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa kielet.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/kieli")
    @ResponseBody
    public List<UiKoodiItemDto> findKieliOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKieliOptions(parseLocale(lang));
    }


    @ApiOperation("Palauttaa tutkintotyypit.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/tutkintotyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintoTyyppiOptions(@RequestParam("lang") String lang) {
        return koodistoService.findTutkintoTyyppiOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa tutkinnot.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/tutkinto")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintoOptions(@RequestParam("lang") String lang) {
        return koodistoService.findTutkintoOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa koulutusalat.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/koulutusala")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutusAlaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutusAlaOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa kaikki opintoalat haluttaessa rajattuna yhden koulutusalan perusteella.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/opintoala")
    @ResponseBody
    public List<UiKoodiItemDto> findOpintoAlaOptions(@RequestParam("lang") String lang,
                             @RequestParam(value="koulutusala", required = false) String koulutusala) {
        Locale locale = parseLocale(lang);
        if (koulutusala != null) {
            return koodistoService.findOpintoalasByKoulutusalaAlaUri(locale, koulutusala);
        }
        return koodistoService.findOpintoAlaOptions(locale);
    }

    @ApiOperation("Palauttaa opintoalat rajattuna useampaan koulutusalaan. " +
            "POST-tyyppinen URL:n maksimittarajoituksen välttämiseksi.")
    @RequestMapping(method  =  RequestMethod.POST, value  =  "/opintoala")
    @ResponseBody
    public List<UiKoodiItemDto> findOpintoAalaOptionsByKoulutusalas(@RequestParam("lang") String lang,
                           @RequestParam(value="koulutusala", required = false) String[] koulutusalas) {
        return koodistoService.findOpintoAlasByKoulutusAlas(parseLocale(lang), koulutusalas);
    }

    @ApiOperation("Palauttaa koulutustyypit.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/koulutustyyppi")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutusTyyppiOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutusTyyppiOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa koulutuslajit.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/koulutuslaji")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutusLajiOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutusLajiOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa koulutukset haluttaessa rajattuna yhteen opintoalaan.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/koulutus")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutusOptions(@RequestParam("lang") String lang,
                                                    @RequestParam(value="opintoala", required = false) String opintoala) {
        Locale locale = parseLocale(lang);
        if (opintoala != null) {
            return koodistoService.findKoulutusByOpintoalaUri(locale, opintoala);
        }
        return koodistoService.findKoulutusOptions(locale);
    }

    @ApiOperation("Palauttaa koulutukset rajattuna haluttaessa useampaan opintoalaan tai koulutustyyppiin. " +
            "POST-tyyppinen URL:n maksimittarajoituksen välttämiseksi.")
    @RequestMapping(method  =  RequestMethod.POST, value  =  "/koulutus")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutusOptionsByOpintoalasOrTyyppis(@RequestParam("lang") String lang,
                                @RequestParam(value="opintoala", required = false) String[] opintoalas,
                                @RequestParam(value="tyyppi", required = false) String[] tyyppis) {
        return koodistoService.findKoulutusByOpintoalasOrTyyppis(parseLocale(lang), opintoalas, tyyppis);
    }

    @ApiOperation("Palauttaa opetuskielet.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/opetuskieli")
    @ResponseBody
    public List<UiKoodiItemDto> findOppilaitoksenOpetuskieliOptions(@RequestParam("lang") String lang) {
        return koodistoService.findOppilaitoksenOpetuskieliOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa kielivalikoiman.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/kielivalikoima")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutuksenKieliOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutuksenKieliOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa koulutusasteet.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/koulutusaste")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutusAsteOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutusAsteOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa koulutuksen järjestäjät.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/koulutuksenjarjestaja")
    @ResponseBody
    public List<UiKoodiItemDto> findKoulutuksenJarjestejaOptions(@RequestParam("lang") String lang) {
        return koodistoService.findKoulutuksenJarjestejaOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa avi-koodiston arvot.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/avi")
    @ResponseBody
    public List<UiKoodiItemDto> findAlueHallintoVirastoOptions(@RequestParam("lang") String lang) {
        return koodistoService.findAlueHallintoVirastoOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa käyttöoikeusryhmät haettuna muista poiketen henkilöpalvelun kautta.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/kayttoikeusryhma")
    @ResponseBody
    public List<UiKoodiItemDto> findKayttooikeusryhmas(@RequestParam("lang") String lang) {
        return koodistoService.findKayttooikeusryhmas(parseLocale(lang));
    }

    @ApiOperation("Palauttaa käyttössä olevat tutkintotoimikuntien roolit muista poiketen AITU:n tietojen perusteella.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/tutkintotoimikuntaRoolis")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintotoimikuntaRoolis(@RequestParam("lang") String lang) {
        return koodistoService.findTutkintotoimikuntaRooliOptions(parseLocale(lang));
    }

    @ApiOperation("Palauttaa tutkintotoimikunnat muista poiketen AITU:n tietojen perusteella.")
    @RequestMapping(method  =  RequestMethod.GET, value  =  "/tutkintotoimikuntas")
    @ResponseBody
    public List<UiKoodiItemDto> findTutkintotoimikuntas(@RequestParam("lang") String lang) {
        return koodistoService.findTutkintotoimikuntaOptions(parseLocale(lang));
    }

}