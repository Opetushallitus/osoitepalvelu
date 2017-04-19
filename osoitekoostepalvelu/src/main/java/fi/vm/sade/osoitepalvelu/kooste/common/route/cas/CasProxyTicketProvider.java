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

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.authentication.CasAuthenticationToken;

/**
 * Provides the CAS authentication headers.
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

    private String casService;
    private String authMode;

    public CasProxyTicketProvider(String casService, String authMode) {
        this.casService  =  casService;
        this.authMode  =  authMode;
    }

    @Override
    public Map<String, Object> provideTicketHeaders(String service) {
        final String targetService  =  getTargetServiceCasUri(service);
        Authentication authentication  =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken
                && "dev".equals(authMode)) {
            // Development mode, this works provided that Spring Security's authentication manager has
            // erase-credientals = false: <authentication-manager alias = "authenticationManager"  erase-credentials = "false">
            UsernamePasswordAuthenticationToken token  =  (UsernamePasswordAuthenticationToken) authentication;
            return new UsernamePasswordCasClientTicketProvider(casService, token.getName(),
                    ""  +  token.getCredentials()).provideTicketHeaders(targetService);
        }

        final Map<String, Object> result  =  new HashMap<String, Object>();
        Assertion assertion = ((CasAuthenticationToken) authentication).getAssertion();
        String proxyTicket = assertion.getPrincipal().getProxyTicketFor(targetService);
        if (proxyTicket == null) {
            throw new NullPointerException(
                    "obtainNewCasProxyTicket got null proxyticket, there must be something wrong with cas proxy authentication -scenario! check proxy callback works etc, targetService: "
                            + targetService + ", user: " + authentication.getName());
        }
        result.put("CasSecurityTicket", proxyTicket);
        return result;
    }
}
