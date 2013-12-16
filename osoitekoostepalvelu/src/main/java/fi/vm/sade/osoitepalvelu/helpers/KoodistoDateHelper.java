package fi.vm.sade.osoitepalvelu.helpers;

import org.joda.time.LocalDate;

public class KoodistoDateHelper {

	public static boolean isPaivaVoimassaValilla(LocalDate pvm, LocalDate voimassaAlkuPvm, LocalDate voimassaLoppuPvm) {
		if (voimassaAlkuPvm != null && voimassaLoppuPvm != null) {
			// Onko annettu päivämäärä aikavälillä?
			return (pvm.compareTo(voimassaAlkuPvm)  >= 0) && (pvm.compareTo(voimassaLoppuPvm) <= 0);
		} else if (voimassaAlkuPvm != null) {
			return (pvm.compareTo(voimassaAlkuPvm) >= 0);
		} else if(voimassaLoppuPvm != null) {
			return (pvm.compareTo(voimassaLoppuPvm) <= 0);
		} else {
			// Aina voimassa, koska rajoja ei ole asetettu
			return true;
		}
	}
}
