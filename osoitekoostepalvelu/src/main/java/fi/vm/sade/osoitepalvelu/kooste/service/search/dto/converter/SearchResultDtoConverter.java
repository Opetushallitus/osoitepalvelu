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

import fi.vm.sade.osoitepalvelu.kooste.common.dtoconverter.AbstractDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloYhteystietoRyhmaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteysosoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.HenkiloOsoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OsoitteistoDto;
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
        to.setOsoite(from.findArvo(HenkiloYhteystietoDto.YHTEYSTIETO_KATUOSOITE));
        to.setPostinumero(from.findArvo(HenkiloYhteystietoDto.YHTEYSTIETO_POSTINUMERO));
        to.setPostitoimipaikka(from.findArvo(HenkiloYhteystietoDto.YHTEYSTIETO_KAUPUNKI));
        to.setHenkiloEmail(from.findArvo(HenkiloYhteystietoDto.YHTESYTIETO_SAHKOPOSTI));
        to.setPuhelinnumero(from.findArvo(HenkiloYhteystietoDto.YHTEYSTIETO_PUHELINNUMERO));

        return to;
    }

}
