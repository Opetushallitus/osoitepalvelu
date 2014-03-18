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

package fi.vm.sade.osoitepalvelu.kooste.common.util;

import java.util.Locale;

/**
 * User: ratamaa
 * Date: 3/18/14
 * Time: 4:28 PM
 */
public class LocaleHelper {

    /**
     * @param locale tha language and country code pair, language or kieli_language to parse
     * @param defaultLocale to return if the given locale is null or empty
     * @return the parsed locale or defaultLocale if locale is not defined
     */
    public static Locale parseLocale(String locale, Locale defaultLocale) {
        if (locale == null || locale.trim().length() < 1) {
            return defaultLocale;
        }
        if (locale.startsWith("kieli_")) {
            locale = locale.substring("kieli_".length());
        }
        String[] prts = locale.split("_");
        if (prts.length == 2) {
            return new Locale(prts[0].toLowerCase(), prts[1].toUpperCase());
        }
        prts = locale.split("-");
        if (prts.length == 2) {
            return new Locale(prts[0].toLowerCase(), prts[1].toUpperCase());
        }
        return new Locale(locale.toLowerCase());
    }

}
