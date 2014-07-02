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

package fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter;

import fi.ratamaa.dtoconverter.types.TypeResolver;
import fi.vm.sade.osoitepalvelu.kooste.common.dtoconverter.AbstractDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloYhteystietoRyhmaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteysosoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:53 PM
 */
@Component
public class SearchResultDtoConverter extends AbstractDtoConverter {
    @Autowired
    private KoodistoService koodistoService;

    @Override
    protected void registerTypes(TypeResolver typeResolver) {
        typeResolver.registerType("henkiloAggregate", HenkiloResultAggregateDto.class)
                    .registerType("organisaatioAggregate", OrganisaatioResultAggregateDto.class)
                    .registerType("toimikuntaJasenAggregate", AituToimikuntaJasenAggregateDto.class);
    }

    public SearchResultRowDto convert(AituToimikuntaJasenAggregateDto from, SearchResultRowDto to, Locale locale) {
        if (from.getJasen() != null) {
            to.setPostiosoite(new SearchResultOsoiteDto());
            to.getPostiosoite().setOsoite(from.getJasen().getOsoite());
            to.getPostiosoite().setPostinumero(from.getJasen().getPostinumero());
            to.getPostiosoite().setPostitoimipaikka(from.getJasen().getPostitoimipaikka());
            to.setNimike(from.getJasen().getEdustus());
            to.setYhteystietoNimi(from.getJasen().getKokoNimi());
            to.setHenkiloEmail(from.getJasen().getSahkoposti());
        }
        if (from.getToimikunta() != null) {
            to.setNimi(LocaleHelper.findLocalized(from.getToimikunta().getNimi(), locale, DefaultKoodistoService.DEFAULT_LOCALE));
        }
        return to;
    }

    public OrganisaatioResultDto convert(OrganisaatioYhteystietoHakuResultDto from, OrganisaatioResultDto to,
                                         Locale locale) {
        convertValue(from, to, locale);
        if (from.getKotipaikka() != null) {
            UiKoodiItemDto kuntaKoodi  =  koodistoService
                    .findKuntaByKoodiUri(locale, from.getKotipaikka());
            if (kuntaKoodi != null) {
                to.setKotikunta(kuntaKoodi.getNimi());
            }
        }
        return to;
    }

    public OsoitteistoDto convert(OrganisaatioYhteysosoiteDto from, OsoitteistoDto to, Locale locale) {
        convertValue(from, to, locale);
        if (from.getPostinumero() != null) {
            UiKoodiItemDto postinumeroKoodi  =  koodistoService
                .findPostinumeroByKoodiUri(locale, from.getPostinumero());
            if (postinumeroKoodi != null) {
                to.setPostinumero(postinumeroKoodi.getKoodiId());
                to.setPostitoimipaikka(postinumeroKoodi.getNimi());
            }
        }
        return to;
    }

    public HenkiloOsoiteDto convert(HenkiloYhteystietoRyhmaDto from, HenkiloOsoiteDto to, Locale locale) {
        to.setId(from.getId());
        to.setOsoite(from.findArvo(HenkiloYhteystietoDto.YHTEYSTIETO_KATUOSOITE));
        to.setPostinumero(from.findArvo(HenkiloYhteystietoDto.YHTEYSTIETO_POSTINUMERO));
        to.setPostitoimipaikka(from.findArvo(HenkiloYhteystietoDto.YHTEYSTIETO_KAUPUNKI));
        to.setHenkiloEmail(from.findArvo(HenkiloYhteystietoDto.YHTESYTIETO_SAHKOPOSTI));
        to.setPuhelinnumero(from.findArvo(HenkiloYhteystietoDto.YHTEYSTIETO_PUHELINNUMERO));

        return to;
    }

}
