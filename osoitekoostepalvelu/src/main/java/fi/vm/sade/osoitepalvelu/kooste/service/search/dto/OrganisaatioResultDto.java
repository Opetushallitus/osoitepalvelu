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

package fi.vm.sade.osoitepalvelu.kooste.service.search.dto;

import fi.ratamaa.dtoconverter.annotation.DtoConversion;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganisaatioResultDto implements Serializable {
    private static final long serialVersionUID  =  -3235512233762738508L;
    
    private String oid; // Yksikäsitteinen tunniste
    private String kotikunta;
    private String toimipistekoodi;
    private String oppilaitosKoodi;
    private String ytunnus;
    private String yritysmuoto;
    private List<String> kieletUris;
    private String opetuskieli;
    private String wwwOsoite;
    private Map<String, String> nimi  =  new HashMap<String, String>(); // Organisaation nimi lokaalin mukaan
    private String puhelinnumero;
    private String emailOsoite;
    private List<String> tyypit;
    @DtoConversion
    private List<OsoitteistoDto> postiosoite  =  new ArrayList<OsoitteistoDto>();
    @DtoConversion
    private List<OsoitteistoDto> kayntiosoite  =  new ArrayList<OsoitteistoDto>();
    private List<OrganisaatioYhteystietoDto> yhteyshenkilot  =  new ArrayList<OrganisaatioYhteystietoDto>();
    private String viranomaistiedotuksenEmail;
    private String koulutusneuvonnanEmail;
    private String kriisitiedotuksenEmail;
    private String varhaiskasvatuksenYhteyshenkilo;
    private String varhaiskasvatuksenEmail;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid  =  oid;
    }

    public String getKotikunta() {
        return kotikunta;
    }

    public void setKotikunta(String kotikunta) {
        this.kotikunta  =  kotikunta;
    }

    public String getToimipistekoodi() {
        return toimipistekoodi;
    }

    public void setToimipistekoodi(String toimipistekoodi) {
        this.toimipistekoodi  =  toimipistekoodi;
    }

    public String getYtunnus() {
        return ytunnus;
    }

    public void setYtunnus(String ytunnus) {
        this.ytunnus = ytunnus;
    }

    public String getYritysmuoto() {
        return yritysmuoto;
    }

    public void setYritysmuoto(String yritysmuoto) {
        this.yritysmuoto = yritysmuoto;
    }

    public List<String> getKieletUris() {
        return kieletUris;
    }

    public void setKieletUris(List<String> kieletUris) {
        this.kieletUris = kieletUris;
    }

    public String getOpetuskieli() {
        return opetuskieli;
    }

    public void setOpetuskieli(String opetuskieli) {
        this.opetuskieli = opetuskieli;
    }

    public String getWwwOsoite() {
        return wwwOsoite;
    }

    public void setWwwOsoite(String wwwOsoite) {
        this.wwwOsoite  =  wwwOsoite;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi  =  nimi;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero  =  puhelinnumero;
    }

    public String getEmailOsoite() {
        return emailOsoite;
    }

    public void setEmailOsoite(String emailOsoite) {
        this.emailOsoite  =  emailOsoite;
    }
    
    public List<String> getTyypit() {
        return tyypit;
    }
    
    public void setTyypit(List<String> tyypit) {
        this.tyypit  =  tyypit;
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
    
    public List<OrganisaatioYhteystietoDto> getYhteyshenkilot() {
        return yhteyshenkilot;
    }
    
    public void setYhteyshenkilot(List<OrganisaatioYhteystietoDto> yhteyshenkilot) {
        this.yhteyshenkilot  =  yhteyshenkilot;
    }
    
    public void addYhteyshenkilo(OrganisaatioYhteystietoDto yhteyshenkilo) {
        this.yhteyshenkilot.add(yhteyshenkilo);
    }

    public String getViranomaistiedotuksenEmail() {
        return viranomaistiedotuksenEmail;
    }

    public void setViranomaistiedotuksenEmail(String viranomaistiedotuksenEmail) {
        this.viranomaistiedotuksenEmail  =  viranomaistiedotuksenEmail;
    }

    public String getKoulutusneuvonnanEmail() {
        return koulutusneuvonnanEmail;
    }

    public void setKoulutusneuvonnanEmail(String koulutusneuvonnanEmail) {
        this.koulutusneuvonnanEmail  =  koulutusneuvonnanEmail;
    }

    public String getKriisitiedotuksenEmail() {
        return kriisitiedotuksenEmail;
    }

    public void setKriisitiedotuksenEmail(String kriisitiedotuksenEmail) {
        this.kriisitiedotuksenEmail = kriisitiedotuksenEmail;
    }

    public String getVarhaiskasvatuksenYhteyshenkilo() {
        return varhaiskasvatuksenYhteyshenkilo;
    }

    public void setVarhaiskasvatuksenYhteyshenkilo(String varhaiskasvatuksenYhteyshenkilo) {
        this.varhaiskasvatuksenYhteyshenkilo = varhaiskasvatuksenYhteyshenkilo;
    }

    public String getVarhaiskasvatuksenEmail() {
        return varhaiskasvatuksenEmail;
    }

    public void setVarhaiskasvatuksenEmail(String varhaiskasvatuksenEmail) {
        this.varhaiskasvatuksenEmail = varhaiskasvatuksenEmail;
    }

    public String getOppilaitosKoodi() {
        return oppilaitosKoodi;
    }

    public void setOppilaitosKoodi(String oppilaitosKoodi) {
        this.oppilaitosKoodi  =  oppilaitosKoodi;
    }
}
