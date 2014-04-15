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

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the CAS authentication headers using ProxyAuthenticator by default.
 * @see ProxyAuthenticator that uses an internal ticket cache
 *
 * However, if "dev" authMode is used and the Spring Security's context contains a
 * UsernamePasswordAuthenticationToken, which is the case for the basic authentication
 * used in development environment, act as UsernamePasswordCasClientTicketProvider
 * using the username and passwrod found in the token to issue a new CAS ticket for
 * the service.
 * @see UsernamePasswordCasClientTicketProvider
 *
 * @see CasTicketProvider
 *
 * User: ratamaa
 * Date: 3/11/14
 * Time: 5:15 PM
 */
public class CasProxyTicketProvider extends AbstractCasTicketProvider {
    private ProxyAuthenticator proxyAuthenticator  =  new ProxyAuthenticator();
    private String casService;
    private String authMode;

    public CasProxyTicketProvider(String casService, String authMode) {
        this.casService  =  casService;
        this.authMode  =  authMode;
    }

    @Override
    public Map<String, Object> provideTicketHeaders(String service) {
        service  =  getTargetServiceCasUri(service);
        Authentication authentication  =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication instanceof UsernamePasswordAuthenticationToken
                && "dev".equals(authMode)) {
            // Development mode, this works provided that Spring Security's authentication manager has
            // erase-credientals = false: <authentication-manager alias = "authenticationManager"  erase-credentials = "false">
            UsernamePasswordAuthenticationToken token  =  (UsernamePasswordAuthenticationToken) authentication;
            return new UsernamePasswordCasClientTicketProvider(casService, token.getName(),
                    ""  +  token.getCredentials()).provideTicketHeaders(service);
        }

        // In production we basically do this
//        Authentication authentication  =  SecurityContextHolder.getContext().getAuthentication();
//        Assertion assertion  =  ((CasAuthenticationToken) authentication).getAssertion();
//        return assertion.getPrincipal().getProxyTicketFor(service);

        // But for performance reasons, use a ticket cache implemented in ProxyAuthenticator (implementation might
        // also change):
        final Map<String, Object> result  =  new HashMap<String, Object>();
        proxyAuthenticator.proxyAuthenticate(service, "cas", new ProxyAuthenticator.Callback() {
            @Override
            public void setRequestHeader(String key, String value) {
                // Dirty callback solution, but the implementation is not asynchronous, so we are safe here:
                result.put(key, value);
            }
        });
        return result;
    }
}
