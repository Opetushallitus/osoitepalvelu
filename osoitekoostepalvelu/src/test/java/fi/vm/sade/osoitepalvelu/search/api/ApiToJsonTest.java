package fi.vm.sade.osoitepalvelu.search.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fi.vm.sade.osoitepalvelu.kooste.search.api.KayttajahakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.KayttajahakuResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OrganisaatioResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OsoisteDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.SearchKeyDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.SearchQueryDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.SearchValueDto;
import fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto.KoodistoDto.KoodistoTyyppi;

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
        result.setNimi(nimi );
        result.setTyypit(tyypit );
        result.setEmailOsoite("organisaatio2@email.com");
        result.setFaksinumero("09123123");
        result.setKotikunta("Helsinki");
        result.setOid("1.2.246.562.10.38898719687");
        result.setPuhelinnumero("0955544412");
        result.setToimipistekoodi("0032901");
        result.setWwwOsoite("http://www.kimitoon.fi/barn-och-utbildning/skolor/svenskaskolor/kimitoonsgymnasi");
        
        OsoisteDto kayntiosoite = new OsoisteDto();
        kayntiosoite.setOsoite("Mannerheiminkatu 48B");
        kayntiosoite.setOsoiteTyyppi("kaynti");
        kayntiosoite.setPostinumero("00100");
        kayntiosoite.setPostitoimipaikka("Helsinki");
        kayntiosoite.setYhteystietoOid("1.2.246.562.5.95913959722");
        result.setKayntiosoite(kayntiosoite);
        
        OsoisteDto postiosoite = new OsoisteDto();
        postiosoite.setOsoite("PL 231");
        postiosoite.setOsoiteTyyppi("posti");
        postiosoite.setPostinumero("00101");
        postiosoite.setPostitoimipaikka("Helsinki");
        postiosoite.setExtraRivi("Ostolaskut");
        postiosoite.setYhteystietoOid("1.2.246.562.5.140081871810");
        result.setPostiosoite(postiosoite);
        
        results.addResult(result);
        
        result = new OrganisaatioResultDto();
        tyypit = new ArrayList<String>();
        tyypit.add("Toimipiste");
        result.setTyypit(tyypit );
        nimi = new HashMap<String, String>();
        nimi.put("sv", "Högskola");
        nimi.put("fi", "Tre Korkeakoulu");
        result.setNimi(nimi );
        result.setEmailOsoite("esimerkki@email.com");
        result.setFaksinumero("03123123");
        result.setKotikunta("Tampere");
        result.setOid("1.2.246.562.10.388987196872");
        result.setPuhelinnumero("03555666000");
        result.setToimipistekoodi("0032902");
        result.setWwwOsoite("http://www.organisaatio.ab");
        
        kayntiosoite = new OsoisteDto();
        kayntiosoite.setOsoite("Yliopistonkatu 58 B");
        kayntiosoite.setOsoiteTyyppi("kaynti");
        kayntiosoite.setPostinumero("33100");
        kayntiosoite.setPostitoimipaikka("Tampere");
        kayntiosoite.setYhteystietoOid("1.2.246.562.5.95913959724");
        result.setKayntiosoite(kayntiosoite);
        
        postiosoite = new OsoisteDto();
        postiosoite.setOsoite("PL 231");
        postiosoite.setOsoiteTyyppi("posti");
        postiosoite.setPostinumero("33101");
        postiosoite.setPostitoimipaikka("Tampere");
        postiosoite.setExtraRivi("Postit paperiset");
        postiosoite.setYhteystietoOid("1.2.246.562.5.140081871815");
        result.setPostiosoite(postiosoite);
        
        results.addResult(result);
        
        printJson(results);
        
    }

    
    @Test
    public void generateKayttajahakuResultsJsonExample() {
        KayttajahakuResultsDto results = new KayttajahakuResultsDto();
        
        KayttajahakuResultDto result = new KayttajahakuResultDto();
        result.setEmail("olli.opettaja@opetus.fi");
        result.setEtunimi("Olli");
        result.setSukunimi("Opettaja");
        result.setOrganisaatioOid("1.2.246.562.10.388987196872");
        result.setOid("1.2.3.4.1.4");
        
        results.addResult(result);
        
        result = new KayttajahakuResultDto();
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
