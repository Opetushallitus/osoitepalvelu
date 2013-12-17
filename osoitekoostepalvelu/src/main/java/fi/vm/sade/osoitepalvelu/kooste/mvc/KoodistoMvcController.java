package fi.vm.sade.osoitepalvelu.kooste.mvc;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.vm.sade.osoitepalvelu.kooste.service.kooste.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.UiKoodiItemDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;


@Api("Koodiston valintakriteerit")
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
	public @ResponseBody List<UiKoodiItemDto> findOppilaitosTyyppiOptions() {
		return koodistoService.findOppilaitosTyyppiOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/omistajatyyppi")
	public @ResponseBody List<UiKoodiItemDto> findOmistajaTyyppiOptions() {
		return koodistoService.findOmistajaTyyppiOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/vuosiluokka")
	public @ResponseBody List<UiKoodiItemDto> findVuosiluokkaOptions() {
		return koodistoService.findVuosiluokkaOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/maakunta")
	public @ResponseBody List<UiKoodiItemDto> findMaakuntaOptions() {
		return koodistoService.findMaakuntaOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/kunta")
	public @ResponseBody List<UiKoodiItemDto> findKuntaOptions() {
		return koodistoService.findKuntaOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/tutkintotyyppi")
	public @ResponseBody List<UiKoodiItemDto> findTutkintoTyyppiOptions() {
		return koodistoService.findTutkintoTyyppiOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/tutkinto")
	public @ResponseBody List<UiKoodiItemDto> findTutkintoOptions() {
		return koodistoService.findTutkintoOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/opetuskieli")
	public @ResponseBody List<UiKoodiItemDto> findOppilaitoksenOpetuskieliOptions() {
		return koodistoService.findOppilaitoksenOpetuskieliOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/kielivalikoima")
	public @ResponseBody List<UiKoodiItemDto> findKoulutuksenKieliOptions() {
		return koodistoService.findKoulutuksenKieliOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/koulutusaste")
	public @ResponseBody List<UiKoodiItemDto> findKoulutusAsteOptions() {
		return koodistoService.findKoulutusAsteOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/koulutuksenjarjestaja")
	public @ResponseBody List<UiKoodiItemDto> findKoulutuksenJarjestejaOptions() {
		return koodistoService.findKoulutuksenJarjestejaOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/opintoala")
	public @ResponseBody List<UiKoodiItemDto> findOpintoAlaOptions() {
		return koodistoService.findOpintoAlaOptions(UI_LOCALE);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/avi")
	public @ResponseBody List<UiKoodiItemDto> findAlueHallintoVirastoOptions() {
		return koodistoService.findAlueHallintoVirastoOptions(UI_LOCALE);
	}	
}
