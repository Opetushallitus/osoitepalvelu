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

import fi.ratamaa.dtoconverter.DtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;

import java.util.Locale;

/**
 * User: ratamaa
 * Date: 12/17/13
 * Time: 1:50 PM
 */
public interface KoodistoDtoConverter extends DtoConverter {

    UiKoodiItemDto convert(KoodiDto koodi, UiKoodiItemDto uiKoodi, Locale lokaali);

}
