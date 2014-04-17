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

import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 4/17/14
 * Time: 2:19 PM
 */
public final class KoodiHelper {

    private KoodiHelper() {
    }

    public static List<String> parseKoodiArvos(String uriPrefix, List<String> koodis) {
        List<String> arvos = new ArrayList<String>();
        for (String koodi : koodis) {
            String arvo = parseKoodiArvo(uriPrefix, koodi);
            if (arvo != null) {
                arvos.add(arvo);
            }
        }
        return arvos;
    }

    public static String parseKoodiArvo(String uriPrefix, String koodi) {
        if (koodi == null) {
            return null;
        }
        if (!koodi.startsWith(uriPrefix)) {
            return koodi;
        }
        if (!uriPrefix.endsWith("_")) {
            uriPrefix = uriPrefix+"_";
        }
        return koodi.replaceAll(uriPrefix+"(.*?)"+"(#.*)?", "$1");
    }
}
