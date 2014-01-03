package fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class OrganisaatioDto {
    private static final String DEFAULT_TO_STRING_LANG = "fi";

    private OrganisaatioOid oid; // Organisaation oid (Yksikäsitteinen tunniste)
    private String kotipaikkaUri;
    private String toimipistekoodi;
    private String wwwOsoite;
    private HashMap<String, String> nimi; // Organisaation nimi lokaalin mukaan
    private String puhelinnumero;
    private String faksinumero;
    private String emailOsoite;

    public String getKotipaikkaUri() {
        return kotipaikkaUri;
    }

    public void setKotipaikkaUri(String kotipaikkaUri) {
        this.kotipaikkaUri = kotipaikkaUri;
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

    public OrganisaatioOid getOid() {
        return oid;
    }

    public void setOid(OrganisaatioOid oid) {
        this.oid = oid;
    }

    public String getNimiLokaalille(Locale locale) {
        if (nimi != null) {
            String kieliAvain = locale.getLanguage().toLowerCase();
            return nimi.get(kieliAvain);
        }
        return null;
    }

    @Override
    public String toString() {
        if (nimi != null) {
            String value = nimi.get(DEFAULT_TO_STRING_LANG);
            if (value == null) {
                Iterator<String> i = nimi.values().iterator();
                if (i.hasNext()) {
                    return i.next();
                }
            } else {
                return value;
            }
        }
        return "";
    }
}
