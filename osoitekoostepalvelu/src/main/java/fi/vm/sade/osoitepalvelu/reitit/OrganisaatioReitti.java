package fi.vm.sade.osoitepalvelu.reitit;

import java.util.List;

import org.apache.camel.Exchange;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.osoitepalvelu.service.DefaultOrganisaatioService;
import fi.vm.sade.osoitepalvelu.service.dto.OrganisaatioDto;
import fi.vm.sade.osoitepalvelu.service.dto.OrganisaatioOid;

/**
 * Organisaatiopalvelun Camel-reittien toteutus.
 */
@Component
public class OrganisaatioReitti extends AbstractJsonToDtoRouteBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultOrganisaatioService.class);

	public static final String REITI_HAE_ORGANISAATIO_OIDT = "direct:haeOrganisaatioOidt";
	public static final String RETTI_HAE_ORGANISAATIO = "direct:haeOrganisaatio";
	
	// Organisaatiopalvelun REST -rajapinnan URL, luetaan .properties tiedostosta
	@Value("${valintalaskentakoostepalvelu.organisaatioService.rest.url}")
	private String dataProviderUrl;
	
	/**
	 * Organisaatio palvelun Camel -query reittien konfigurointi. 
	 */
	@Override
	public void configure() throws Exception {		
		dataProviderUrl = trim(dataProviderUrl);
		logger.debug("-- Organisaatio palvelun query URL: " + dataProviderUrl);		
		
		TypeReference<List<OrganisaatioOid>> oidsDtoType = new TypeReference<List<OrganisaatioOid>>(){};
		fromHttpGetToDtos(REITI_HAE_ORGANISAATIO_OIDT, dataProviderUrl, oidsDtoType);
		
		TypeReference<OrganisaatioDto> orgDtoType = new TypeReference<OrganisaatioDto>() {};
		fromHttpGetToDtos(RETTI_HAE_ORGANISAATIO, dataProviderUrl,
				Exchange.HTTP_PATH, simple("${in.headers.oid}"),
				orgDtoType);
	}	
	
	/**
	 * @return Palauttaa kaikkien organisaatioiden oidt.
	 */
	public List<OrganisaatioOid> haeKaikkiOrganisaatioOidit() {
		@SuppressWarnings("unchecked")
		List<OrganisaatioOid> oids =
			getCamelTemplate().requestBody(OrganisaatioReitti.REITI_HAE_ORGANISAATIO_OIDT, "", List.class);
		return oids;
	}
	
	/**
	 * @param oid Haettavan organisaation yksikäsitteiden oid.
	 * @return Organisaation tiedot OrganisaatioDto:ssa.
	 */
	public OrganisaatioDto haeOrganisaatioOidilla(OrganisaatioOid oid) {
		// Organisaation tunniste annetaan tässä header parametrina "oid"
		OrganisaatioDto org = getCamelTemplate().requestBodyAndHeader(
				OrganisaatioReitti.RETTI_HAE_ORGANISAATIO, "", "oid", oid.getValue(),
				OrganisaatioDto.class);
		return org;
	}
}
