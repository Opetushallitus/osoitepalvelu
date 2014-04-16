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

package fi.vm.sade.osoitepalvelu.kooste.common.route.cas;

import java.util.Map;

/**
 * User: ratamaa
 * Date: 3/24/14
 * Time: 12:37 PM
 */
public class LazyCasTicketProvider implements CasTicketProvider {
    private CasTicketCache cache;
    private CasTicketProvider target;

    public LazyCasTicketProvider(CasTicketCache cache, CasTicketProvider target) {
        this.cache  =  cache;
        this.target  =  target;
    }

    @Override
    public Map<String, Object> provideTicketHeaders(String service) {
        Map<String, Object> headers = null;
        if (this.cache != null) {
            headers  =  this.cache.get(service);
            if (headers != null) {
                return headers;
            }
        }
        headers = this.target.provideTicketHeaders(service);
        return headers;
    }
}
