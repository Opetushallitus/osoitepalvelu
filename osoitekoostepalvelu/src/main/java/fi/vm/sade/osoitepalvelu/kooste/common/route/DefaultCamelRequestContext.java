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
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.DefaultCookieBasedCasTicketCache;

/**
 * User: ratamaa
 * Date: 3/24/14
 * Time: 12:46 PM
 */
public class DefaultCamelRequestContext implements CamelRequestContext {
    private CasTicketCache ticketCache;
    private long requestCount=0;

    public DefaultCamelRequestContext(CasTicketCache ticketCache) {
        this.ticketCache = ticketCache;
    }

    public DefaultCamelRequestContext() {
        this.ticketCache  =  new DefaultCookieBasedCasTicketCache();
    }

    @Override
    public CasTicketCache getTicketCache() {
        return this.ticketCache;
    }

    @Override
    public long getRequestCount() {
        return requestCount;
    }

    @Override
    public void requestPerformed() {
        this.requestCount++;
    }
}
