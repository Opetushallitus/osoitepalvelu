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
import fi.vm.sade.osoitepalvelu.kooste.service.route.AuthenticationServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloListResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KayttooikesuryhmaDto;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/12/14
 * Time: 3:18 PM
 */
public class AuhenticationServiceRouteMock implements AuthenticationServiceRoute {
    private List<KayttooikesuryhmaDto> kayttooikesuryhmas  =  new ArrayList<KayttooikesuryhmaDto>();
    private List<HenkiloListResultDto> henkilos  =  new ArrayList<HenkiloListResultDto>();
    private HenkiloDetailsDto henkiloDetails = null;

    public AuhenticationServiceRouteMock(List<KayttooikesuryhmaDto> kayttooikesuryhmas) {
        this.kayttooikesuryhmas  =  kayttooikesuryhmas;
    }

    public AuhenticationServiceRouteMock() {
    }

    @Override
    public List<KayttooikesuryhmaDto> findKayttooikeusryhmas(CamelRequestContext context) {
        return kayttooikesuryhmas;
    }

    @Override
    public List<HenkiloListResultDto> findHenkilos(HenkiloCriteriaDto criteria, CamelRequestContext context) {
        return henkilos;
    }

    @Override
    public HenkiloDetailsDto getHenkiloTiedot(String oid, CamelRequestContext requestContext) {
        return henkiloDetails;
    }

    public void setKayttooikesuryhmas(List<KayttooikesuryhmaDto> kayttooikesuryhmas) {
        this.kayttooikesuryhmas  =  kayttooikesuryhmas;
    }

    public List<HenkiloListResultDto> getHenkilos() {
        return henkilos;
    }

    public void setHenkilos(List<HenkiloListResultDto> henkilos) {
        this.henkilos  =  henkilos;
    }

    public void setHenkiloDetails(HenkiloDetailsDto henkiloDetails) {
        this.henkiloDetails = henkiloDetails;
    }
}
