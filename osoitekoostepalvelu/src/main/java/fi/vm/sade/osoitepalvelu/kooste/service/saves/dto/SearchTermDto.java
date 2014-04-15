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

package fi.vm.sade.osoitepalvelu.kooste.service.saves.dto;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:30 PM
 */
@DtoConversion
public class SearchTermDto implements Serializable {
    public static final String TERM_KUNTAS = "kuntas";
    public static final String TERM_MAAKUNTAS = "maakuntas";
    private static final long serialVersionUID  =  -1563200682961231758L;

    public static final String TERM_ORGANISAATION_KIELIS = "organisaationKielis";
    public static final String TERM_OPPILAITOSTYYPPIS = "oppilaitostyyppis";
    public static final String TERM_VUOSILUOKKAS = "vuosiluokkas";
    public static final String TERM_KOULTUKSENJARJESTAJAS = "koultuksenjarjestajas";
    public static final String TERM_KAYTTOOIKEUSRYHMAS = "koulutaRoolis";

    private String type;
    private List<String> values  =  new ArrayList<String>();

    public SearchTermDto() {
    }

    public SearchTermDto(String type, List<String> values) {
        this.type  =  type;
        this.values  =  values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type  =  type;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values  =  values;
    }
}
