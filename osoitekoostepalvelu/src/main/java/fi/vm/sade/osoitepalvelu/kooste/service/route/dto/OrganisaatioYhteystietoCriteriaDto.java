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
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 2:08 PM
 */
public class OrganisaatioYhteystietoCriteriaDto implements Serializable {
    private List<String> kieliList = new ArrayList<String>();
    private List<String> kuntaList = new ArrayList<String>();
    private List<String> oppilaitostyyppiList = new ArrayList<String>();
    private List<String> vuosiluokkaList = new ArrayList<String>();
    private Integer limit = 0;

    public List<String> getKieliList() {
        return kieliList;
    }

    public void setKieliList(List<String> kieliList) {
        this.kieliList = kieliList;
    }

    public List<String> getKuntaList() {
        return kuntaList;
    }

    public void setKuntaList(List<String> kuntaList) {
        this.kuntaList = kuntaList;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<String> getOppilaitostyyppiList() {
        return oppilaitostyyppiList;
    }

    public void setOppilaitostyyppiList(List<String> oppilaitostyyppiList) {
        this.oppilaitostyyppiList = oppilaitostyyppiList;
    }

    public List<String> getVuosiluokkaList() {
        return vuosiluokkaList;
    }

    public void setVuosiluokkaList(List<String> vuosiluokkaList) {
        this.vuosiluokkaList = vuosiluokkaList;
    }
}
