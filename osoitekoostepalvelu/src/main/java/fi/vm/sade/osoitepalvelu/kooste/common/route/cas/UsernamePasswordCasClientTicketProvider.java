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

import fi.vm.sade.authentication.cas.CasClient;

import java.util.HashMap;
import java.util.Map;

/**
 * To be used with system username and password authentication mode, tests and, in development
 * mode as a replacement for
 * @see CasProxyTicketProvider
 *
 * Uses CasClient to issuea a new CAS ticket by given username and password.
 * @see CasClient
 *
 * @see AbstractCasTicketProvider
 *
 * User: ratamaa
 * Date: 3/11/14
 * Time: 5:22 PM
 */
public class UsernamePasswordCasClientTicketProvider extends AbstractCasTicketProvider {
    private String casService;
    private String username;
    private String password;
    private boolean modifyUrl=true;

    public UsernamePasswordCasClientTicketProvider(String casService, String username, String password) {
        this.casService  =  casService;
        this.username  =  username;
        this.password  =  password;
    }

    public UsernamePasswordCasClientTicketProvider(String casService,
                       String username, String password, boolean modifyUrl) {
        this(casService, username, password);
        this.modifyUrl = modifyUrl;
    }

    @Override
    public Map<String, Object> provideTicketHeaders(String service) {
        Map<String, Object> headers  =  new HashMap<String, Object>();
        String serviceUrl = service;
        if (modifyUrl) {
            serviceUrl = getTargetServiceCasUri(serviceUrl);
        }
        String casHeader  =  CasClient.getTicket(casService +  "/v1/tickets", username, password, serviceUrl, false);
        headers.put(CAS_HEADER, casHeader);
        return headers;
    }
}
