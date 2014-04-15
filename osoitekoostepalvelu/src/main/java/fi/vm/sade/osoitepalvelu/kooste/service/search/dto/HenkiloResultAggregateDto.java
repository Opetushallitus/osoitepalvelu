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

package fi.vm.sade.osoitepalvelu.kooste.service.search.dto;

import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioHenkiloDto;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 5:15 PM
 */
public class HenkiloResultAggregateDto implements Serializable {
    private static final int HASH_FACTOR = 31;

    private static final long serialVersionUID  = -1L;

    private HenkiloHakuResultDto henkilo;
    private OrganisaatioHenkiloDto organisaatioHenkilo;
    private HenkiloOsoiteDto osoite;

    public HenkiloResultAggregateDto(HenkiloHakuResultDto henkilo,
                                     OrganisaatioHenkiloDto organisaatioHenkilo,
                                     HenkiloOsoiteDto osoite) {
        this.henkilo = henkilo;
        this.organisaatioHenkilo = organisaatioHenkilo;
        this.osoite = osoite;
    }

    public HenkiloHakuResultDto getHenkilo() {
        return henkilo;
    }

    public OrganisaatioHenkiloDto getOrganisaatioHenkilo() {
        return organisaatioHenkilo;
    }

    public void setHenkilo(HenkiloHakuResultDto henkilo) {
        this.henkilo = henkilo;
    }

    public void setOrganisaatioHenkilo(OrganisaatioHenkiloDto organisaatioHenkilo) {
        this.organisaatioHenkilo = organisaatioHenkilo;
    }

    public HenkiloOsoiteDto getOsoite() {
        return osoite;
    }

    public void setOsoite(HenkiloOsoiteDto osoite) {
        this.osoite = osoite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HenkiloResultAggregateDto that = (HenkiloResultAggregateDto) o;

        if (!henkilo.equals(that.henkilo)) {
            return false;
        }
        if (organisaatioHenkilo != null ? !organisaatioHenkilo.equals(that.organisaatioHenkilo) : that.organisaatioHenkilo != null) {
            return false;
        }
        if (osoite != null ? !osoite.equals(that.osoite) : that.osoite != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = henkilo.hashCode();
        result = HASH_FACTOR * result + (organisaatioHenkilo != null ? organisaatioHenkilo.hashCode() : 0);
        result = HASH_FACTOR * result + (osoite != null ? osoite.hashCode() : 0);
        return result;
    }
}
