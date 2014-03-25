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
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYksityiskohtaisetTiedotDto;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/17/14
 * Time: 9:27 AM
 */
public class OrganisaatioServiceRouteMock implements OrganisaatioServiceRoute {
    private List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults  =  new ArrayList<OrganisaatioYhteystietoHakuResultDto>();
    private OrganisaatioYksityiskohtaisetTiedotDto yksityiskohtaisetTiedotByOid;

    @Override
    public List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(OrganisaatioYhteystietoCriteriaDto criteria,
                                                                                   CamelRequestContext context) {
        return organisaatioYhteystietoResults;
    }

    @Override
    public OrganisaatioYksityiskohtaisetTiedotDto getdOrganisaatioByOid(String oid,
                                                                        CamelRequestContext context) {
        return yksityiskohtaisetTiedotByOid;
    }

    public void setYksityiskohtaisetTiedotByOid(OrganisaatioYksityiskohtaisetTiedotDto yksityiskohtaisetTiedotByOid) {
        this.yksityiskohtaisetTiedotByOid  =  yksityiskohtaisetTiedotByOid;
    }

    public void setOrganisaatioYhteystietoResults(List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults) {
        this.organisaatioYhteystietoResults  =  organisaatioYhteystietoResults;
    }
}
