package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.io.Serializable;

public class OrganisaatioSearchValueDto implements Comparable<OrganisaatioSearchValueDto>, Serializable{

    private static final long serialVersionUID = -5123398206835328711L;
    
    private String koodiArvo;
    

    public OrganisaatioSearchValueDto() {
    }
    
    public OrganisaatioSearchValueDto(String koodiArvo) {
        this.koodiArvo = koodiArvo;
    }
    
    public String getKoodiArvo() {
        return koodiArvo;
    }
    
    public void setKoodiArvo(String koodiArvo) {
        this.koodiArvo = koodiArvo;
    }

    @Override
    public int compareTo(OrganisaatioSearchValueDto o) {
        if( o != null ) {
            return ((OrganisaatioSearchValueDto)o).getKoodiArvo().compareTo(this.getKoodiArvo());
        } else {
            return -1;
        }
    }
    
    
}
