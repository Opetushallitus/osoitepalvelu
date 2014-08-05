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

package fi.vm.sade.osoitepalvelu.kooste.route.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 2:57 PM
 */
public class HenkiloCriteriaDto implements Serializable {
    private static final long serialVersionUID = -6881901596982757750L;
    
    private static final int HASH_FACTOR = 31;
    private List<String> organisaatioOids = new ArrayList<String>();
    private List<String> kayttoOikeusRayhmas = new ArrayList<String>();

    public List<String> getOrganisaatioOids() {
        return organisaatioOids;
    }

    public void setOrganisaatioOids(List<String> organisaatioOids) {
        this.organisaatioOids = organisaatioOids;
    }

    public List<String> getKayttoOikeusRayhmas() {
        return kayttoOikeusRayhmas;
    }

    public void setKayttoOikeusRayhmas(List<String> kayttoOikeusRayhmas) {
        this.kayttoOikeusRayhmas = kayttoOikeusRayhmas;
    }

    public boolean isOrganisaatioUsed() {
        return this.organisaatioOids != null && !this.organisaatioOids.isEmpty();
    }

    public boolean isKayttoOikeusRyhmasUsed() {
        return this.kayttoOikeusRayhmas != null && !this.kayttoOikeusRayhmas.isEmpty();
    }

    public int getNumberOfUsedConditions() {
        return (isOrganisaatioUsed() ? 1 : 0)
                + (isKayttoOikeusRyhmasUsed() ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HenkiloCriteriaDto that = (HenkiloCriteriaDto) o;

        if (kayttoOikeusRayhmas != null
                ? !kayttoOikeusRayhmas.equals(that.kayttoOikeusRayhmas) : that.kayttoOikeusRayhmas != null) {
            return false;
        }
        if (organisaatioOids != null ? !organisaatioOids.equals(that.organisaatioOids) : that.organisaatioOids != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = organisaatioOids != null ? organisaatioOids.hashCode() : 0;
        result = HASH_FACTOR * result + (kayttoOikeusRayhmas != null ? kayttoOikeusRayhmas.hashCode() : 0);
        return result;
    }
}
