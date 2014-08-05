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

package fi.vm.sade.osoitepalvelu;


import fi.vm.sade.generic.ui.portlet.security.SecurityTicketOutInterceptorRest;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasDisabledCasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.CasTicketProvider;
import fi.vm.sade.osoitepalvelu.kooste.common.route.cas.UsernamePasswordCasClientTicketProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 8/5/14
 * Time: 3:45 PM
 */
@Configuration
@PropertySource({"classpath:/test.properties" })
@Import(value = {SpringTestAppConfig.class, MongoTestConfig.class})
public class SpringItestConfig {

    @Autowired
    private Environment env;

    @Value("${web.url.cas}")
    private String casService;

    @Value("${cas.service.authentication-service:}")
    private String authenticationCasService;
    @Value("${osoitepalvelu.app.username.to.authenticationservice:}")
    private String casAuthenticationServiceUsername;
    @Value("${osoitepalvelu.app.password.to.authenticationservice:}")
    private String casAuthenticationServicePassword;

    @Value("${web.url.cas.aitu:}")
    private String aituCasService;
    @Value("${cas.service.aitu-service:}")
    private String aituServiceCasServiceUrl;
    @Value("${osoitepalvelu.app.username.to.aituservice:}")
    private String casAituServiceUsername;
    @Value("${osoitepalvelu.app.password.to.aituservice:}")
    private String casAituServicePassword;

    @Bean
    public SecurityTicketOutInterceptorRest ticketInterceptorRest() {
        return new SecurityTicketOutInterceptorRest();
    }

    @Bean
    public CasTicketProvider usernamePasswordCasTicketProvider() {
        final CasTicketProvider defaultProvider  =  new CasDisabledCasTicketProvider();
        final Map<String, CasTicketProvider> providersByService  =  new HashMap<String, CasTicketProvider>();
        providersByService.put(authenticationCasService, new UsernamePasswordCasClientTicketProvider(casService,
                casAuthenticationServiceUsername, casAuthenticationServicePassword));
        providersByService.put(authenticationCasService, new UsernamePasswordCasClientTicketProvider(casService,
                casAuthenticationServiceUsername, casAuthenticationServicePassword));

        CasTicketProvider aituCasProvider;
        if (casAituServiceUsername != null && casAituServiceUsername.length() > 0) {
            aituCasProvider = new UsernamePasswordCasClientTicketProvider(aituCasService, casAituServiceUsername,
                    casAituServicePassword, false);
        } else {
            aituCasProvider = defaultProvider;
        }
        providersByService.put(aituServiceCasServiceUrl, aituCasProvider);
        // ..

        // Always use username and password authentication in tests (since user not logged in):
        return new CasTicketProvider() {
            @Override
            public Map<String, Object> provideTicketHeaders(String service) {
                if(providersByService.containsKey(service)) {
                    return providersByService.get(service).provideTicketHeaders(service);
                }
                return defaultProvider.provideTicketHeaders(service);
            }
        };
    }

}
