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

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 12:29 PM
 */
public final class StringHelper {

    private StringHelper() {
    }
    
    /**
     * @param concatBy the concatenator
     * @param values to concatenate by concatBy
     * @return values concatenated by concatBy
     */
    public static String join(String concatBy, String... values) {
        StringBuffer b  =  new StringBuffer();
        for (int i = 0; i < values.length; ++i) {
            String val  =  values[i];
            if (val != null) {
                if (b.length() > 0) {
                    b.append(concatBy);
                }
                b.append(val);
            }
        }
        return b.toString();
    }

    /**
     * @param str
     * @return str in lowercase, null-safe
     */
    public static String lower(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

}
