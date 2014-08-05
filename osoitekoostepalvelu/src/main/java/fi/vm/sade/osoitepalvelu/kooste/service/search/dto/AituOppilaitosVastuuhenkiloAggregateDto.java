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

import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituOppilaitosResultDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituTutkintoDto;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 7/11/14
 * Time: 10:31 AM
 */
public class AituOppilaitosVastuuhenkiloAggregateDto implements Serializable {
    private static final long serialVersionUID = -3490764493902616037L;

    private static final int HASH_FACTOR = 31;

    private AituOppilaitosResultDto oppilaitos;
    private AituTutkintoDto tutkinto;

    public AituOppilaitosVastuuhenkiloAggregateDto(AituOppilaitosResultDto oppilaitos, AituTutkintoDto tutkinto) {
        this.oppilaitos = oppilaitos;
        this.tutkinto = tutkinto;
    }

    public AituOppilaitosResultDto getOppilaitos() {
        return oppilaitos;
    }

    public void setOppilaitos(AituOppilaitosResultDto oppilaitos) {
        this.oppilaitos = oppilaitos;
    }

    public AituTutkintoDto getTutkinto() {
        return tutkinto;
    }

    public void setTutkinto(AituTutkintoDto tutkinto) {
        this.tutkinto = tutkinto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AituOppilaitosVastuuhenkiloAggregateDto)) {
            return false;
        }

        AituOppilaitosVastuuhenkiloAggregateDto that = (AituOppilaitosVastuuhenkiloAggregateDto) o;

        if (oppilaitos != null ? !oppilaitos.equals(that.oppilaitos) : that.oppilaitos != null) {
            return false;
        }
        if (tutkinto != null ? !tutkinto.equals(that.tutkinto) : that.tutkinto != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = oppilaitos != null ? oppilaitos.hashCode() : 0;
        result = HASH_FACTOR * result + (tutkinto != null ? tutkinto.hashCode() : 0);
        return result;
    }
}
