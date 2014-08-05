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

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonIgnore-annotation is applied to the getters of properties that are not present in the organisaatio-service's
 * yhteystietohaku remote interface's search criteria's JSON.
 *
 * User: ratamaa
 * Date: 3/14/14
 * Time: 2:08 PM
 */
public class OrganisaatioYhteystietoCriteriaDto implements Serializable {
    private static final long serialVersionUID = 9152021492358122085L;
    private static final int HASH_FACTOR = 31;

    public static final int HIGH_LIMIT_VALUE = 999999999;

    private List<String> kuntaList  =  new ArrayList<String>();
    private List<String> kieliList  =  new ArrayList<String>();
    private List<String> oppilaitostyyppiList  =  new ArrayList<String>();
    private List<String> vuosiluokkaList  =  new ArrayList<String>();
    private List<String> ytunnusList  =  new ArrayList<String>();
    private List<String> organisaatioTyyppis = new ArrayList<String>();
    private Integer limit  = HIGH_LIMIT_VALUE; // Integer.MAX_VALUE seems to result in 500 error on the remote end
    private boolean vainAktiiviset = true;
    private boolean useYtunnus=true;
    private boolean useOppilaitotyyppi=true;
    private boolean useKieli=true;
    private boolean useKunta=true;
    private boolean useVuosiluokka=true;
    private boolean useOrganisaatioTyyppi=true;

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

    @JsonIgnore
    public List<String> getOrganisaatioTyyppis() {
        return organisaatioTyyppis;
    }

    public void setOrganisaatioTyyppis(List<String> organisaatioTyyppis) {
        this.organisaatioTyyppis = organisaatioTyyppis;
    }

    @JsonIgnore
    public boolean isOpetusKieliUsed() {
        return useKieli && this.kieliList != null && !this.kieliList.isEmpty();
    }

    @JsonIgnore
    public boolean isKuntaUsed() {
        return useKunta && this.kuntaList != null && !this.kuntaList.isEmpty();
    }

    @JsonIgnore
    public boolean isVuosiluokkaUsed() {
        return useVuosiluokka && this.vuosiluokkaList != null && !this.vuosiluokkaList.isEmpty();
    }

    @JsonIgnore
    public boolean isOppilaitostyyppiUsed() {
        return useOppilaitotyyppi && this.oppilaitostyyppiList != null && !this.oppilaitostyyppiList.isEmpty();
    }

    @JsonIgnore
    public boolean isOrganisaatioTyyppiUsed() {
        return useOrganisaatioTyyppi && this.organisaatioTyyppis != null && !this.organisaatioTyyppis.isEmpty();
    }

    @JsonIgnore
    public boolean isYtunnusUsed() {
        return useYtunnus && this.ytunnusList != null && !this.ytunnusList.isEmpty();
    }

    public void setUseYtunnus(boolean useYtunnus) {
        this.useYtunnus = useYtunnus;
    }

    public void setUseOppilaitotyyppi(boolean useOppilaitotyyppi) {
        this.useOppilaitotyyppi = useOppilaitotyyppi;
    }

    public void setUseKieli(boolean useKieli) {
        this.useKieli = useKieli;
    }

    public void setUseKunta(boolean useKunta) {
        this.useKunta = useKunta;
    }

    public void setUseVuosiluokka(boolean useVuosiluokka) {
        this.useVuosiluokka = useVuosiluokka;
    }

    public void setUseOrganisaatioTyyppi(boolean useOrganisaatioTyyppi) {
        this.useOrganisaatioTyyppi = useOrganisaatioTyyppi;
    }

    @JsonIgnore
    public boolean isVainAktiiviset() {
        return vainAktiiviset;
    }

    public void setVainAktiiviset(boolean vainAktiiviset) {
        this.vainAktiiviset = vainAktiiviset;
    }

    public void useAll() {
        setUseYtunnus(true);
        setUseOppilaitotyyppi(true);
        setUseVuosiluokka(true);
        setUseKunta(true);
        setUseKieli(true);
        setUseOrganisaatioTyyppi(true);
    }

    @JsonIgnore
    public int getNumberOfUsedConditions() {
        return (isVuosiluokkaUsed() ? 1 : 0)
                + (isYtunnusUsed() ? 1 : 0)
                + (isOpetusKieliUsed() ? 1 : 0)
                + (isKuntaUsed() ? 1 : 0)
                + (isOppilaitostyyppiUsed() ? 1 : 0)
                + (isOrganisaatioTyyppiUsed() ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganisaatioYhteystietoCriteriaDto)) {
            return false;
        }

        OrganisaatioYhteystietoCriteriaDto that = (OrganisaatioYhteystietoCriteriaDto) o;

        if (vainAktiiviset != that.vainAktiiviset) {
            return false;
        }
        if (kieliList != null ? !kieliList.equals(that.kieliList) : that.kieliList != null) {
            return false;
        }
        if (kuntaList != null ? !kuntaList.equals(that.kuntaList) : that.kuntaList != null) {
            return false;
        }
        if (limit != null ? !limit.equals(that.limit) : that.limit != null) {
            return false;
        }
        if (oppilaitostyyppiList != null
                ? !oppilaitostyyppiList.equals(that.oppilaitostyyppiList) : that.oppilaitostyyppiList != null) {
            return false;
        }
        if (organisaatioTyyppis != null
                ? !organisaatioTyyppis.equals(that.organisaatioTyyppis) : that.organisaatioTyyppis != null) {
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
        result = HASH_FACTOR * result + (kieliList != null ? kieliList.hashCode() : 0);
        result = HASH_FACTOR * result + (oppilaitostyyppiList != null ? oppilaitostyyppiList.hashCode() : 0);
        result = HASH_FACTOR * result + (vuosiluokkaList != null ? vuosiluokkaList.hashCode() : 0);
        result = HASH_FACTOR * result + (ytunnusList != null ? ytunnusList.hashCode() : 0);
        result = HASH_FACTOR * result + (organisaatioTyyppis != null ? organisaatioTyyppis.hashCode() : 0);
        result = HASH_FACTOR * result + (limit != null ? limit.hashCode() : 0);
        result = HASH_FACTOR * result + (vainAktiiviset ? 1 : 0);
        return result;
    }
}
