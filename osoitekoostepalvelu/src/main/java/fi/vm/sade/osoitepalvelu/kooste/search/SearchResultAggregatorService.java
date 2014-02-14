package fi.vm.sade.osoitepalvelu.kooste.search;

import fi.vm.sade.osoitepalvelu.kooste.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.search.dto.SearchResultRowDto;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SearchResultAggregatorService {

    /**
     * @param results to be aggregated
     * @return the aggregated rows for presentation
     */
    List<SearchResultRowDto> aggregateResultRows( List<OrganisaatioResultDto> results );

}
