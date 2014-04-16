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

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 12:47 PM
 */
public class DefaultCookieBasedCasTicketCache implements CasTicketCache {
    public static final String SET_COOKIE_HEADER = "Set-Cookie";
    public static final String COOKIE_HEADER = "Cookie";
    private Map<String, Map<String, Object>> store  =  new HashMap<String, Map<String, Object>>();

    @Override
    public Map<String, Object> get(String service) {
        return this.store.get(service);
    }

    @Override
    public void store(String service, Map<String, Object> headers) {
        Object cookieHeader = headers.get(SET_COOKIE_HEADER);
        if (cookieHeader != null) {
            if (cookieHeader instanceof String) {
                List<HttpCookie> cookies = HttpCookie.parse((String)cookieHeader);
                Map<String, Object> storeHeaders = new HashMap<String, Object>();
                for (HttpCookie cookie : cookies) {
                    storeHeaders.put(COOKIE_HEADER, cookie.toString());
                    this.store.put(service, storeHeaders);
                }
            }
        }
    }
}
