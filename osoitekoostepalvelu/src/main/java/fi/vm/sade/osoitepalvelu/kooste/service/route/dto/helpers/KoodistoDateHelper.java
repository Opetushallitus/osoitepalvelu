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

package fi.vm.sade.osoitepalvelu.kooste.service.route.dto.helpers;

import org.joda.time.LocalDate;

/**
 * Apuluokka koodistopalvelun päivämäärien käsittelyyn.
 */
public final class KoodistoDateHelper {
    
    private KoodistoDateHelper() {        
    }

    public static boolean isPaivaVoimassaValilla(LocalDate pvm, LocalDate voimassaAlkuPvm, LocalDate voimassaLoppuPvm) {
        if (voimassaAlkuPvm != null && voimassaLoppuPvm != null) {
            // Onko annettu päivämäärä aikavälillä?
            return (pvm.compareTo(voimassaAlkuPvm) >= 0) && (pvm.compareTo(voimassaLoppuPvm) <= 0);
        } else if (voimassaAlkuPvm != null) {
            return (pvm.compareTo(voimassaAlkuPvm) >= 0);
        } else if (voimassaLoppuPvm != null) {
            return (pvm.compareTo(voimassaLoppuPvm) <= 0);
        } else {
            // Aina voimassa, koska rajoja ei ole asetettu
            return true;
        }
    }
}
