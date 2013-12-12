package fi.vm.sade.osoitepalvelu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.osoitepalvelu.reitit.KoodistoReitti;
import fi.vm.sade.osoitepalvelu.service.converter.KoodistoDtoConverter;
import fi.vm.sade.osoitepalvelu.service.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.service.dto.UiKoodiItemDto;

/**
 * Service, jonka kautta voidaan hakea koodiston eri arvojoukkoja.
 * Komponentti, koska transaktionaalisuutta ei tarvita Camel-toteutuksen ansiosta.
 */
@Component
public class KoodistoService {
	
	@Autowired
	private KoodistoReitti koodistoReitti;
	
	@Autowired
	private KoodistoDtoConverter dtoConverter;
	
	
	public List<UiKoodiItemDto> haeOppilaitosTyyppiValinnat(Locale lokaali) {
		List<KoodiDto> arvot = koodistoReitti.haeKooditKoodistonTyyppilla(KoodistoTyyppi.OPPILAITOSTYYPPI);
		List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, lokaali, new ArrayList<UiKoodiItemDto>());
		return jarjestaNimetNousevasti(optiot);
	}
	
	public List<UiKoodiItemDto> haeOmistajaTyyppiValinnat(Locale lokaali) {
		List<KoodiDto> arvot = koodistoReitti.haeKooditKoodistonTyyppilla(KoodistoTyyppi.OMISTAJATYYPPI);
		List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, lokaali, new ArrayList<UiKoodiItemDto>());
		return jarjestaNimetNousevasti(optiot);
	}
	
	public List<UiKoodiItemDto> haeVuosiluokkaValinnat(Locale lokaali) {
		List<KoodiDto> arvot = koodistoReitti.haeKooditKoodistonTyyppilla(KoodistoTyyppi.VUOSILUOKAT);
		List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, lokaali, new ArrayList<UiKoodiItemDto>());
		return jarjestaNimetNousevasti(optiot);
	}
	
	public List<UiKoodiItemDto> haeMaakuntaValinnat(Locale lokaali) {
		List<KoodiDto> arvot = koodistoReitti.haeKooditKoodistonTyyppilla(KoodistoTyyppi.MAAKUNTA);
		List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, lokaali, new ArrayList<UiKoodiItemDto>());
		return jarjestaNimetNousevasti(optiot);
	}
	
	public List<UiKoodiItemDto> haeKuntaValinnat(Locale lokaali) {
		List<KoodiDto> arvot = koodistoReitti.haeKooditKoodistonTyyppilla(KoodistoTyyppi.KUNTA);
		List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, lokaali, new ArrayList<UiKoodiItemDto>());
		return jarjestaNimetNousevasti(optiot);
	}
	
	public List<UiKoodiItemDto> haeTutkintoTyyppiValinnat(Locale lokaali) {
		List<KoodiDto> arvot = koodistoReitti.haeKooditKoodistonTyyppilla(KoodistoTyyppi.TUTKINTOTYYPPI);
		List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, lokaali, new ArrayList<UiKoodiItemDto>());
		return jarjestaNimetNousevasti(optiot);
	}
	
	public List<UiKoodiItemDto> haeTutkintoValinnat(Locale lokaali) {
		List<KoodiDto> arvot = koodistoReitti.haeKooditKoodistonTyyppilla(KoodistoTyyppi.TUTKINTO);
		List<UiKoodiItemDto> optiot = dtoConverter.convert(arvot, lokaali, new ArrayList<UiKoodiItemDto>());
		return jarjestaNimetNousevasti(optiot);
	}
	
	private List<UiKoodiItemDto> jarjestaNimetNousevasti(List<UiKoodiItemDto> arvot) {
		Collections.sort(arvot, new Comparator<UiKoodiItemDto>() {
			@Override
			public int compare(UiKoodiItemDto koodiA, UiKoodiItemDto koodiB) {
				return koodiA.getNimi().compareTo(koodiB.getNimi());
			}
		});
		return arvot;
	}
}
