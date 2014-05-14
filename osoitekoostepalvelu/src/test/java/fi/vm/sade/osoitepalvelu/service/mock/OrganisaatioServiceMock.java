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
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.OrganisaatioService;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: ratamaa
 * Date: 3/17/14
 * Time: 9:27 AM
 */
public class OrganisaatioServiceMock implements OrganisaatioService {
    private List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults  =  new ArrayList<OrganisaatioYhteystietoHakuResultDto>();
    private OrganisaatioDetailsDto yksityiskohtaisetTiedotByOid;

    @Override
    public List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            OrganisaatioYhteystietoCriteriaDto criteria,
            Locale locale, CamelRequestContext requestContext) {
        return organisaatioYhteystietoResults;
    }

    @Override
    public OrganisaatioDetailsDto getdOrganisaatioByOid(String oid, CamelRequestContext context) {
        return yksityiskohtaisetTiedotByOid;
    }

    @Override
    public void purgeOrganisaatioByOidCache(String oid) {
    }

    @Override
    public void updateOrganisaatioYtunnusDetails(CamelRequestContext requestContext) {
    }

    @Override
    public List<String> findAllOidsOfCachedOrganisaatios() {
        return new ArrayList<String>();
    }

    public void setYksityiskohtaisetTiedotByOid(OrganisaatioDetailsDto yksityiskohtaisetTiedotByOid) {
        this.yksityiskohtaisetTiedotByOid  =  yksityiskohtaisetTiedotByOid;
    }

    public void setOrganisaatioYhteystietoResults(List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults) {
        this.organisaatioYhteystietoResults  =  organisaatioYhteystietoResults;
    }
}
