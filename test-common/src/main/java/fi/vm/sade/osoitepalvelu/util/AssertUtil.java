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

package fi.vm.sade.osoitepalvelu.util;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: ratamaa
 * Date: 8/5/14
 * Time: 4:42 PM
 */
public final class AssertUtil {
    private AssertUtil() {
    }

    public static <T> void assertListNotEmpty(List<T> arvot, String arvojoukonNimi) {
        assertNotNull(arvot);
        assertTrue("Virhe: Lista '"  +  arvojoukonNimi  +  "' ei saa olla tyhjä!", arvot.size() > 0);
    }
}
