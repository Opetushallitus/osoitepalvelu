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

/**
 * User: ratamaa
 * Date: 4/9/14
 * Time: 5:22 PM
 */
public class AituJasenyysDto implements Serializable {
    private boolean voimassa;
    private String etunimi;
    private String sukunimi;
    private String sahkoposti;
    private String osoite;
    private String postinumero; // free text, basically 5 numbers (not koodisto value)
    private String postitoimipaikka; // free text (not koodisto value)
    private String aidinkieli; // short lower case language code, e.g. "fi", but may be also 2k (kaksikielinen = fi/sv)
    private String rooli; // AITU's rooli
    private String edustus;

    public boolean isVoimassa() {
        return voimassa;
    }

    public void setVoimassa(boolean voimassa) {
        this.voimassa = voimassa;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero = postinumero;
    }

    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka = postitoimipaikka;
    }

    public String getAidinkieli() {
        return aidinkieli;
    }

    public void setAidinkieli(String aidinkieli) {
        this.aidinkieli = aidinkieli;
    }

    public String getRooli() {
        return rooli;
    }

    public void setRooli(String rooli) {
        this.rooli = rooli;
    }

    public String getEdustus() {
        return edustus;
    }

    public void setEdustus(String edustus) {
        this.edustus = edustus;
    }

    public String getKokoNimi() {
        return this.etunimi + " " + this.sukunimi;
    }
}
