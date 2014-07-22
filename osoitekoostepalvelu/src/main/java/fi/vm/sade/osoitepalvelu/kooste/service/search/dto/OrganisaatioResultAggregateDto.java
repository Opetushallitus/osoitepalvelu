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

import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:38 PM
 */
public class OrganisaatioResultAggregateDto implements Serializable {
    private static final int HASH_FACTOR = 31;

    private static final long serialVersionUID  =  1483746476279296389L;
    
    private OrganisaatioResultDto organisaatio;
    private OrganisaatioYhteystietoDto henkilo;
    private OsoitteistoDto kayntiosoite;
    private OsoitteistoDto postiosoite;

    public OrganisaatioResultAggregateDto(OrganisaatioResultDto organisaatio, OrganisaatioYhteystietoDto henkilo,
                                          OsoitteistoDto postiosoite, OsoitteistoDto kayntiosoite) {
        this.organisaatio  =  organisaatio;
        this.henkilo  =  henkilo;
        this.postiosoite  =  postiosoite;
        this.kayntiosoite = kayntiosoite;
    }

    public OrganisaatioResultDto getOrganisaatio() {
        return organisaatio;
    }

    public OrganisaatioYhteystietoDto getHenkilo() {
        return henkilo;
    }

    public OsoitteistoDto getKayntiosoite() {
        return kayntiosoite;
    }

    public void setKayntiosoite(OsoitteistoDto kayntiosoite) {
        this.kayntiosoite = kayntiosoite;
    }

    public OsoitteistoDto getPostiosoite() {
        return postiosoite;
    }

    public void setPostiosoite(OsoitteistoDto postiosoite) {
        this.postiosoite = postiosoite;
    }

    @Override
    public int hashCode() {
        int result  =  (organisaatio != null && organisaatio.getOid() != null ? organisaatio.getOid().hashCode() : 0);
        result  =  HASH_FACTOR * result + (henkilo != null && henkilo.getEmail() != null ? henkilo.getEmail().hashCode() : 0);
        result  =  HASH_FACTOR * result  +  (postiosoite != null && postiosoite.getYhteystietoOid() != null
                ? postiosoite.getYhteystietoOid().hashCode() : 0);
        result  =  HASH_FACTOR * result  +  (kayntiosoite != null && kayntiosoite.getYhteystietoOid() != null
                ? kayntiosoite.getYhteystietoOid().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrganisaatioResultAggregateDto that  =  (OrganisaatioResultAggregateDto) o;
        if (EqualsHelper.differentNulls(organisaatio, that.organisaatio)
                || (EqualsHelper.notNulls(organisaatio, that.organisaatio)
                    && !EqualsHelper.areEquals(organisaatio.getOid(), that.organisaatio))) {
            return false;
        }
        if (EqualsHelper.differentNulls(henkilo, that.henkilo)
                || (EqualsHelper.notNulls(henkilo, that.henkilo)
                    && !EqualsHelper.areEquals(henkilo.getEmail(), that.henkilo.getEmail()))) {
            return false;
        }
        if (EqualsHelper.differentNulls(postiosoite, that.postiosoite)
                || (EqualsHelper.notNulls(postiosoite, that.postiosoite)
                    && !(
                        EqualsHelper.areEquals(postiosoite.getYhteystietoOid(), that.postiosoite.getYhteystietoOid()))
                        && EqualsHelper.areEquals(postiosoite.getKieli(), that.postiosoite.getKieli())
                  )) {
            return false;
        }
        if (EqualsHelper.differentNulls(kayntiosoite, that.kayntiosoite)
                || (EqualsHelper.notNulls(kayntiosoite, that.kayntiosoite)
                && !(
                EqualsHelper.areEquals(kayntiosoite.getYhteystietoOid(), that.kayntiosoite.getYhteystietoOid()))
                && EqualsHelper.areEquals(kayntiosoite.getKieli(), that.kayntiosoite.getKieli()))) {
            return false;
        }
        return true;
    }
}
