package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.io.Serializable;

public class OrganisaatioSearchKeyDto implements Serializable, Comparable<OrganisaatioSearchKeyDto> {

    private static final long serialVersionUID = -7866357718772030725L;
    
    private String koodistoUri;
    
    private String operaattori;
    
    public OrganisaatioSearchKeyDto() {
    }
    
    public OrganisaatioSearchKeyDto(String koodistoUri, String operaattori) {
        this.koodistoUri = koodistoUri;
        this.operaattori = operaattori;
    }
    
    public String getKoodistoUri() {
        return koodistoUri;
    }
    
    public void setKoodistoUri(String koodistoUri) {
        this.koodistoUri = koodistoUri;
    }
    
    public String getOperaattori() {
        return operaattori;
    }
    
    public void setOperaattori(String operaattori) {
        this.operaattori = operaattori;
    }

    @Override
    public int compareTo(OrganisaatioSearchKeyDto o) {
        return koodistoUri.compareTo(o.getKoodistoUri());
    }
    
    @Override
    public boolean equals(Object obj) {
        if( obj != null && obj instanceof OrganisaatioSearchKeyDto ) {
            return (this.koodistoUri+this.operaattori).equals(
                    ((OrganisaatioSearchKeyDto)obj).getKoodistoUri()
                    + ((OrganisaatioSearchKeyDto)obj).getOperaattori());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return (this.koodistoUri+this.operaattori).hashCode();
    }
    
}
