package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.io.Serializable;

public class SearchValueDto implements Comparable<SearchValueDto>, Serializable{

    private static final long serialVersionUID = -5123398206835328711L;
    
    private String koodiArvo;
    

    public SearchValueDto() {
    }
    
    public SearchValueDto(String koodiArvo) {
        this.koodiArvo = koodiArvo;
    }
    
    public String getKoodiArvo() {
        return koodiArvo;
    }
    
    public void setKoodiArvo(String koodiArvo) {
        this.koodiArvo = koodiArvo;
    }

    @Override
    public int compareTo(SearchValueDto o) {
        if (o != null) {
            return ((SearchValueDto) o).getKoodiArvo().compareTo(this.getKoodiArvo());
        } else {
            return -1;
        }
    }
    
    
}
