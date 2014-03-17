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

import fi.ratamaa.dtoconverter.ConversionCall;
import fi.ratamaa.dtoconverter.reflection.PropertyConversionContext;
import fi.ratamaa.dtoconverter.typeconverter.TypeConversionContainer;
import fi.ratamaa.dtoconverter.typeconverter.TypeConverterAdapter;
import fi.vm.sade.osoitepalvelu.kooste.common.dtoconverter.AbstractDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.DefaultKoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.*;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.*;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DefaultKoodistoDtoConverter extends AbstractDtoConverter implements KoodistoDtoConverter {

    @Override
    protected void registerConverters(TypeConversionContainer conversions) {
        super.registerConverters(conversions);
        conversions.add(LocalizedContainerDto.class, String.class, new TypeConverterAdapter<LocalizedContainerDto, String>() {
            @Override
            public String convert(LocalizedContainerDto obj, PropertyConversionContext context, String currentValue, ConversionCall call) {
                Locale locale = parameterOfType(Locale.class, 0, call);
                return obj.findForLocale(locale == null ? DefaultKoodistoService.DEFAULT_LOCALE
                        : DefaultKoodistoService.DEFAULT_LOCALE);
            }
        });
    }

    public UiKoodiItemDto convert(KayttooikesuryhmaDto from, UiKoodiItemDto to, Locale locale) {
        convertValue(from, to, locale);
        to.setKoodiUri(to.getKoodiId());
        to.setKoodistonTyyppi(KoodistoDto.KoodistoTyyppi.KAYTTOOIKEUSRYHMA);
        to.setLyhytNimi(to.getNimi());
        return to;
    }

    public UiKoodiItemDto convert(KoodiDto koodi, UiKoodiItemDto uiKoodi, Locale lokaali) {
        convertValue(koodi, uiKoodi, lokaali);
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
}
