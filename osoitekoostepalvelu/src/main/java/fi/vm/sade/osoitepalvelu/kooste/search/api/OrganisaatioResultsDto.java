package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.util.ArrayList;
import java.util.List;

public class OrganisaatioResultsDto {

    private List<OrganisaatioResultDto> tulos;
    
    
    public OrganisaatioResultsDto() {
        tulos = new ArrayList<OrganisaatioResultDto>();
    }
    
    public List<OrganisaatioResultDto> getResults() {
        return tulos;
    }
    
    public void setResults(List<OrganisaatioResultDto> results) {
        this.tulos = results;
    }
    
    public void addResult( OrganisaatioResultDto result ) {
        this.tulos.add(result);
    }
    
}
