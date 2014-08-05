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

import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituJasenyysDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituToimikuntaResultDto;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 4/16/14
 * Time: 5:55 PM
 */
public class AituToimikuntaJasenAggregateDto implements Serializable {
    private static final long serialVersionUID  = -1L;

    private AituToimikuntaResultDto toimikunta;
    private AituJasenyysDto jasen;

    public AituToimikuntaJasenAggregateDto(AituToimikuntaResultDto toimikunta, AituJasenyysDto jasen) {
        this.toimikunta = toimikunta;
        this.jasen = jasen;
    }

    public AituToimikuntaResultDto getToimikunta() {
        return toimikunta;
    }

    public void setToimikunta(AituToimikuntaResultDto toimikunta) {
        this.toimikunta = toimikunta;
    }

    public AituJasenyysDto getJasen() {
        return jasen;
    }

    public void setJasen(AituJasenyysDto jasen) {
        this.jasen = jasen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AituToimikuntaJasenAggregateDto that = (AituToimikuntaJasenAggregateDto) o;

        if (jasen != null ? !jasen.equals(that.jasen) : that.jasen != null) {
            return false;
        }
        if (!toimikunta.getId().equals(that.toimikunta.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return toimikunta.getId().hashCode();
    }
}
