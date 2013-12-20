package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.io.Serializable;
import java.util.TreeSet;

public class ConditionDto implements Serializable {
    
    private static final long serialVersionUID = 3474454227186986250L;

    private String operaattori;
    
    private TreeSet<OrganisaatioSearchValueDto> ehdot;

    public ConditionDto() {
        this.ehdot = new TreeSet<OrganisaatioSearchValueDto>();
    }
    
    public String getOperaattori() {
        return operaattori;
    }
    
    public void setOperaattori(String operaattori) {
        this.operaattori = operaattori;
    }

    public TreeSet<OrganisaatioSearchValueDto> getEhdot() {
        return ehdot;
    }
    
    public void addEhto(OrganisaatioSearchValueDto ehto) {
        this.ehdot.add(ehto);
    }
    
}
