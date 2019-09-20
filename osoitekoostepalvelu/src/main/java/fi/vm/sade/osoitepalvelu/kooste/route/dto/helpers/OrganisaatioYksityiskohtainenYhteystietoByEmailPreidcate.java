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

package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.common.util.LocaleHelper;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsYhteystietoDto;

import java.util.Locale;
import java.util.Set;

public class OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate
            implements Predicate<OrganisaatioDetailsYhteystietoDto> {
    private final Set<String> kielet;

    public OrganisaatioYksityiskohtainenYhteystietoByEmailPreidcate(Set<String> kielet) {
        this.kielet = kielet;
    }

    @Override
    public boolean apply(OrganisaatioDetailsYhteystietoDto yhteystieto) {
        return  yhteystieto.getEmail() != null
                && yhteystieto.getKieli() != null
                && kielet.stream().anyMatch(kieli -> yhteystieto.getKieli().startsWith(kieli));
    }
}
