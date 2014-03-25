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
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteysosoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioTiedotDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OsoitteistoDto;
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

    public OrganisaatioTiedotDto convert(OrganisaatioYhteystietoHakuResultDto from, OrganisaatioTiedotDto to,
                                         Locale locale) {
        convertValue(from, to, locale);
        if (from.getKotipaikka() != null) {
            UiKoodiItemDto kuntaKoodi  =  koodistoService
                    .findKuntaByKoodiUri(locale, from.getKotipaikka());
            to.setKotikunta(kuntaKoodi.getNimi());
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

}
