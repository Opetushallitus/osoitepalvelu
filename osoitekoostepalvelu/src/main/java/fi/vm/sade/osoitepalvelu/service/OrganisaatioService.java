package fi.vm.sade.osoitepalvelu.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.osoitepalvelu.reitit.OrganisaatioReitti;
import fi.vm.sade.osoitepalvelu.service.dto.OrganisaatioOid;


/**
 * Service, jonka kautta haetaan organisaatioita.
 * Komponentti, koska transaktionaalisuutta ei tarvita Camel-toteutuksen ansiosta.
 */
@Component
public class OrganisaatioService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(OrganisaatioService.class);
	
	@Autowired
	private OrganisaatioReitti organisaatioReitti;
	
	public List<OrganisaatioOid> findAllOrganizationIds() {
		return organisaatioReitti.haeKaikkiOrganisaatioOidit();
	}
}
