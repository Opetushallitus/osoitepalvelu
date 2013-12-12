package fi.vm.sade.osoitepalvelu.service.dto;

import org.joda.time.LocalDate;

public class KoodiArvoDto {
	private String nimi;
	private String kuvaus;
	private String lyhytNimi;
	private String kieli;
	private LocalDate voimassaAlkuPvm;
	private LocalDate voimassaLoppuPvm;

	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public String getKuvaus() {
		return kuvaus;
	}

	public void setKuvaus(String kuvaus) {
		this.kuvaus = kuvaus;
	}

	public String getLyhytNimi() {
		return lyhytNimi;
	}

	public void setLyhytNimi(String lyhytNimi) {
		this.lyhytNimi = lyhytNimi;
	}

	public String getKieli() {
		return kieli;
	}

	public void setKieli(String kieli) {
		this.kieli = kieli;
	}

	public LocalDate getVoimassaAlkuPvm() {
		return voimassaAlkuPvm;
	}

	public void setVoimassaAlkuPvm(LocalDate voimassaAlkuPvm) {
		this.voimassaAlkuPvm = voimassaAlkuPvm;
	}

	public LocalDate getVoimassaLoppuPvm() {
		return voimassaLoppuPvm;
	}

	public void setVoimassaLoppuPvm(LocalDate voimassaLoppuPvm) {
		this.voimassaLoppuPvm = voimassaLoppuPvm;
	}
	
	@Override
	public String toString() {
		return this.nimi;
	}
}
