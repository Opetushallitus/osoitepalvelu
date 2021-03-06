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

package fi.vm.sade.osoitepalvelu.kooste.service.saves.dto.converter;

import fi.ratamaa.dtoconverter.codebuilding.CodeBuilder;
import fi.vm.sade.osoitepalvelu.kooste.common.dtoconverter.AbstractDtoConverter;
import org.springframework.stereotype.Component;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:08 PM
 */
@Component
public class DefaultSavedSearchDtoConverter extends AbstractDtoConverter implements SavedSearchDtoConverter {

    private static final long serialVersionUID = 6713503958881803346L;

    @Override
    protected CodeBuilder createCodeBuilder() {
        // NullPointerException at fi.ratamaa.dtoconverter.codebuilding.ReadableType$Method.getCode(ReadableType.java:384)
        // randomly occuring on first run with SavedSearch -> SavedSearchListDto conversion, => use reflection
        return null;
    }
}
