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
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KayttooikesuryhmaDto;

import java.util.List;

/**
 * User: ratamaa
 * Date: 3/12/14
 * Time: 3:13 PM
 */
public interface AuthenticationServiceRoute {

    /**
     * @param requestContext the context for HTTP request received by the application to operate in
     * @return all kayttoikeusryhmas
     */
    List<KayttooikesuryhmaDto> findKayttooikeusryhmas(CamelRequestContext requestContext);

    /**
     * @param ooids of the organisaatio
     * @param requestContext the context for HTTP request received by the application to operate in
     * @return henkilös for the organisaatio
     */
    List<HenkiloDto> findHenkilosByOrganisaatioOids(List<String> ooids, CamelRequestContext requestContext);
}
