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

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioHenkiloDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 3:27 PM
 */
public class HenkiloHakuResultDto implements Serializable {
    private static final int HASH_FACTOR = 31;

    private static final long serialVersionUID  = -1L;

    private Long id;
    @DtoConversion(path="oidHenkilo", withClass = HenkiloDetailsDto.class)
    private String henkiloOid;
    private String nimi;
    private List<OrganisaatioHenkiloDto> organisaatioHenkilos = new ArrayList<OrganisaatioHenkiloDto>();
    @DtoConversion(path="tyoOsoitees", withClass = HenkiloDetailsDto.class)
    private List<HenkiloOsoiteDto> osoittees = new ArrayList<HenkiloOsoiteDto>();

    public String getHenkiloOid() {
        return henkiloOid;
    }

    public void setHenkiloOid(String henkiloOid) {
        this.henkiloOid = henkiloOid;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public List<HenkiloOsoiteDto> getOsoittees() {
        return osoittees;
    }

    public void setOsoittees(List<HenkiloOsoiteDto> osoittees) {
        this.osoittees = osoittees;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrganisaatioHenkiloDto> getOrganisaatioHenkilos() {
        return organisaatioHenkilos;
    }

    public void setOrganisaatioHenkilos(List<OrganisaatioHenkiloDto> organisaatioHenkilos) {
        this.organisaatioHenkilos = organisaatioHenkilos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HenkiloHakuResultDto that = (HenkiloHakuResultDto) o;

        if (henkiloOid != null ? !henkiloOid.equals(that.henkiloOid) : that.henkiloOid != null) {
            return false;
        }
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (osoittees != null ? !osoittees.equals(that.osoittees) : that.osoittees != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = HASH_FACTOR * result + (henkiloOid != null ? henkiloOid.hashCode() : 0);
        return result;
    }
}
