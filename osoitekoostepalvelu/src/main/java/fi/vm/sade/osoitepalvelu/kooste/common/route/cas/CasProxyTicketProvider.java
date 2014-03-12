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

import fi.vm.sade.generic.ui.portlet.security.ProxyAuthenticator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * User: ratamaa
 * Date: 3/11/14
 * Time: 5:15 PM
 */
public class CasProxyTicketProvider extends AbstractCasTicketProvider {
    private ProxyAuthenticator proxyAuthenticator = new ProxyAuthenticator();
    private String casService;

    public CasProxyTicketProvider(String casService) {
        this.casService = casService;
    }

    @Override
    public String provideTicket(String service) {
        service = getTargetServiceCasUri(service);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if( authentication != null && authentication instanceof UsernamePasswordAuthenticationToken ) {
            // Development mode, this works provided that Spring Security's authentication manager has
            // erase-credientals=false: <authentication-manager alias="authenticationManager"  erase-credentials="false">
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            return new UsernamePasswordCasClientTicketProvider(casService, token.getName(),
                    ""+token.getCredentials()).provideTicket(service);
        }

        // In production we basically do this
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if( authentication == null || !(authentication instanceof CasAuthenticationToken) ) {
//            return null;
//        }
//        Assertion assertion = ((CasAuthenticationToken) authentication).getAssertion();
//        return assertion.getPrincipal().getProxyTicketFor(service);

        // But for performance reasons, use a ticket cache implemented in ProxyAuthenticator (implementation might
        // also change):
        final Holder<String> result = new Holder<String>();
        proxyAuthenticator.proxyAuthenticate(service, "prod", new ProxyAuthenticator.Callback() {
            @Override
            public void setRequestHeader(String key, String value) {
                if(CAS_HEADER.equals(key)) {
                    // Dirty callback solution, but the implementation is not asynchronous, so we are safe here:
                    result.set(value);
                } else {
                    throw new IllegalStateException("Not implemented: The header field " + key + " has been added "
                        + " to CAS authentication procedure and not currently supported by Osoitepalvelu's"
                        + " Camel CAS authentication implementation.");
                }
            }
        });
        return result.get();
    }

    protected static class Holder<T> {
        private T value;

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }
    }
}
