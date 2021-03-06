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

package fi.vm.sade.osoitepalvelu.kooste.service.organisaatio;

import java.util.List;

import com.google.common.base.Function;

/**
 * User: ratamaa
 * Date: 5/12/14
 * Time: 4:42 PM
 */
public interface FilterableOrganisaatio {

    String getOid();

    List<String> getTyypit();

    List<String> getKielet();

    String getKotipaikka();

    Function<FilterableOrganisaatio, String> GET_OID = new Function<FilterableOrganisaatio, String>() {
        public String apply(FilterableOrganisaatio organisaatio) {
            return organisaatio.getOid(); // NOSONAR
        }
    };
}
