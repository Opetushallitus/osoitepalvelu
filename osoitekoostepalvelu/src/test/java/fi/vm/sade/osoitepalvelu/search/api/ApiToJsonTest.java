package fi.vm.sade.osoitepalvelu.search.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fi.vm.sade.osoitepalvelu.kooste.search.api.*;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(JUnit4.class)
public class ApiToJsonTest {

    @Test
    public void generateSearchJsonExample() {
        SearchQueryDto haku = new SearchQueryDto();

        haku.addOrCriteria(new SearchKeyDto(KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI.toString(), "TAI"),
                new SearchValueDto("1"));
        haku.addOrCriteria(new SearchKeyDto(KoodistoTyyppi.OPPILAITOKSEN_OPETUSKIELI.toString(), "TAI"),
                new SearchValueDto("2"));

        haku.addOrCriteria(new SearchKeyDto(KoodistoTyyppi.KUNTA.toString(), "TAI"),
                new SearchValueDto("020"));
        haku.addOrCriteria(new SearchKeyDto(KoodistoTyyppi.KUNTA.toString(), "TAI"),
                new SearchValueDto("010"));
        
        haku.addAndCriteria(new SearchKeyDto(KoodistoTyyppi.OPPILAITOSTYYPPI.toString(), "TAI"),
                new SearchValueDto("64"));
        haku.addAndCriteria(new SearchKeyDto(KoodistoTyyppi.OPPILAITOSTYYPPI.toString(), "TAI"),
                new SearchValueDto("15"));

        printJson(haku);

    }
    
