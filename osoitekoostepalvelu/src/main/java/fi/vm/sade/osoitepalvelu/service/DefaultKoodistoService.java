package fi.vm.sade.osoitepalvelu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.osoitepalvelu.reitit.KoodistoReitti;
import fi.vm.sade.osoitepalvelu.service.converter.KoodistoDtoConverter;
import fi.vm.sade.osoitepalvelu.service.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoTila;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoVersioDto;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.service.dto.UiKoodiItemDto;

/**
 * Service, jonka kautta voidaan hakea koodiston eri arvojoukkoja.
 */
@Service
public class DefaultKoodistoService implements KoodistoService {
	
	@Autowired
	private KoodistoReitti koodistoReitti;
	
	@Autowired
	private KoodistoDtoConverter dtoConverter;
	
	@Override
	public List<UiKoodiItemDto> haeOppilaitosTyyppiValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.OPPILAITOSTYYPPI);
	}
	
	@Override
	public List<UiKoodiItemDto> haeOmistajaTyyppiValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.OMISTAJATYYPPI);
	}
	
	@Override
	public List<UiKoodiItemDto> haeVuosiluokkaValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.VUOSILUOKAT);
	}
	
	@Override
	public List<UiKoodiItemDto> haeMaakuntaValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.MAAKUNTA);
	}
	
	@Override
	public List<UiKoodiItemDto> haeKuntaValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.KUNTA);
	}
	
	@Override
	public List<UiKoodiItemDto> haeTutkintoTyyppiValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.TUTKINTOTYYPPI);
	}
	
	@Override
	public List<UiKoodiItemDto> haeTutkintoValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.TUTKINTO);
	}
	
	@Override
	public List<UiKoodiItemDto> haeOppilaitoksenOpetuskieliValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI);
	}

	@Override
	public List<UiKoodiItemDto> haeKoulutuksenKieliValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.KOULUTUS_KIELIVALIKOIMA);
	}

	@Override
	public List<UiKoodiItemDto> haeKoulutusAsteValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.KOULUTUSASTEKELA);
	}

	@Override
	public List<UiKoodiItemDto> haeKoulutuksenJarjestejaValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.KOULUTUSTOIMIJA);
	}

	@Override
	public List<UiKoodiItemDto> haeOpintoAlaValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.OPINTOALAOPH2002);
	}

	@Override
	public List<UiKoodiItemDto> haeAlueHallintoVirastoValinnat(Locale lokaali) {
		return haeKoodistoTyypinPerusteella(lokaali, KoodistoTyyppi.ALUEHALLINTOVIRASTO);
	}
	
	private List<UiKoodiItemDto> haeKoodistoTyypinPerusteella(Locale lokaali, KoodistoTyyppi tyyppi) {
		KoodistoVersioDto koodistoVersio = haeViimeisinVoimassaOlevaKoodistonVersio(tyyppi);
		if (koodistoVersio == null) {
			return new ArrayList<UiKoodiItemDto>();		// Palautetaan tässä tyhjä lista
		}
		List<KoodiDto> arvot = koodistoReitti.haeKooditKoodistonVersiolleTyyppilla(tyyppi, koodistoVersio.getVersio());
		arvot = filteroiAktiivisetKoodit(arvot, new LocalDate());
		List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, lokaali, new ArrayList<UiKoodiItemDto>());
		return jarjestaNimetNousevasti(optiot);
	}
	
	/**
	 * Filtteröi (eli poimii) kaikista koodiston arvoista uusimmat ja aktiiviset versiot.
	 * @param koodit Koodilista, joka filtteröi
	 * @param voimassaPvm Päivämäärä, jonka avulla päätellään, onko koodin arvo voimassa.
	 * @return Suodatettu lista, jossa mukana vain voimassa olevat arvot.
	 */
	private List<KoodiDto> filteroiAktiivisetKoodit(List<KoodiDto> koodit, LocalDate voimassaPvm) {
		HashMap<String, KoodiDto> aktiivisetMap = new HashMap<String, KoodiDto>();
		for (KoodiDto koodi : koodit) {
			if (koodi.isVoimassaPvm(voimassaPvm) && KoodistoTila.isAktiivinenTila(koodi.getTila())) {
				// Päivitetään tässä versiotieto
				String mapKoodiId = koodi.getKoodiArvo();
				KoodiDto aikaisempiVersio = aktiivisetMap.get(mapKoodiId);
				// Onko eka versio tai uudempi versionumero kyseessä?
				if (aikaisempiVersio == null || koodi.getVersio() > aikaisempiVersio.getVersio()) {
					// Tuoreempi versio -> Käytetään sitten tätä uusinta arvoa koodista
					aktiivisetMap.put(mapKoodiId, koodi);
				}
			}
		}
		return new ArrayList<KoodiDto>(aktiivisetMap.values());
	}
	
	/**
	 * Järjestää koodiston arvot nimen perusteella nousevasti.
	 * @param arvot Koodiston arvot, jotka tulisi järjestää.
	 * @return
	 */
	private List<UiKoodiItemDto> jarjestaNimetNousevasti(List<UiKoodiItemDto> arvot) {
		Collections.sort(arvot, new Comparator<UiKoodiItemDto>() {
			@Override
			public int compare(UiKoodiItemDto koodiA, UiKoodiItemDto koodiB) {
				return koodiA.getNimi().compareTo(koodiB.getNimi());
			}
		});
		return arvot;
	}

	@Override
	public Map<KoodistoTyyppi, List<UiKoodiItemDto>> haeKaikkiTuetutKoodistot(Locale lokaali) {
		Map<KoodistoTyyppi, List<UiKoodiItemDto>> koodistotMap = new HashMap<KoodistoTyyppi, List<UiKoodiItemDto>>();
		KoodistoTyyppi[] tuetutKoodistot = KoodistoTyyppi.values();
		for (KoodistoTyyppi tyyppi : tuetutKoodistot) {
			List<UiKoodiItemDto> koodistonArvojoukko = haeKoodistoTyypinPerusteella(lokaali, tyyppi);
			koodistotMap.put(tyyppi, koodistonArvojoukko);
		}
		return koodistotMap;
	}
	
	/**
	 * Hakee viimeisimmän tänään voimassa olevan koodiston versiotiedot.
	 * @param tyyppi Koodiston tyyppi, eli koodiston tunniste.
	 * @return Voimassa oleva koodiston versio tai null, jos tällaista versiota ei löydy.
	 */
	private KoodistoVersioDto haeViimeisinVoimassaOlevaKoodistonVersio(KoodistoTyyppi tyyppi) {
		List<KoodistoVersioDto> versiot = koodistoReitti.haeKoodistonVersiot(tyyppi);
		long maxVersionNumber = -1L;
		KoodistoVersioDto versioVoimassa = null;
		if (versiot != null && versiot.size() > 0) {
			LocalDate tanaan = new LocalDate();
			for (KoodistoVersioDto versio : versiot) {
				// Onko koodi aktiivinen ja tänään voimassa?
				if (versio.isVoimassaPvm(tanaan) && KoodistoTila.isAktiivinenTila(versio.getTila())) {
					// Etsitään maksimia versionumerosta
					if (versio.getVersio() > maxVersionNumber) {
						maxVersionNumber = versio.getVersio();
						versioVoimassa = versio;
					}
				}
			}
		}
		return versioVoimassa;
	}
}
