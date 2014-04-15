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

package fi.vm.sade.osoitepalvelu.kooste.service.route;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;

import java.util.List;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 1:54 PM
 */
public interface OrganisaatioServiceRoute {

    /**
     * @return all organisaatio OIDs
     */
    List<String> findAllOrganisaatioOids(CamelRequestContext requestContext);

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
    OrganisaatioDetailsDto getdOrganisaatioByOid(String oid,
                                 CamelRequestContext requestContext);

    /**
     * @param tyyppi the organisaatio's tyyppi to search for.
     * @param requestContext the context for HTTP request received by the application to operate in
     * @return organisaatio hierarchy results containing only active results
     */
    OrganisaatioHierarchyResultsDto findOrganisaatioHierachyByTyyppi(String tyyppi, CamelRequestContext requestContext);
}
