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
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:25 PM
 */
public abstract class AbstractService {
    public static final Locale DEFAULT_LOCALE  =  new Locale("fi", "FI");
    public static final int MILLIS_IN_SECOND = 1000;

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    protected String getLoggedInUserOid() {
        Authentication auth  =  SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            throw new AuthorizationException("User not logged in.");
        }
        return auth.getName();
    }

    protected String getLoggedInUserOidOrNull() {
        Authentication auth  =  SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return null;
        }
        return auth.getName();
    }

    protected void ensureLoggedInUser(String ownerUsername) {
        if (!EqualsHelper.areEquals(ownerUsername, getLoggedInUserOid())) {
            throw new AuthorizationException("Authenticated user "  +  getLoggedInUserOid()
                     +  " does not have access right to given entity.");
        }
    }

    protected String localized(Map<String, String> nimi, Locale preferredLocale, Locale defaultLocale) {
        return LocaleHelper.findLocalized(nimi, preferredLocale, defaultLocale);
    }
}
