package fi.vm.sade.osoitepalvelu.kooste.route.dto.oiva;

import fi.vm.sade.osoitepalvelu.kooste.route.dto.BaseDto;
import org.joda.time.LocalDate;

import java.util.List;

public class KoulutuslupaDto extends BaseDto {

    private String jarjestajaYtunnus;
    private LocalDate alkupvm;
    private LocalDate loppupvm;
    private List<String> koulutukset; // koodisto "koulutus"

    public String getJarjestajaYtunnus() {
        return jarjestajaYtunnus;
    }

    public void setJarjestajaYtunnus(String jarjestajaYtunnus) {
        this.jarjestajaYtunnus = jarjestajaYtunnus;
    }

    public LocalDate getAlkupvm() {
        return alkupvm;
    }

    public void setAlkupvm(LocalDate alkupvm) {
        this.alkupvm = alkupvm;
    }

    public LocalDate getLoppupvm() {
        return loppupvm;
    }

    public void setLoppupvm(LocalDate loppupvm) {
        this.loppupvm = loppupvm;
    }

    public List<String> getKoulutukset() {
        return koulutukset;
    }

    public void setKoulutukset(List<String> koulutukset) {
        this.koulutukset = koulutukset;
    }

}
