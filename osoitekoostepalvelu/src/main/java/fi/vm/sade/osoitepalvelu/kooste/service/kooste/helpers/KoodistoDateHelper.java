package fi.vm.sade.osoitepalvelu.kooste.service.kooste.helpers;

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
