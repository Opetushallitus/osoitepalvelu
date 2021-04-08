package fi.vm.sade.osoitepalvelu.kooste.service.search;

import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.KayntiosoitteistoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OsoitteistoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import fi.vm.sade.osoitepalvelu.service.search.AllColumnsSearchResultPresentation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes  =  {SpringTestAppConfig.class})
public class DefaultSearchResultTransformerServiceTest {

    @Autowired
    DefaultSearchResultTransformerService transformer = new DefaultSearchResultTransformerService();

    @Test
    public void transformOrganisaatiosEmpty() {
        List<OrganisaatioResultDto> fixture = Collections.emptyList();
        List<SearchResultRowDto> result = transformer.transformOrganisaatios(fixture, new AllColumnsSearchResultPresentation());
        assertTrue(result.isEmpty());
    }

    @Test
    public void transformOrganisaatiosSingle() {
        List<OrganisaatioResultDto> fixture = Arrays.asList(new OrganisaatioResultDto());
        List<SearchResultRowDto> result = transformer.transformOrganisaatios(fixture, new AllColumnsSearchResultPresentation());
        assertFalse(result.isEmpty());
        assertNull(result.get(0).getPostiosoite());
        assertNull(result.get(0).getKayntiosoite());
    }

    @Test
    public void transformOrganisaatiosSingleWithPostiosoite() {
        OrganisaatioResultDto org = new OrganisaatioResultDto();
        org.getPostiosoite().add(new OsoitteistoDto());
        List<OrganisaatioResultDto> fixture = Arrays.asList(org);
        List<SearchResultRowDto> result = transformer.transformOrganisaatios(fixture, new AllColumnsSearchResultPresentation());
        assertFalse(result.isEmpty());
        assertNull(result.get(0).getKayntiosoite());
        assertNotNull(result.get(0).getPostiosoite());
    }

    @Test
    public void transformOrganisaatiosSingleWithKayntiosoite() {
        OrganisaatioResultDto org = new OrganisaatioResultDto();
        org.getKayntiosoite().add(new KayntiosoitteistoDto());
        List<OrganisaatioResultDto> fixture = Arrays.asList(org);
        List<SearchResultRowDto> result = transformer.transformOrganisaatios(fixture, new AllColumnsSearchResultPresentation());
        assertFalse(result.isEmpty());
        assertNull(result.get(0).getPostiosoite());
        assertNotNull(result.get(0).getKayntiosoite());
    }
}
