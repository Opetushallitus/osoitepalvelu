package fi.vm.sade.osoitepalvelu.kooste.route.dto;

import java.util.List;

public class OppijanumerorekisteriSlice<T> {

    private List<T> results;

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

}
