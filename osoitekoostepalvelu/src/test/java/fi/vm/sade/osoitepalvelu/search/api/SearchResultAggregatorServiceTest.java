package fi.vm.sade.osoitepalvelu.search.api;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 2/14/14
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */

import fi.vm.sade.osoitepalvelu.SpringTestAppConfig;
import fi.vm.sade.osoitepalvelu.kooste.search.SearchResultAggregatorService;
import fi.vm.sade.osoitepalvelu.kooste.search.api.KayttajahakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OsoitteistoDto;
import fi.vm.sade.osoitepalvelu.kooste.search.dto.SearchResultRowDto;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SpringTestAppConfig.class})
public class SearchResultAggregatorServiceTest {

    @Autowired
    private SearchResultAggregatorService aggregatorService;

    @Test
    public void testAggregateOrganisaatiotWithYhteyshenkilos() {
        OrganisaatioResultDto organisaatio1 = new OrganisaatioResultDto(),
                organisaatio2 = new OrganisaatioResultDto();
        organisaatio1.setOid("org1");
        organisaatio2.setOid("org2");
        organisaatio2.setEmailOsoite("org email");
        organisaatio2.setFaksinumero("fax");
        organisaatio2.setPuhelinnumero("org puh");
        organisaatio2.setKotikunta("kotikunta");
        organisaatio2.setNimi(new HashMap<String, String>());
        organisaatio2.getNimi().put("fi", "suomi");
        organisaatio2.getNimi().put("sv", "svenska");
        organisaatio2.setToimipistekoodi("toimipiste");
        organisaatio2.setTyypit(new ArrayList<String>(Arrays.asList("Tyyppi1", "Tyyppi2")));
        organisaatio2.setWwwOsoite("www");
        KayttajahakuResultDto yhteyshenkilo1 = new KayttajahakuResultDto(),
                yhteyshenkilo2 = new KayttajahakuResultDto();
        yhteyshenkilo1.setOid("henk1");
        yhteyshenkilo1.setEmail("email");
        yhteyshenkilo1.setEtunimi("Etu");
        yhteyshenkilo1.setSukunimi("Suku");
        yhteyshenkilo1.setRoolit(new HashSet<String>(Arrays.asList("A", "B")));
        yhteyshenkilo2.setOid("henk2");
        yhteyshenkilo2.setSukunimi("Suku2");
        organisaatio1.getYhteyshenkilöt().add(yhteyshenkilo1);
        organisaatio1.getYhteyshenkilöt().add(yhteyshenkilo2);

        List<OrganisaatioResultDto> list = Arrays.asList(organisaatio1, organisaatio2);
        List<SearchResultRowDto> rows = aggregatorService.aggregateResultRows(list);
        assertEquals(3, rows.size());
        assertEquals("org1", rows.get(0).getOrganisaatioOid());
        assertEquals("henk1", rows.get(0).getHenkiloOid());
        assertEquals("email", rows.get(0).getEmail());
        assertEquals("Etu", rows.get(0).getEtunimi());
        assertEquals("Suku", rows.get(0).getSukunimi());
        assertEquals("org1", rows.get(1).getOrganisaatioOid());
        assertEquals("Suku2", rows.get(1).getSukunimi());
        assertEquals("org2", rows.get(2).getOrganisaatioOid());
        assertEquals("org email", rows.get(2).getEmailOsoite());
        assertEquals("org puh", rows.get(2).getPuhelinnumero());
        assertEquals("kotikunta", rows.get(2).getKotikunta());
        assertEquals("toimipiste", rows.get(2).getToimipistekoodi());
        assertEquals(2, rows.get(2).getTyypit().size());
        assertEquals(2, rows.get(2).getNimi().size());
        assertNull(rows.get(2).getHenkiloOid());
    }


    @Test
    public void testAggregateOrganisaatiotWithYhteyshenkiloAndPostiosoites() {
        OrganisaatioResultDto organisaatio1 = new OrganisaatioResultDto(),
                organisaatio2 = new OrganisaatioResultDto();
        organisaatio1.setOid("org1");
        organisaatio2.setOid("org2");
        KayttajahakuResultDto yhteyshenkilo1 = new KayttajahakuResultDto(),
                yhteyshenkilo2 = new KayttajahakuResultDto();
        yhteyshenkilo1.setOid("henk1");
        yhteyshenkilo2.setOid("henk2");
        organisaatio1.getYhteyshenkilöt().add(yhteyshenkilo1);
        organisaatio1.getYhteyshenkilöt().add(yhteyshenkilo2);

        OsoitteistoDto osoite1 = new OsoitteistoDto(),
                osoite2 = new OsoitteistoDto();
        osoite1.setYhteystietoOid("yht1");
        osoite1.setExtraRivi("extra");
        osoite1.setKieli("sv");
        osoite1.setOsoiteTyyppi("osoitetyyppi");
        osoite1.setOsoite("Rantatie 1");
        osoite1.setPostinumero("12345");
        osoite1.setPostitoimipaikka("TOIJALA");
        osoite2.setYhteystietoOid("yht2");
        organisaatio1.getPostiosoite().add(osoite1);
        organisaatio1.getPostiosoite().add(osoite2);
        organisaatio2.getPostiosoite().add(osoite1);
        organisaatio2.getPostiosoite().add(osoite2);

        List<OrganisaatioResultDto> list = Arrays.asList(organisaatio1, organisaatio2);
        List<SearchResultRowDto> rows = aggregatorService.aggregateResultRows(list);
        assertEquals(6, rows.size());
        assertEquals("org1", rows.get(0).getOrganisaatioOid());
        assertEquals("org1", rows.get(3).getOrganisaatioOid());
        assertEquals("yht1", rows.get(0).getYhteystietoOid());
        assertEquals("extra", rows.get(0).getExtraRivi());
        assertEquals("sv", rows.get(0).getOsoiteKieli());
        assertEquals("osoitetyyppi", rows.get(0).getOsoiteTyyppi());
        assertEquals("Rantatie 1", rows.get(0).getOsoite());
        assertEquals("12345", rows.get(0).getPostinumero());
        assertEquals("TOIJALA", rows.get(0).getPostitoimipaikka());
        assertEquals("yht1", rows.get(1).getYhteystietoOid());
        assertEquals("yht2", rows.get(2).getYhteystietoOid());
        assertEquals("yht2", rows.get(3).getYhteystietoOid());
        assertEquals("henk1", rows.get(0).getHenkiloOid());
        assertEquals("henk2", rows.get(1).getHenkiloOid());
        assertEquals("henk1", rows.get(2).getHenkiloOid());
        assertEquals("henk2", rows.get(3).getHenkiloOid());
        assertEquals("org2", rows.get(4).getOrganisaatioOid());
        assertEquals("yht1", rows.get(4).getYhteystietoOid());
        assertEquals("org2", rows.get(5).getOrganisaatioOid());
        assertEquals("yht2", rows.get(5).getYhteystietoOid());
        assertNull(rows.get(5).getHenkiloOid());
    }

}
