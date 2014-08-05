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
 * Date: 4/9/14
 * Time: 5:10 PM
 */
public class AituOsoitepalveluResultsDto implements Serializable {
    private static final long serialVersionUID = 8330815477399941813L;
    
    private List<AituToimikuntaResultDto> toimikunnat = new ArrayList<AituToimikuntaResultDto>();
    private List<AituOppilaitosResultDto> oppilaitokset = new ArrayList<AituOppilaitosResultDto>();

    public List<AituToimikuntaResultDto> getToimikunnat() {
        return toimikunnat;
    }

    public void setToimikunnat(List<AituToimikuntaResultDto> toimikunnat) {
        this.toimikunnat = toimikunnat;
    }

    public List<AituOppilaitosResultDto> getOppilaitokset() {
        return oppilaitokset;
    }

    public void setOppilaitokset(List<AituOppilaitosResultDto> oppilaitokset) {
        this.oppilaitokset = oppilaitokset;
    }
}
