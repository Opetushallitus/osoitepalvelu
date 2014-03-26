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

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 2:37 PM
 */
public class OrganisaatioHenkiloDto implements Serializable {
    private static final int HASH_FACTOR = 31;

    private static final long serialVersionUID  = -1L;

    private Long id;
    private String organisaatioOid;
    private String tehtavanimike;
    private boolean passivoitu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganisaatioOid() {
        return organisaatioOid;
    }

    public void setOrganisaatioOid(String organisaatioOid) {
        this.organisaatioOid = organisaatioOid;
    }

    public String getTehtavanimike() {
        return tehtavanimike;
    }

    public void setTehtavanimike(String tehtavanimike) {
        this.tehtavanimike = tehtavanimike;
    }

    public boolean isPassivoitu() {
        return passivoitu;
    }

    public void setPassivoitu(boolean passivoitu) {
        this.passivoitu = passivoitu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganisaatioHenkiloDto that = (OrganisaatioHenkiloDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (organisaatioOid != null ? !organisaatioOid.equals(that.organisaatioOid) : that.organisaatioOid != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = HASH_FACTOR * result + (organisaatioOid != null ? organisaatioOid.hashCode() : 0);
        return result;
    }
}
