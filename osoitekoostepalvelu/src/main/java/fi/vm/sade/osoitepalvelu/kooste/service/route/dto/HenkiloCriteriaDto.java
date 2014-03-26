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

package fi.vm.sade.osoitepalvelu.kooste.service.route.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 2:57 PM
 */
public class HenkiloCriteriaDto implements Serializable {
    private List<String> organisaatioOids = new ArrayList<String>();

    public List<String> getOrganisaatioOids() {
        return organisaatioOids;
    }

    public void setOrganisaatioOids(List<String> organisaatioOids) {
        this.organisaatioOids = organisaatioOids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HenkiloCriteriaDto that = (HenkiloCriteriaDto) o;

        if (organisaatioOids != null ? !organisaatioOids.equals(that.organisaatioOids) : that.organisaatioOids != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return organisaatioOids != null ? organisaatioOids.hashCode() : 0;
    }
}
