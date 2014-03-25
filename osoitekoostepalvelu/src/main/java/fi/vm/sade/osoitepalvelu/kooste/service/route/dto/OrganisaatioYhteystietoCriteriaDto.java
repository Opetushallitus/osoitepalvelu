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
    private static final int HASH_FACTOR = 31;
    public static final int HIGH_LIMIT_VALUE = 999999999;

    private List<String> kuntaList  =  new ArrayList<String>();
    private List<String> kieliList  =  new ArrayList<String>();
    private List<String> oppilaitostyyppiList  =  new ArrayList<String>();
    private List<String> vuosiluokkaList  =  new ArrayList<String>();
    private List<String> ytunnusList  =  new ArrayList<String>();
    private Integer limit  = HIGH_LIMIT_VALUE; // Integer.MAX_VALUE seems to result in 500 error on the remote end

    public List<String> getKieliList() {
        return kieliList;
    }

    public void setKieliList(List<String> kieliList) {
        this.kieliList  =  kieliList;
    }

    public List<String> getKuntaList() {
        return kuntaList;
    }

    public void setKuntaList(List<String> kuntaList) {
        this.kuntaList  =  kuntaList;
    }

    public List<String> getOppilaitostyyppiList() {
        return oppilaitostyyppiList;
    }

    public void setOppilaitostyyppiList(List<String> oppilaitostyyppiList) {
        this.oppilaitostyyppiList  =  oppilaitostyyppiList;
    }

    public List<String> getVuosiluokkaList() {
        return vuosiluokkaList;
    }

    public void setVuosiluokkaList(List<String> vuosiluokkaList) {
        this.vuosiluokkaList  =  vuosiluokkaList;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit  =  limit;
    }

    public List<String> getYtunnusList() {
        return ytunnusList;
    }

    public void setYtunnusList(List<String> ytunnusList) {
        this.ytunnusList  =  ytunnusList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { 
            return true;
        }
        if (o == null || getClass() != o.getClass()) { 
            return false;
        }

        OrganisaatioYhteystietoCriteriaDto that  =  (OrganisaatioYhteystietoCriteriaDto) o;

        if (kieliList != null ? !kieliList.equals(that.kieliList) : that.kieliList != null) { 
            return false;
        }
        if (kuntaList != null ? !kuntaList.equals(that.kuntaList) : that.kuntaList != null) { 
            return false;
        }
        if (limit != null ? !limit.equals(that.limit) : that.limit != null) { 
            return false;
        }
        if (oppilaitostyyppiList != null ? !oppilaitostyyppiList.equals(that.oppilaitostyyppiList) : that.oppilaitostyyppiList != null) {
            return false;
        }
        if (vuosiluokkaList != null ? !vuosiluokkaList.equals(that.vuosiluokkaList) : that.vuosiluokkaList != null) {
            return false;
        }
        if (ytunnusList != null ? !ytunnusList.equals(that.ytunnusList) : that.ytunnusList != null) { 
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = kuntaList != null ? kuntaList.hashCode() : 0;
        result  =  HASH_FACTOR * result  +  (kieliList != null ? kieliList.hashCode() : 0);
        result  =  HASH_FACTOR * result  +  (oppilaitostyyppiList != null ? oppilaitostyyppiList.hashCode() : 0);
        result  =  HASH_FACTOR * result  +  (vuosiluokkaList != null ? vuosiluokkaList.hashCode() : 0);
        result  =  HASH_FACTOR * result  +  (ytunnusList != null ? ytunnusList.hashCode() : 0);
        result  =  HASH_FACTOR * result  +  (limit != null ? limit.hashCode() : 0);
        return result;
    }
}
