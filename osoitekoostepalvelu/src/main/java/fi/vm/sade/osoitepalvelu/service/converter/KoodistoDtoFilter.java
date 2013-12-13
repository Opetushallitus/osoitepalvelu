package fi.vm.sade.osoitepalvelu.service.converter;

import org.joda.time.LocalDate;

public class KoodistoDtoFilter {
	// Voimassa olon rajoitusehto
	private LocalDate voimassaDate = new LocalDate();

	public LocalDate getVoimassaDate() {
		return voimassaDate;
	}

	public void setVoimassaDate(LocalDate voimassaDate) {
		this.voimassaDate = voimassaDate;
	}
}
