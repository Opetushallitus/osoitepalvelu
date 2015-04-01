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

package fi.vm.sade.osoitepalvelu.kooste.route;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoulutusCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.TarjontaKoulutusHakuResultDto;

import java.io.Serializable;

/**
 * User: simok
 * Date: 3/23/15
 * Time: 3:29 PM
 */
public interface TarjontaServiceRoute extends Serializable {

    /**
     * @param criteria search criteria for koulutus
     * @param requestContext the context for HTTP request received by the application to operate in
     * @return koulutus hakutulos for the criteria
     */
    public TarjontaKoulutusHakuResultDto findKoulutukset(KoulutusCriteriaDto criteria, CamelRequestContext requestContext);

}
