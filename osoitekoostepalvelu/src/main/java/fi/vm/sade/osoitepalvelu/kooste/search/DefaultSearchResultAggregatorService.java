package fi.vm.sade.osoitepalvelu.kooste.search;

import fi.vm.sade.osoitepalvelu.kooste.search.api.KayttajahakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OsoitteistoDto;
import fi.vm.sade.osoitepalvelu.kooste.search.dto.ResultAggregateDto;
import fi.vm.sade.osoitepalvelu.kooste.search.dto.SearchResultRowDto;
import fi.vm.sade.osoitepalvelu.kooste.search.dto.converter.SearchResultDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DefaultSearchResultAggregatorService implements SearchResultAggregatorService {
    @Autowired
    private SearchResultDtoConverter dtoConverter;

    @Override
    public List<SearchResultRowDto> aggregateResultRows(List<OrganisaatioResultDto> results) {
        Set<ResultAggregateDto> aggregates = new LinkedHashSet<ResultAggregateDto>();
        for(OrganisaatioResultDto result : results) {
            for(OsoitteistoDto osoite : result.getPostiosoite()) {
                for(KayttajahakuResultDto kayttaja : result.getYhteyshenkilöt() ) {
                    aggregates.add(new ResultAggregateDto(result, kayttaja, osoite));
                }
                if(result.getYhteyshenkilöt().size() < 1) {
                    aggregates.add(new ResultAggregateDto(result, null, osoite));
                }
            }
            if( result.getPostiosoite().size() < 1 ) {
                for(KayttajahakuResultDto kayttaja : result.getYhteyshenkilöt() ) {
                    aggregates.add(new ResultAggregateDto(result, kayttaja, null));
                }
            }
            if( result.getPostiosoite().size() < 1 && result.getYhteyshenkilöt().size() < 1 ) {
                aggregates.add(new ResultAggregateDto(result, null, null));
            }
        }
        return dtoConverter.convert( aggregates, new ArrayList<SearchResultRowDto>(), SearchResultRowDto.class );
    }
}
