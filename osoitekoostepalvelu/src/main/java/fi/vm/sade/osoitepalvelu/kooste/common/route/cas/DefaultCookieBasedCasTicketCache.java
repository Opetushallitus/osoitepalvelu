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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 12:47 PM
 */
public class DefaultCookieBasedCasTicketCache implements CasTicketCache {
    public static final String SET_COOKIE_HEADER = "Set-Cookie";
    public static final String COOKIE_HEADER = "Cookie";
    private final Map<String, Map<String, Object>> store = new ConcurrentHashMap<>();

    @Override
    public Map<String, Object> get(String service) {
        return this.store.getOrDefault(service, Collections.EMPTY_MAP);
    }

    @Override
    public void store(String service, Map<String, Object> headers) {
        String cookies = generateCookieString(getCookies(headers));
        if ( !cookies.isEmpty() ) {
            this.store.put(service, Map.of(COOKIE_HEADER, cookies + ";CSRF=CSRF"));
        }
    }

    protected String generateCookieString(List<String> headers) {
        return headers
                .stream()
                .map(cookie -> HttpCookie.parse(cookie).stream().findFirst())
                .map(Optional::get)
                .filter(cookie -> !cookie.getName().equalsIgnoreCase("CSRF"))
                .map(HttpCookie::toString)
                .collect(Collectors.joining(";"));
    }

    protected List<String> getCookies(Map<String, Object> headers) {
        Object cookieHeader = headers.getOrDefault(SET_COOKIE_HEADER, Collections.EMPTY_LIST);
        if (cookieHeader instanceof String) {
            return Arrays.asList((String) cookieHeader);
        }
        return (List<String>) cookieHeader;
    }
}
