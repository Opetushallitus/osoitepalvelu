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

package fi.vm.sade.osoitepalvelu.kooste.common.route;

import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketCache;

/**
 * Context for Camel requests sent during the same HTTP request.
 *
 * User: ratamaa
 * Date: 3/24/14
 * Time: 12:44 PM
 */
public interface CamelRequestContext {

    /**
     * @return the Camel ticket cache used or null
     */
    CasTicketCache getTicketCache();

    /**
     * @return the request count
     */
    long getRequestCount();

    /**
     * Call when request is to be performed
     */
    void requestPerformed();
}
