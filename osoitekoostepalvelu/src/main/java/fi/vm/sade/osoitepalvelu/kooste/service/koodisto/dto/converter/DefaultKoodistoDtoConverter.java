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

package fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.converter;

import java.util.List;
import java.util.Locale;

import fi.vm.sade.osoitepalvelu.kooste.common.dtoconverter.AbstractDtoConverter;
import org.springframework.stereotype.Component;

import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodiArvoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;

@Component
public class DefaultKoodistoDtoConverter extends AbstractDtoConverter implements KoodistoDtoConverter {

    public UiKoodiItemDto convert(KoodiDto koodi, UiKoodiItemDto uiKoodi, Locale lokaali) {
        uiKoodi.setKoodiId(koodi.getKoodiArvo());
        uiKoodi.setKoodistonTyyppi(koodi.getKoodisto().getTyyppi());

        KoodiArvoDto arvo = koodi.getArvoByKieli(lokaali.getLanguage());
        if (arvo != null) {
            uiKoodi.setNimi(arvo.getNimi());
            uiKoodi.setKuvaus(arvo.getKuvaus());
            uiKoodi.setLyhytNimi(arvo.getLyhytNimi());
        } else {
            uiKoodi.setNimi(lokaali.getLanguage() + "_missing!");
            uiKoodi.setLyhytNimi(lokaali.getLanguage() + "_missing!");
        }
        return uiKoodi;
    }

    public List<UiKoodiItemDto> convert(List<KoodiDto> koodit, List<UiKoodiItemDto> uiKoodit, Locale lokaali) {
        for (KoodiDto koodi : koodit) {
            UiKoodiItemDto optio = convert(koodi, new UiKoodiItemDto(), lokaali);
            uiKoodit.add(optio);
        }
        return uiKoodit;
    }
}
