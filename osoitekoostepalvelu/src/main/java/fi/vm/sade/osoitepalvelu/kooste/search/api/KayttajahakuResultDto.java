package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.util.Set;
import java.util.TreeSet;

public class KayttajahakuResultDto {

    private Set<String> roolit;
    private String etunimi;
    private String sukunimi;
    private String email;
    private String oid;
    private String organisaatioOid;

    public Set<String> getRoolit() {
        return roolit;
    }
    
    public void setRoolit(Set<String> roolit) {
        this.roolit = roolit;
    }
    
    public void addRooli(String rooli) {
        if(this.roolit == null) {
            this.roolit = new TreeSet<String>();
        }
        
        this.roolit.add(rooli);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOrganisaatioOid() {
        return organisaatioOid;
    }

    public void setOrganisaatioOid(String organisaatioOid) {
        this.organisaatioOid = organisaatioOid;
    }

}
