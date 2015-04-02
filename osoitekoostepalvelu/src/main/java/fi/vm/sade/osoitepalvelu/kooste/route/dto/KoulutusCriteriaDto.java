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
 * User: simok
 * Date: 3/23/15
 * Time: 3:29 PM
 */
public class KoulutusCriteriaDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int HASH_FACTOR = 43;

    private List<String> koulutustyyppis = new ArrayList<String>();
    private List<String> opetuskielet = new ArrayList<String>();
    private String koulutuslaji;
    private List<String> koulutuskoodis;
    private List<String> opintoalakoodis;
    private List<String> koulutusalakoodis;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KoulutusCriteriaDto that = (KoulutusCriteriaDto) o;

        if (getKoulutustyyppis() != null
                ? !koulutustyyppis.equals(that.koulutustyyppis) : that.getKoulutustyyppis() != null) {
            return false;
        }
        if (getOpetuskielet() != null ? !opetuskielet.equals(that.opetuskielet) : that.getOpetuskielet() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = HASH_FACTOR * hash + (this.koulutustyyppis != null ? this.koulutustyyppis.hashCode() : 0);
        hash = HASH_FACTOR * hash + (this.opetuskielet != null ? this.opetuskielet.hashCode() : 0);
        hash = HASH_FACTOR * hash + (this.koulutuslaji != null ? this.koulutuslaji.hashCode() : 0);
        return hash;
    }

    /**
     * @return the koulutustyyppis
     */
    public List<String> getKoulutustyyppis() {
        return koulutustyyppis;
    }

    /**
     * @param koulutustyyppis the koulutustyyppis to set
     */
    public void setKoulutustyyppis(List<String> koulutustyyppis) {
        this.koulutustyyppis = koulutustyyppis;
    }

    /**
     * @return the opetuskielet
     */
    public List<String> getOpetuskielet() {
        return opetuskielet;
    }

    /**
     * @param opetuskielet the opetuskielet to set
     */
    public void setOpetuskielet(List<String> opetuskielet) {
        this.opetuskielet = opetuskielet;
    }

    /**
     * @return the koulutuslaji
     */
    public String getKoulutuslaji() {
        return koulutuslaji;
    }

    /**
     * @param koulutuslaji the koulutuslaji to set
     */
    public void setKoulutuslaji(String koulutuslaji) {
        this.koulutuslaji = koulutuslaji;
    }

    /**
     * @return the koulutuskoodis
     */
    public List<String> getKoulutuskoodis() {
        return koulutuskoodis;
    }

    /**
     * @param koulutuskoodis the koulutuskoodis to set
     */
    public void setKoulutuskoodis(List<String> koulutuskoodis) {
        this.koulutuskoodis = koulutuskoodis;
    }

    /**
     * @return the opintoalakoodis
     */
    public List<String> getOpintoalakoodis() {
        return opintoalakoodis;
    }

    /**
     * @param opintoalakoodis the opintoalakoodis to set
     */
    public void setOpintoalakoodis(List<String> opintoalakoodis) {
        this.opintoalakoodis = opintoalakoodis;
    }

    /**
     * @return the koulutusalakoodis
     */
    public List<String> getKoulutusalakoodis() {
        return koulutusalakoodis;
    }

    /**
     * @param koulutusalakoodis the koulutusalakoodis to set
     */
    public void setKoulutusalakoodis(List<String> koulutusalakoodis) {
        this.koulutusalakoodis = koulutusalakoodis;
    }

    @JsonIgnore
    public boolean isKoulutustyyppisUsed() {
        return this.koulutustyyppis != null && !this.koulutustyyppis.isEmpty();
    }

    @JsonIgnore
    public boolean isOpetuskieletUsed() {
        return this.opetuskielet != null && !this.opetuskielet.isEmpty();
    }

    @JsonIgnore
    public boolean isKoulutuslajiUsed() {
        return this.koulutuslaji != null && !this.koulutuslaji.isEmpty();
    }

    @JsonIgnore
    public boolean isKoulutuskoodisUsed() {
        return this.koulutuskoodis != null && !this.koulutuskoodis.isEmpty();
    }

    @JsonIgnore
    public boolean isOpintoalakoodisUsed() {
        return this.opintoalakoodis != null && !this.opintoalakoodis.isEmpty();
    }

    @JsonIgnore
    public boolean isKoulutusalakoodisUsed() {
        return this.koulutusalakoodis != null && !this.koulutusalakoodis.isEmpty();
    }

    @JsonIgnore
    public int getNumberOfUsedConditions() {
        return (isKoulutustyyppisUsed() ? 1 : 0)
                + (isOpetuskieletUsed() ? 1 : 0)
                + (isKoulutuslajiUsed() ? 1 : 0)
                + (isKoulutuskoodisUsed() ? 1 : 0)
                + (isOpintoalakoodisUsed() ? 1 : 0)
                + (isKoulutusalakoodisUsed() ? 1 : 0);
    }
}
