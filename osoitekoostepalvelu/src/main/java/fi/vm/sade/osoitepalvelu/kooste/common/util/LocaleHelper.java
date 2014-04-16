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
import java.util.Map;

/**
 * User: ratamaa
 * Date: 3/18/14
 * Time: 4:28 PM
 */
public final class LocaleHelper {

    
    private LocaleHelper() {
    }
    
    /**
     * @param locale tha language and country code pair, language or kieli_language(#versio) to parse language from,
     *               may be null
     * @param defaultLocale to return if the given locale is null or empty, may be null
     * @return the parsed locale or defaultLocale if locale is not defined (may be null)
     */
    public static Locale parseLocale(String locale, Locale defaultLocale) {
        if (locale == null || locale.trim().length() < 1) {
            return defaultLocale;
        }
        if (locale.startsWith("kieli_")) {
            locale  =  locale.substring("kieli_".length());
        }
        String[] stripPrts = locale.split("#");
        if (stripPrts.length > 1) {
            locale  =  stripPrts[0];
        }
        String[] prts = locale.split("_");
        if (prts.length == 2) {
            return new Locale(prts[0].toLowerCase(), prts[1].toUpperCase());
        }
        prts  =  locale.split("-");
        if (prts.length == 2) {
            return new Locale(prts[0].toLowerCase(), prts[1].toUpperCase());
        }
        return new Locale(locale.toLowerCase());
    }

    /**
     * @param locale1 locale language part of which to compare against locale2
     * @param locale2 locale language part of which to compare against locale1
     * @return true if both locales are null or if their language code equals (case insensitive), null-safe
     */
    public static boolean languageEquals(Locale locale1, Locale locale2) {
        if (locale1 == null && locale2 == null) {
            return true;
        }
        if (locale1 == null || locale2 == null) {
            return false;
        }
        return EqualsHelper.equals(StringHelper.lower(locale1.getLanguage()),
                                StringHelper.lower(locale2.getLanguage()));
    }

    /**
     * @param name to localize
     * @param preferredLocale
     * @param defaultLocale to fallback if no match for preferredLocale
     * @return the localized String
     */
    public static String findLocalized(Map<String, String> name, Locale preferredLocale, Locale defaultLocale) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        if (preferredLocale != null) {
            if (name.containsKey(preferredLocale.toString())) {
                return name.get(preferredLocale.toString());
            }
            if (name.containsKey(preferredLocale.getLanguage())) {
                return name.get(preferredLocale.getLanguage());
            }
        }
        if (!EqualsHelper.equals(preferredLocale, defaultLocale)) {
            return findLocalized(name, defaultLocale, defaultLocale);
        }
        if (!name.isEmpty()) {
            // Return some name over nothing:
            return name.values().iterator().next();
        }
        return null;
    }
}
