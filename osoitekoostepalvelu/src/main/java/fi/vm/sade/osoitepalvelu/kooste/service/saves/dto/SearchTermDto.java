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
    private static final long serialVersionUID  =  -1563200682961231758L;
    private static final int HASH_FACTOR = 31;

    public static final String TERM_KUNTAS = "kuntas";
    public static final String TERM_MAAKUNTAS = "maakuntas";

    public static final String TERM_ORGANISAATION_OPETUSKIELIS = "organisaationKielis";
    public static final String TERM_OPPILAITOSTYYPPIS = "oppilaitostyyppis";
    public static final String TERM_VUOSILUOKKAS = "vuosiluokkas";
    public static final String TERM_KOULTUKSENJARJESTAJAS = "koultuksenjarjestajas";
    public static final String TERM_KAYTTOOIKEUSRYHMAS = "koulutaRoolis";
    public static final String TERM_TUTKINTOIMIKUNTA_ROOLIS = "tutkintotoimikuntaRoolis";
    public static final String TERM_TUTKINTOIMIKUNTA = "tutkintotoimikuntas";
    public static final String TERM_TUTKINTOIMIKUNTA_TOIMIKAUSIS = "tutkintotoimikuntaToimikausis";
    public static final String TERM_TUTKINTOIMIKUNTA_KIELIS = "tutkintotoimikuntaKielis";
    public static final String TERM_TUTKINTOIMIKUNTA_JASEN_KIELIS = "tutkintotoimikuntaJasenKielis";
    public static final String TERM_KOULUTUSALAS = "koulutusalas";
    public static final String TERM_OPINTOALAS = "opintoalas";
    public static final String TERM_KOULUTUS = "koulutus";
    public static final String TERM_KOULUTUSTYYPPIS = "koulutustyyppis";


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchTermDto)) {
            return false;
        }

        SearchTermDto that = (SearchTermDto) o;

        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        if (values != null ? !values.equals(that.values) : that.values != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = HASH_FACTOR * result + (values != null ? values.hashCode() : 0);
        return result;
    }
}
