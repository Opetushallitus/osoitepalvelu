package fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto;

import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;

public class UiKoodiItemDto {
	private KoodistoTyyppi koodistonTyyppi;			// Kategoria
	private String koodiId; 						// koodiArvo koodistossa

	private String nimi;
	private String kuvaus;
	private String lyhytNimi;

	public KoodistoTyyppi getKoodistonTyyppi() {
		return koodistonTyyppi;
	}

	public void setKoodistonTyyppi(KoodistoTyyppi koodistonTyyppi) {
		this.koodistonTyyppi = koodistonTyyppi;
	}

	public String getKoodiId() {
		return koodiId;
	}

	public void setKoodiId(String koodiId) {
		this.koodiId = koodiId;
	}

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
	
	public String toString() {
		return this.koodiId + ": " + this.nimi;
	}
}
