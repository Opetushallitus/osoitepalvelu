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

import java.io.Serializable;

import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OsoitteistoDto;

/**
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:38 PM
 */
public class ResultAggregateDto implements Serializable {
    private static final long serialVersionUID = 1483746476279296389L;
    
    private OrganisaatioResultDto organisaatio;
    private OrganisaatioYhteystietoDto henkilo;
    private OsoitteistoDto osoite;

    public ResultAggregateDto(OrganisaatioResultDto organisaatio, OrganisaatioYhteystietoDto henkilo, OsoitteistoDto osoite) {
        this.organisaatio = organisaatio;
        this.henkilo = henkilo;
        this.osoite = osoite;
    }

    public OrganisaatioResultDto getOrganisaatio() {
        return organisaatio;
    }

    public OrganisaatioYhteystietoDto getHenkilo() {
        return henkilo;
    }

    public OsoitteistoDto getOsoite() {
        return osoite;
    }

    @Override
    public int hashCode() {
        int result = ( organisaatio != null && organisaatio.getOid() != null ? organisaatio.getOid().hashCode() : 0 );
        result = 31 * result + (henkilo != null && henkilo.getEmail() != null ? henkilo.getEmail().hashCode() : 0);
        result = 31 * result + (osoite != null && osoite.getYhteystietoOid() != null ? osoite.getYhteystietoOid().hashCode() : 0);
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
        ResultAggregateDto that = (ResultAggregateDto) o;
        if (EqualsHelper.differentNulls(osoite, that.osoite)
                || (EqualsHelper.notNulls(osoite, that.osoite)
                    && !EqualsHelper.equals(organisaatio.getOid(), that.organisaatio))) {
            return false;
        }
        if (EqualsHelper.differentNulls(osoite, that.osoite)
                || (EqualsHelper.notNulls(osoite, that.osoite)
                    && !EqualsHelper.equals(henkilo.getEmail(), that.henkilo.getEmail()))) {
            return false;
        }
        if ( EqualsHelper.differentNulls(osoite, that.osoite)
                || (EqualsHelper.notNulls(osoite, that.osoite)
                    && !(
                        EqualsHelper.equals(osoite.getYhteystietoOid(), that.osoite.getYhteystietoOid()))
                        && EqualsHelper.equals(osoite.getKieli(), that.osoite.getKieli())
                    ) ) {
            return false;
        }
        return true;
    }
}
