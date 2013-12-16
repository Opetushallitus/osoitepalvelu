package fi.vm.sade.osoitepalvelu.kooste.mvc;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.vm.sade.osoitepalvelu.service.KoodistoService;
import fi.vm.sade.osoitepalvelu.service.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoDto.KoodistoTyyppi;

@Controller
@RequestMapping(value = "/koodisto")
public class KoodistoMvcController {
	private static final Locale UI_LOCALE = new Locale("fi", "FI");
	
	@Autowired
	private KoodistoService koodistoService;
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Map<KoodistoTyyppi, List<UiKoodiItemDto>> haeKaikkiValintalistojenArvot() {
		return koodistoService.haeKaikkiTuetutKoodistot(UI_LOCALE);
	}
		
	@RequestMapping(method = RequestMethod.GET, value="/oppilaitostyyppi")
	public @ResponseBody List<UiKoodiItemDto> haeOppilaitosTyyppiValinnat() {
		return koodistoService.haeOppilaitosTyyppiValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/omistajatyyppi")
	public @ResponseBody List<UiKoodiItemDto> haeOmistajaTyyppiValinnat() {
		return koodistoService.haeOmistajaTyyppiValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/vuosiluokka")
	public @ResponseBody List<UiKoodiItemDto> haeVuosiluokkaValinnat() {
		return koodistoService.haeVuosiluokkaValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/maakunta")
	public @ResponseBody List<UiKoodiItemDto> haeMaakuntaValinnat() {
		return koodistoService.haeMaakuntaValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/kunta")
	public @ResponseBody List<UiKoodiItemDto> haeKuntaValinnat() {
		return koodistoService.haeKuntaValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/tutkintotyyppi")
	public @ResponseBody List<UiKoodiItemDto> haeTutkintoTyyppiValinnat() {
		return koodistoService.haeTutkintoTyyppiValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/tutkinto")
	public @ResponseBody List<UiKoodiItemDto> haeTutkintoValinnat() {
		return koodistoService.haeTutkintoValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/opetuskieli")
	public @ResponseBody List<UiKoodiItemDto> haeOppilaitoksenOpetuskieliValinnat() {
		return koodistoService.haeOppilaitoksenOpetuskieliValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/kielivalikoima")
	public @ResponseBody List<UiKoodiItemDto> haeKoulutuksenKieliValinnat() {
		return koodistoService.haeKoulutuksenKieliValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/koulutusaste")
	public @ResponseBody List<UiKoodiItemDto> haeKoulutusAsteValinnat() {
		return koodistoService.haeKoulutusAsteValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/koulutuksenjarjestaja")
	public @ResponseBody List<UiKoodiItemDto> haeKoulutuksenJarjestejaValinnat() {
		return koodistoService.haeKoulutuksenJarjestejaValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/opintoala")
	public @ResponseBody List<UiKoodiItemDto> haeOpintoAlaValinnat() {
		return koodistoService.haeOpintoAlaValinnat(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/avi")
	public @ResponseBody List<UiKoodiItemDto> haeAlueHallintoVirastoValinnat() {
		return koodistoService.haeAlueHallintoVirastoValinnat(UI_LOCALE);
	}	
}
