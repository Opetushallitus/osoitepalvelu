package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.util.List;

public class OrganisaatioResultsDto {

    private List<OrganisaatioResultDto> results;
    
    
    public List<OrganisaatioResultDto> getResults() {
        return results;
    }
    
    public void setResults(List<OrganisaatioResultDto> results) {
        this.results = results;
    }
}
