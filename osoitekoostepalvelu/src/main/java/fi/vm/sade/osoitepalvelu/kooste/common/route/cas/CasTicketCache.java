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

import java.util.Map;

/**
 * Used to store tickets for services that are called multiple times during <b>the same request</b>.
 *
 * User: ratamaa
 * Date: 3/24/14
 * Time: 12:34 PM
 */
public interface CasTicketCache {
    /**
     * @param service to get cached ticket headers for
     * @return the cached headers or null if not cached
     */
    public Map<String, Object> get(String service);

    /**
     * @param service to store headers for
     * @param headers to store
     */
    public void store(String service, Map<String, Object> headers);
}
