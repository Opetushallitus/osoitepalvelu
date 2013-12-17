package fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto;

import org.joda.time.LocalDate;

import fi.vm.sade.osoitepalvelu.kooste.service.kooste.helpers.KoodistoDateHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;

/**
 * Koodiston versiotietojen mallintaminen.
 */
public class KoodistoVersioDto {
	private String koodistoUri;
	private KoodistoTyyppi koodistoTyyppi;
	private long versio;
	private LocalDate voimassaAlkuPvm;
	private LocalDate voimassaLoppuPvm;
	
	private KoodistoTila tila;

	public String getKoodistoUri() {
		return koodistoUri;
	}

	public void setKoodistoUri(String koodistoUri) {
		this.koodistoUri = koodistoUri;
		if (koodistoUri != null) {
			this.koodistoTyyppi = KoodistoTyyppi.parseTyyppi(koodistoUri);
		}
	}

	public KoodistoTyyppi getKoodistoTyyppi() {
		return koodistoTyyppi;
	}

	public void setKoodistoTyyppi(KoodistoTyyppi koodistoTyyppi) {
		this.koodistoTyyppi = koodistoTyyppi;
	}

	public long getVersio() {
		return versio;
	}

	public void setVersio(long versio) {
		this.versio = versio;
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

	public KoodistoTila getTila() {
		return tila;
	}

	public void setTila(KoodistoTila tila) {
		this.tila = tila;
	}
	
	public boolean isVoimassaPvm(LocalDate pvm) {
		return KoodistoDateHelper.isPaivaVoimassaValilla(pvm, voimassaAlkuPvm, voimassaLoppuPvm);
	}
}
