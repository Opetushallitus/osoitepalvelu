package fi.vm.sade.osoitepalvelu.kooste.search.api;

import java.util.ArrayList;
import java.util.List;

public class KayttajahakuResultsDto {

    private List<KayttajahakuResultDto> results;
    
    public KayttajahakuResultsDto() {
        results = new ArrayList<KayttajahakuResultDto>();
    }
    
    public List<KayttajahakuResultDto> getResults() {
        return results;
    }
    
    public void setResults(List<KayttajahakuResultDto> results) {
        this.results = results;
    }
    
    public void addResult(KayttajahakuResultDto result) {
        this.results.add(result);
    }
    
}
