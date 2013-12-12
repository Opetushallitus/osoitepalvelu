package fi.vm.sade.osoitepalvelu.service;

import java.util.List;

import fi.vm.sade.osoitepalvelu.service.dto.OrganisaatioOid;

public interface OrganisaatioService {

	List<OrganisaatioOid> haeKaikkiOrganisaatioOidit();
}
