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

/**
 * User: ratamaa
 * Date: 3/12/14
 * Time: 2:11 PM
 */
public abstract class AbstractCasTicketProvider implements CasTicketProvider {

    protected String getTargetServiceCasUri( String service ) {
        if (!service.endsWith("/j_spring_cas_security_check")) {
            service = service+"/j_spring_cas_security_check";
        }
        if( service.startsWith("https://") ) {
            service = service.replaceAll("(https://)(.*):443(/?.*)", "$1$2$3");
        }
        return service;
    }
}