    @Test
    public void generateResultsJsonExample() {
        OrganisaatioResultsDto results = new OrganisaatioResultsDto();
        
        OrganisaatioResultDto result = new OrganisaatioResultDto();
        List<String> tyypit = new ArrayList<String>();
        tyypit.add("Opetuspiste");
        HashMap<String, String> nimi = new HashMap<String, String>();
        nimi.put("sv", "Svenska text");
        nimi.put("fi", "Suomeksi nimi");
        result.setNimi(nimi);
        result.setTyypit(tyypit);
        result.setEmailOsoite("organisaatio2@email.com");
        result.setFaksinumero("09123123");
        result.setKotikunta("Helsinki");
        result.setOid("1.2.246.562.10.38898719687");
        result.setPuhelinnumero("0955544412");
        result.setToimipistekoodi("0032901");
        result.setWwwOsoite("http://www.kimitoon.fi/barn-och-utbildning/skolor/svenskaskolor/kimitoonsgymnasi");
        
        OsoitteistoDto kayntiosoite = new OsoitteistoDto();
        kayntiosoite.setKieli("fi");
        kayntiosoite.setOsoite("Mannerheiminkatu 48B");
        kayntiosoite.setOsoiteTyyppi("kaynti");
        kayntiosoite.setPostinumero("00100");
        kayntiosoite.setPostitoimipaikka("Helsinki");
        kayntiosoite.setYhteystietoOid("1.2.246.562.5.95913959722");
        result.addKayntiosoite(kayntiosoite);
        
        OsoitteistoDto postiosoite = new OsoitteistoDto();
        postiosoite.setKieli("fi");
        postiosoite.setOsoite("PL 231");
        postiosoite.setOsoiteTyyppi("posti");
        postiosoite.setPostinumero("00101");
        postiosoite.setPostitoimipaikka("Helsinki");
        postiosoite.setExtraRivi("Ostolaskut");
        postiosoite.setYhteystietoOid("1.2.246.562.5.140081871810");
        result.addPostiosoite(postiosoite);
        
        KayttajahakuResultDto yhteyshenkilo = new KayttajahakuResultDto();
        yhteyshenkilo.setEmail("ylli.yhteyshenkilo@email.fi");
        yhteyshenkilo.setEtunimi("Ylli");
        yhteyshenkilo.setSukunimi("Yhteyshenkilö");
        yhteyshenkilo.setOrganisaatioOid(result.getOid());
        yhteyshenkilo.addRooli("Yhteyshenkilö");
        yhteyshenkilo.setOid("1.2.3.4.1.4.3");
        result.addYhteyshenkilö(yhteyshenkilo);
        
        results.addResult(result);
        
        result = new OrganisaatioResultDto();
        tyypit = new ArrayList<String>();
        tyypit.add("Toimipiste");
        result.setTyypit(tyypit);
        nimi = new HashMap<String, String>();
        nimi.put("sv", "Högskola");
        nimi.put("fi", "Tre Korkeakoulu");
        result.setNimi(nimi);
        result.setEmailOsoite("esimerkki@email.com");
        result.setFaksinumero("03123123");
        result.setKotikunta("Tampere");
        result.setOid("1.2.246.562.10.388987196872");
        result.setPuhelinnumero("03555666000");
        result.setToimipistekoodi("0032902");
        result.setWwwOsoite("http://www.organisaatio.ab");
        
        kayntiosoite = new OsoitteistoDto();
        kayntiosoite.setKieli("fi");
        kayntiosoite.setOsoite("Yliopistonkatu 58 B");
        kayntiosoite.setOsoiteTyyppi("kaynti");
        kayntiosoite.setPostinumero("33100");
        kayntiosoite.setPostitoimipaikka("Tampere");
        kayntiosoite.setYhteystietoOid("1.2.246.562.5.95913959724");
        result.addKayntiosoite(kayntiosoite);
        
        postiosoite = new OsoitteistoDto();
        postiosoite.setKieli("fi");
        postiosoite.setOsoite("PL 231");
        postiosoite.setOsoiteTyyppi("posti");
        postiosoite.setPostinumero("33101");
        postiosoite.setPostitoimipaikka("Tampere");
        postiosoite.setExtraRivi("Postit paperiset");
        postiosoite.setYhteystietoOid("1.2.246.562.5.140081871815");
        result.addPostiosoite(postiosoite);
        
        yhteyshenkilo = new KayttajahakuResultDto();
        yhteyshenkilo.setEmail("rehtori.reijo@opetus.fi");
        yhteyshenkilo.setEtunimi("Reijo");
        yhteyshenkilo.setSukunimi("Rehtori");
        yhteyshenkilo.setOrganisaatioOid(result.getOid());
        yhteyshenkilo.addRooli("Rehtori");
        yhteyshenkilo.setOid("1.2.3.4.1.4.2");
        result.addYhteyshenkilö(yhteyshenkilo);
        
        yhteyshenkilo = new KayttajahakuResultDto();
        yhteyshenkilo.setEmail("apuri.pertti@opetus.fi");
        yhteyshenkilo.setEtunimi("Pertti");
        yhteyshenkilo.setSukunimi("Apuri");
        yhteyshenkilo.setOrganisaatioOid(result.getOid());
        yhteyshenkilo.addRooli("Apulaisrehtori");
        yhteyshenkilo.setOid("1.2.3.4.1.4.5");
        result.addYhteyshenkilö(yhteyshenkilo);
        
        results.addResult(result);
        
        printJson(results);
    }

    
    @Test
    public void generateKayttajahakuResultsJsonExample() {
        KayttajahakuResultsDto results = new KayttajahakuResultsDto();
        
        KayttajahakuResultDto result = new KayttajahakuResultDto();
        result.addRooli("Opettaja");
        result.addRooli("Opo");
        result.setEmail("olli.opettaja@opetus.fi");
        result.setEtunimi("Olli");
        result.setSukunimi("Opettaja");
        result.setOrganisaatioOid("1.2.246.562.10.388987196872");
        result.setOid("1.2.3.4.1.4");
        
        results.addResult(result);
        
        result = new KayttajahakuResultDto();
        result.addRooli("Lehtori");
        result.setEmail("pertti.pera@koulu.fi");
        result.setEtunimi("pertti");
        result.setSukunimi("pera");
        result.setOrganisaatioOid("1.2.246.562.10.38898719687");
        result.setOid("1.2.3.4.1.5");
        
        results.addResult(result);
        
        printJson(results);
        
    }
    
    
    private void printJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getSerializationConfig().without(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        try {
            System.out.println(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
