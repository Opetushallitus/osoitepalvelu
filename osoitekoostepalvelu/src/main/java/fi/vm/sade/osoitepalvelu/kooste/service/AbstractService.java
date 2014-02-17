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

package fi.vm.sade.osoitepalvelu.kooste.service;

import fi.vm.sade.osoitepalvelu.kooste.common.exception.AuthorizationException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:25 PM
 */
public abstract class AbstractService {

    protected String getLoggedInUserOid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    protected <T> T found(T obj) throws NotFoundException {
        if (obj == null) {
            throw new NotFoundException("Entity not found by primary key.");
        }
        return obj;
    }

    protected void ensureLoggedInUser(String ownerUsername) {
        if (!EqualsHelper.equals(ownerUsername, getLoggedInUserOid())) {
            throw new AuthorizationException("Authenticated user " + getLoggedInUserOid()
                    + " does not have access right to given entity.");
        }
    }
}
