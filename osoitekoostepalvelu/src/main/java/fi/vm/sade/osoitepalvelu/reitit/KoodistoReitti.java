package fi.vm.sade.osoitepalvelu.reitit;

import java.util.List;

import org.apache.camel.Exchange;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.osoitepalvelu.service.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.service.dto.KoodistoDto.KoodistoTyyppi;

/**
 * Koodisto-palvelun Camel-reittien toteutus.
 */
@Component
public class KoodistoReitti extends AbstractJsonToDtoRouteBuilder {
	
	private static final String REITTI_HAE_KOODISTON_KOODIT = "direct:haeKoodistonKoodit";

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
}
