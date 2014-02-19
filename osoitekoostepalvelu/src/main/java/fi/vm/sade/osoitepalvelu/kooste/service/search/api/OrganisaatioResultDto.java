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

package fi.vm.sade.osoitepalvelu.kooste.service.search.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrganisaatioResultDto implements Serializable {

    private String oid; // Yksikäsitteinen tunniste
    private String kotikunta;
    private String toimipistekoodi;
    private String wwwOsoite;
    private HashMap<String, String> nimi; // Organisaation nimi lokaalin mukaan
    private String puhelinnumero;
    private String faksinumero;
    private String emailOsoite;
    private List<String> tyypit;
    private List<OsoitteistoDto> postiosoite = new ArrayList<OsoitteistoDto>();
    private List<OsoitteistoDto> kayntiosoite = new ArrayList<OsoitteistoDto>();
    private List<KayttajahakuResultDto> yhteyshenkilöt = new ArrayList<KayttajahakuResultDto>();
    private String viranomaistiedotuksenEmail;
    private String koulutusneuvonnanEmail;
    private String kriisitiedotuksenEmail;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getKotikunta() {
        return kotikunta;
    }

    public void setKotikunta(String kotikunta) {
        this.kotikunta = kotikunta;
    }

    public String getToimipistekoodi() {
        return toimipistekoodi;
    }

    public void setToimipistekoodi(String toimipistekoodi) {
        this.toimipistekoodi = toimipistekoodi;
    }

    public String getWwwOsoite() {
        return wwwOsoite;
    }

    public void setWwwOsoite(String wwwOsoite) {
        this.wwwOsoite = wwwOsoite;
    }

    public HashMap<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(HashMap<String, String> nimi) {
        this.nimi = nimi;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public String getFaksinumero() {
        return faksinumero;
    }

    public void setFaksinumero(String faksinumero) {
        this.faksinumero = faksinumero;
    }

    public String getEmailOsoite() {
        return emailOsoite;
    }

    public void setEmailOsoite(String emailOsoite) {
        this.emailOsoite = emailOsoite;
    }
    
    public List<String> getTyypit() {
        return tyypit;
    }
    
    public void setTyypit(List<String> tyypit) {
        this.tyypit = tyypit;
    }

    public List<OsoitteistoDto> getPostiosoite() {
        return postiosoite;
    }
    
    public void addPostiosoite(OsoitteistoDto postiosoite) {
        this.postiosoite.add(postiosoite);
    }
    
    public List<OsoitteistoDto> getKayntiosoite() {
        return kayntiosoite;
    }

    public void addKayntiosoite(OsoitteistoDto kayntiosoite) {
        this.kayntiosoite.add(kayntiosoite);
    }
    
    public List<KayttajahakuResultDto> getYhteyshenkilöt() {
        return yhteyshenkilöt;
    }
    
    public void setYhteyshenkilöt(List<KayttajahakuResultDto> yhteyshenkilöt) {
        this.yhteyshenkilöt = yhteyshenkilöt;
    }
    
    public void addYhteyshenkilö(KayttajahakuResultDto yhteyshenkilo) {
        this.yhteyshenkilöt.add(yhteyshenkilo);
    }

    public String getViranomaistiedotuksenEmail() {
        return viranomaistiedotuksenEmail;
    }

    public void setViranomaistiedotuksenEmail(String viranomaistiedotuksenEmail) {
        this.viranomaistiedotuksenEmail = viranomaistiedotuksenEmail;
    }

    public String getKoulutusneuvonnanEmail() {
        return koulutusneuvonnanEmail;
    }

    public void setKoulutusneuvonnanEmail(String koulutusneuvonnanEmail) {
        this.koulutusneuvonnanEmail = koulutusneuvonnanEmail;
    }

    public String getKriisitiedotuksenEmail() {
        return kriisitiedotuksenEmail;
    }

    public void setKriisitiedotuksenEmail(String kriisitiedotuksenEmail) {
        this.kriisitiedotuksenEmail = kriisitiedotuksenEmail;
    }
}
