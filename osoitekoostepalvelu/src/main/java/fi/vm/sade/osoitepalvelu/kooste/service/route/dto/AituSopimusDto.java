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

import com.google.common.base.Function;

import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * User: ratamaa
 * Date: 4/9/14
 * Time: 5:34 PM
 */
public class AituSopimusDto implements Serializable {
    private static final long serialVersionUID = 7422103301591584089L;

    @Indexed
    private String toimikunta;
    private String sahkoposti;
    private String vastuuhenkilo;
    private List<AituTutkintoDto> tutkinnot = new ArrayList<AituTutkintoDto>();

    public static final Function<AituSopimusDto, String> TOIMIKUNTA = new Function<AituSopimusDto, String>() {
        public String apply(@NotNull AituSopimusDto input) {
            return input.getToimikunta();
        }
    };
    public static final Function<AituSopimusDto, List<AituTutkintoDto>> TUTKINNOT = new Function<AituSopimusDto, List<AituTutkintoDto>>() {
        public List<AituTutkintoDto> apply(@NotNull AituSopimusDto input) {
            return input.getTutkinnot();
        }
    };

    public String getToimikunta() {
        return toimikunta;
    }

    public void setToimikunta(String toimikunta) {
        this.toimikunta = toimikunta;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }

    public String getVastuuhenkilo() {
        return vastuuhenkilo;
    }

    public void setVastuuhenkilo(String vastuuhenkilo) {
        this.vastuuhenkilo = vastuuhenkilo;
    }

    public List<AituTutkintoDto> getTutkinnot() {
        return tutkinnot;
    }

    public void setTutkinnot(List<AituTutkintoDto> tutkinnot) {
        this.tutkinnot = tutkinnot;
    }
}
