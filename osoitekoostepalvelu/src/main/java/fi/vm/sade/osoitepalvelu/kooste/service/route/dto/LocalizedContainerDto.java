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

package fi.vm.sade.osoitepalvelu.kooste.service.route.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: ratamaa
 * Date: 3/11/14
 * Time: 3:05 PM
 */
public class LocalizedContainerDto implements Serializable {
    private List<LocalizedValueDto> texts = new ArrayList<LocalizedValueDto>();

    public List<LocalizedValueDto> getTexts() {
        return texts;
    }

    public void setTexts(List<LocalizedValueDto> texts) {
        this.texts = texts;
    }

    public String findForLocale(Locale locale) {
        return findForLocale(locale, null);
    }

    public String findForLocale(Locale locale, Locale fallbackTo) {
        String fallbackValue = null;
        for( LocalizedValueDto value : texts ) {
            if( value.getText() != null && value.getLang() != null ) {
                if( value.getLang().toLowerCase().equals(locale.getLanguage().toLowerCase()) ) {
                    return value.getText();
                } else if( fallbackTo != null && value.getLang().toLowerCase().equals(fallbackTo.getLanguage().toLowerCase()) ) {
                    fallbackValue = value.getText();
                } else if( fallbackValue == null ) {
                    fallbackValue = value.getText(); // Take the first one existing if fallbackTo is not defined or found
                }
            }
        }
        return fallbackValue;
    }
}
