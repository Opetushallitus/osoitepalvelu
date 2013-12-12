package fi.vm.sade.osoitepalvelu.service.dto;

import java.util.ArrayList;
import java.util.List;

public class KoodiDto {
	private String koodiUri;
	private long versio;
	private String koodiArvo;
	// Koodiston tyyppi
	private KoodistoDto koodisto;	
	// Todellinen koodin data: Sisältää useita arvoja eri lokaaleille
	private List<KoodiArvoDto> metadata = new ArrayList<KoodiArvoDto>();

	public String getKoodiUri() {
		return koodiUri;
	}

	public void setKoodiUri(String koodiUri) {
		this.koodiUri = koodiUri;
	}

	public long getVersio() {
		return versio;
	}

	public void setVersio(long versio) {
		this.versio = versio;
	}

	public String getKoodiArvo() {
		return koodiArvo;
	}

	public void setKoodiArvo(String koodiArvo) {
		this.koodiArvo = koodiArvo;
	}

	public List<KoodiArvoDto> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<KoodiArvoDto> metadata) {
		this.metadata = metadata;
	}

	public KoodistoDto getKoodisto() {
		return koodisto;
	}

	public void setKoodisto(KoodistoDto koodisto) {
		this.koodisto = koodisto;
	}
	
	public KoodiArvoDto getArvoByKieli(String kieli) {
		for (KoodiArvoDto arvo : metadata) {
			if (arvo.getKieli().equalsIgnoreCase(kieli)) {
				return arvo;
			}
		}
		return null;
	}
}
