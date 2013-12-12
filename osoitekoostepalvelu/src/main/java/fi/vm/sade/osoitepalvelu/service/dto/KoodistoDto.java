package fi.vm.sade.osoitepalvelu.service.dto;

import java.util.HashMap;
import java.util.Map;

public class KoodistoDto {
	// Eri koodistojen tuetut kategoriat/tyypit
	public enum KoodistoTyyppi {
		//
		// Muista päivittää uriToTypeMapper -cache, jos lisäät arvoja tähän enumiin
		OPPILAITOSTYYPPI("oppilaitostyyppi"),
		OMISTAJATYYPPI("omistajatyyppi"),
		VUOSILUOKAT("vuosiluokat"),
		MAAKUNTA("maakunta"),
		KUNTA("kunta"),
		TUTKINTOTYYPPI("tutkintotyyppi"),
		TUTKINTO("tutkinto");
		
		private String uri;
		
		// Cache, jolla merkkijonot voidaan nopeasti mapata KoodistoTyypeiksi
		private static Map<String, KoodistoTyyppi> uriToTypeMapper = new HashMap<String, KoodistoTyyppi>() {
			private static final long serialVersionUID = -4218132478973020911L;
			{
				put(OPPILAITOSTYYPPI.getUri(), OPPILAITOSTYYPPI);
				put(OMISTAJATYYPPI.getUri(), OMISTAJATYYPPI);
				put(VUOSILUOKAT.getUri(), VUOSILUOKAT);
				put(MAAKUNTA.getUri(), MAAKUNTA);
				put(KUNTA.getUri(), KUNTA);
				put(TUTKINTOTYYPPI.getUri(), TUTKINTOTYYPPI);
				put(TUTKINTO.getUri(), TUTKINTO);
			}
		};
		
		private KoodistoTyyppi(String uri) {
			this.uri = uri;
		}

		public String getUri() {
			return uri;
		}
		
		public static KoodistoTyyppi parseTyyppi(String koodistoTyyppi) {
			KoodistoTyyppi tyyppi = uriToTypeMapper.get(koodistoTyyppi);
			if (tyyppi == null) {
				throw new IllegalStateException("Virhe: Tuntematon koodistotyyppi: " + koodistoTyyppi);
			}
			return tyyppi;
		}
	
		@Override
		public String toString() {
			return this.getUri();
		}
	}
	
	private KoodistoTyyppi tyyppi;
	private OrganisaatioOid organisaatioOid;
	
	public KoodistoDto() {
	}
	
	public KoodistoDto(String koodistoUri, String organisaatioOid) {
		this.setKoodistoUri(koodistoUri);
		this.setOrganisaatioOid(new OrganisaatioOid(organisaatioOid));
	}
	
	public KoodistoTyyppi getTyyppi() {
		return this.tyyppi;
	}

	public String getKoodistoUri() {
		return tyyppi.getUri();
	}

	public void setKoodistoUri(String koodistoUri) {
		tyyppi = KoodistoTyyppi.parseTyyppi(koodistoUri);
	}

	public OrganisaatioOid getOrganisaatioOid() {
		return organisaatioOid;
	}

	public void setOrganisaatioOid(OrganisaatioOid organisaatioOid) {
		this.organisaatioOid = organisaatioOid;
	}
	
	@Override
	public String toString() {
		return tyyppi.getUri() + ", " + organisaatioOid;
	}
}
