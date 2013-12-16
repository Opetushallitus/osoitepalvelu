package fi.vm.sade.osoitepalvelu.reitit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.osoitepalvelu.service.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoDto.KoodistoTyyppi;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoVersioDto;

/**
 * Koodisto-palvelun Camel-reittien toteutus.
 */
@Component
public class KoodistoReitti extends AbstractJsonToDtoRouteBuilder {
	
	private static final String REITTI_HAE_KOODISTON_KOODIT = "direct:haeKoodistonKoodit";
	private static final String REITTI_HAE_KOODISTO_VERSION_KOODIT = "direct:haeKoodistoVersionKoodit";
	private static final String REITTI_HAE_KOODISTON_VERSIOT = "direct:haeKoodistonVersiot"; 

	@Value("${valintalaskentakoostepalvelu.koodiService.rest.url}")
	private String koodistoUri;
	
	@Override
	public void configure() throws Exception {
		koodistoUri = trim(koodistoUri);
		
		// Reitti, joka hakee tietyn koodiston koodit
		TypeReference<List<KoodiDto>> dtoType = new TypeReference<List<KoodiDto>>(){};
		fromHttpGetToDtos(REITTI_HAE_KOODISTON_KOODIT, koodistoUri,
				Exchange.HTTP_PATH, simple("${in.headers.koodistoTyyppi}/koodi"),
				dtoType);
		
		// Seuraava reitti hakee tietyn koodistoversion kaikki koodit
		HeaderBuilder headers = new HeaderBuilder();
		headers.add(Exchange.HTTP_PATH, simple("${in.headers.koodistoTyyppi}/koodi"));
		headers.add(Exchange.HTTP_QUERY, simple("koodistoVersio=${in.headers.koodistoVersio}"));		
		fromHttpGetToDtos(REITTI_HAE_KOODISTO_VERSION_KOODIT, koodistoUri, headers, dtoType);
		
		// Reitti, joka hakee koodiston versiotiedot
		TypeReference<List<KoodistoVersioDto>> versioDtoType = new TypeReference<List<KoodistoVersioDto>>(){};
		fromHttpGetToDtos(REITTI_HAE_KOODISTON_VERSIOT, koodistoUri,
				Exchange.HTTP_PATH, simple("${in.headers.koodistoTyyppi}"),
				versioDtoType);
	}
	
	/**
	 * Hakee tietyn koodiston kaikki koodit annetun koodistotyypin perusteella.
	 * @param koodistoTyyppi Haettavan koodiston tyyppi.
	 * @return Lista koodeista, jotka kuuluvat valittuun koodistoon.
	 */
	public List<KoodiDto> haeKooditKoodistonTyyppilla(KoodistoTyyppi koodistoTyyppi) {
		@SuppressWarnings("unchecked")
		List<KoodiDto> koodit =
				getCamelTemplate().requestBodyAndHeader(REITTI_HAE_KOODISTON_KOODIT, "",
						"koodistoTyyppi", koodistoTyyppi.getUri(), List.class);
		return koodit;
	}
	
	public List<KoodiDto> haeKooditKoodistonVersiolleTyyppilla(KoodistoTyyppi koodistoTyyppi, long versio) {
		// Asetataan tarvittavat parametrit
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("koodistoTyyppi", koodistoTyyppi.getUri());
		parameters.put("koodistoVersio", "" + versio);
		@SuppressWarnings("unchecked")
		List<KoodiDto> koodit =
				getCamelTemplate().requestBodyAndHeaders(REITTI_HAE_KOODISTO_VERSION_KOODIT, "",
						parameters, List.class);
		return koodit;
	}
	
	/**
	 * Hakee tietyn koodin versiot.
	 * 
	 * @param koodistoTyyppi
	 * @return
	 */
	public List<KoodistoVersioDto> haeKoodistonVersiot(KoodistoTyyppi koodistoTyyppi) {
		@SuppressWarnings("unchecked")
		List<KoodistoVersioDto> versiot =
				getCamelTemplate().requestBodyAndHeader(REITTI_HAE_KOODISTON_VERSIOT, "",
						"koodistoTyyppi", koodistoTyyppi.getUri(), List.class);
		return versiot;
	}
}
