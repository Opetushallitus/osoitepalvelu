/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.osoitepalvelu.kooste.service.organisaatio;

import java.util.List;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYksityiskohtaisetTiedotDto;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:37 AM
 */
public interface OrganisaatioService {

    /**
     * @param criteria for organisaatios' yhteystietos
     * @param requestContext the context for HTTP request received by the application to operate in
     * @return the yhteystietos for the organisaatios matching the search criteria
     */
    List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            OrganisaatioYhteystietoCriteriaDto criteria,
            CamelRequestContext requestContext);

    /**
     * @param oid of the organisaatio
     * @param requestContext the context for HTTP request received by the application to operate in
     * @return details for the organisaatio
     */
    OrganisaatioYksityiskohtaisetTiedotDto getdOrganisaatioByOid(String oid,
                                                                 CamelRequestContext requestContext);

    /**
     * @param oid of the organisaatio to purge from cache
     */
    void purgeOrganisaatioByOidCache(String oid);
}
