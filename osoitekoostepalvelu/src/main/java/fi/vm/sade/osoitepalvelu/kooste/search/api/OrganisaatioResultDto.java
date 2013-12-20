package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.util.HashMap;
import java.util.List;

public class OrganisaatioResultDto {

    private String oid; // Yksikäsitteinen tunniste
    private String kotikunta;
    private String toimipistekoodi;
    private String wwwOsoite;
    private HashMap<String, String> nimi; // Organisaation nimi lokaalin mukaan
    private String puhelinnumero;
    private String faksinumero;
    private String emailOsoite;
    private List<String> tyypit;
    private OsoisteDto postiosoite;
    private OsoisteDto kayntiosoite;


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

    public OsoisteDto getPostiosoite() {
        return postiosoite;
    }

    public void setPostiosoite(OsoisteDto postiosoite) {
        this.postiosoite = postiosoite;
    }
    
    public OsoisteDto getKayntiosoite() {
        return kayntiosoite;
    }
    
    public void setKayntiosoite(OsoisteDto kaytiosoite) {
        this.kayntiosoite = kaytiosoite;
    }

}
