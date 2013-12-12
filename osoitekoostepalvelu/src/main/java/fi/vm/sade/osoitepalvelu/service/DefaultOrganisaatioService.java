package fi.vm.sade.osoitepalvelu.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.osoitepalvelu.reitit.OrganisaatioReitti;
import fi.vm.sade.osoitepalvelu.service.dto.OrganisaatioOid;


/**
 * Service, jonka kautta haetaan organisaatioita.
 * Komponentti, koska transaktionaalisuutta ei tarvita Camel-toteutuksen ansiosta.
 */
@Service
public class DefaultOrganisaatioService implements OrganisaatioService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(DefaultOrganisaatioService.class);
	
	@Autowired
	private OrganisaatioReitti organisaatioReitti;
	
	@Override
	public List<OrganisaatioOid> haeKaikkiOrganisaatioOidit() {
		return organisaatioReitti.haeKaikkiOrganisaatioOidit();
	}
}
