package fi.vm.sade.osoitepalvelu.kooste.domain;

import fi.ratamaa.dtoconverter.annotation.DtoConverted;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/17/13
 * Time: 9:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class KoodiItem implements Serializable {
    @DtoConverted
    private KoodistoCache.KoodistoTyyppi koodistonTyyppi;
    private String koodiId;
    private String nimi;
    private String kuvaus;
    private String lyhytNimi;

    public String getKoodiId() {
        return koodiId;
    }

    public void setKoodiId(String koodiId) {
        this.koodiId = koodiId;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getLyhytNimi() {
        return lyhytNimi;
    }

    public void setLyhytNimi(String lyhytNimi) {
        this.lyhytNimi = lyhytNimi;
    }

    public KoodistoCache.KoodistoTyyppi getKoodistonTyyppi() {
        return koodistonTyyppi;
    }

    public void setKoodistonTyyppi(KoodistoCache.KoodistoTyyppi koodistonTyyppi) {
        this.koodistonTyyppi = koodistonTyyppi;
    }
}