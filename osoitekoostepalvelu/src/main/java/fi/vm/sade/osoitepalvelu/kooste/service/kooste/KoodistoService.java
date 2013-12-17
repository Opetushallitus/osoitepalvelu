package fi.vm.sade.osoitepalvelu.kooste.service.kooste;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.UiKoodiItemDto;

/**
 * Service -rajapinta, jonka kautta pystyy hakemaan koodiston arvoja koodiston
 * tyypin perusteella. Oletusarvoisesti tämä rajapinta hakee kaikista koodeista
 * viimeisimmän voimassa olevan version.
 */
public interface KoodistoService {

	List<UiKoodiItemDto> findOppilaitosTyyppiOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findOmistajaTyyppiOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findVuosiluokkaOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findMaakuntaOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findKuntaOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findTutkintoTyyppiOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findTutkintoOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findOppilaitoksenOpetuskieliOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findKoulutuksenKieliOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findKoulutusAsteOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findKoulutuksenJarjestejaOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findOpintoAlaOptions(Locale lokaali);
	
	List<UiKoodiItemDto> findAlueHallintoVirastoOptions(Locale lokaali);
	
	/**
	 * Hakee kaikkien koodistojen arvot yhdellä kertaa ja paulattaa näistä mapin.
	 * Mapista halutun koodiston voi hakea KoodistoTyyppi enumin arvon
	 * perustella. Huom: Metodi suorittaa useita kutsuja ulkoiseen koodistopalveluun.
	 * 
	 * @param lokaali Koodistojen arvojen nimien ja kuvauksen lokalisoinnin lokaali.
	 * @return Mappi, joka sisältää kaikkien tuettujen koodistojen arvot.
	 */
	Map<KoodistoTyyppi, List<UiKoodiItemDto>> haeKaikkiTuetutKoodistot(Locale lokaali);
}
