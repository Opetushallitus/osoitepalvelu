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

package fi.vm.sade.osoitepalvelu.kooste.scheduled;

import java.util.Map;

import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.DefaultCookieBasedCasTicketCache;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 12:51 PM
 */
public class ProviderOverriddenCasTicketCache extends DefaultCookieBasedCasTicketCache {
    private CasTicketProvider ticketProvider;

    public ProviderOverriddenCasTicketCache(CasTicketProvider ticketProvider) {
        this.ticketProvider = ticketProvider;
    }

    @Override
    public Map<String, Object> get(String service) {
        Map<String, Object> cached = super.get(service);
        if (cached == null) {
            cached = ticketProvider.provideTicketHeaders(service);
            store(service, cached);
        }
        return cached;
    }
}
