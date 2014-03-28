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

package fi.vm.sade.osoitepalvelu.service.mock;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioHierarchyResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/28/14
 * Time: 4:13 PM
 */
public class OrganisaatioServiceRouteMock implements OrganisaatioServiceRoute {
    private List<String> oids = new ArrayList<String>();
    private List<OrganisaatioYhteystietoHakuResultDto> yhteystietoResults
            = new ArrayList<OrganisaatioYhteystietoHakuResultDto>();
    private OrganisaatioDetailsDto details;
    private OrganisaatioHierarchyResultsDto hierarchyResults;

    @Override
    public List<String> findAllOrganisaatioOids(CamelRequestContext requestContext) {
        return oids;
    }

    @Override
    public List<OrganisaatioYhteystietoHakuResultDto>
            findOrganisaatioYhteystietos(OrganisaatioYhteystietoCriteriaDto criteria, CamelRequestContext requestContext) {
        return yhteystietoResults;
    }

    @Override
    public OrganisaatioDetailsDto getdOrganisaatioByOid(String oid, CamelRequestContext requestContext) {
        return details;
    }

    @Override
    public OrganisaatioHierarchyResultsDto findOrganisaatioHierachyByTyyppi(String tyyppi, CamelRequestContext requestContext) {
        return hierarchyResults;
    }

    public void setOids(List<String> oids) {
        this.oids = oids;
    }

    public void setYhteystietoResults(List<OrganisaatioYhteystietoHakuResultDto> yhteystietoResults) {
        this.yhteystietoResults = yhteystietoResults;
    }

    public void setDetails(OrganisaatioDetailsDto details) {
        this.details = details;
    }

    public void setHierarchyResults(OrganisaatioHierarchyResultsDto hierarchyResults) {
        this.hierarchyResults = hierarchyResults;
    }
}
