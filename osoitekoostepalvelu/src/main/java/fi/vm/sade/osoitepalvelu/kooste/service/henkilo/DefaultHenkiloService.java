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

package fi.vm.sade.osoitepalvelu.kooste.service.henkilo;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.route.AuthenticationServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 2:30 PM
 */
@Service
public class DefaultHenkiloService extends AbstractService implements HenkiloService {
    private static final long serialVersionUID = 2098849501877436535L;

    @Autowired(required = false)
    private AuthenticationServiceRoute authenticationServiceRoute;

    @Override
    public List<HenkiloDetailsDto> findHenkilos(HenkiloCriteriaDto criteria, CamelRequestContext requestContext) {
        return authenticationServiceRoute.findHenkilos(criteria, requestContext);
    }

    public void setAuthenticationServiceRoute(AuthenticationServiceRoute authenticationServiceRoute) {
        this.authenticationServiceRoute = authenticationServiceRoute;
    }
}
