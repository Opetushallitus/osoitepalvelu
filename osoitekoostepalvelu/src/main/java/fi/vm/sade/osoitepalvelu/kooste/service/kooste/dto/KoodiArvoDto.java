package fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto;

public class KoodiArvoDto {
    private String nimi;
    private String kuvaus;
    private String lyhytNimi;
    private String kieli;

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

    public String getKieli() {
        return kieli;
    }

    public void setKieli(String kieli) {
        this.kieli = kieli;
    }

    @Override
    public String toString() {
        return this.nimi;
    }
}
