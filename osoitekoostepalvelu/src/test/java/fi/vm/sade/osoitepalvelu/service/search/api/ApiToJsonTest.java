/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.osoitepalvelu.service.search.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fi.vm.sade.osoitepalvelu.kooste.service.search.api.*;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.KoodistoDto.KoodistoTyyppi;

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
        result.setOppilaitosKoodi("0032901");
        result.setWwwOsoite("http://www.kimitoon.fi/barn-och-utbildning/skolor/svenskaskolor/kimitoonsgymnasi");
        result.setViranomaistiedotuksenEmail("viranomaisille@email.com");
        result.setKoulutusneuvonnanEmail("Koulutusneuvonta@email.com");
        result.setKriisitiedotuksenEmail("kriisitiedotus@email.com");
        
        
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
        postiosoite.setPostilokero("PL 3");
        postiosoite.setOsoite("");
        postiosoite.setOsoiteTyyppi("posti");
        postiosoite.setPostinumero("00101");
        postiosoite.setPostitoimipaikka("Helsinki");
        postiosoite.setExtraRivi("Ostolaskut");
        postiosoite.setYhteystietoOid("1.2.246.562.5.140081871810");
        result.addPostiosoite(postiosoite);
        
        OrganisaatioYhteystietoDto yhteyshenkilo = new OrganisaatioYhteystietoDto();
        yhteyshenkilo.setEmail("ylli.yhteyshenkilo@email.fi");
        yhteyshenkilo.setNimike("Yhteyshenkilö");
        yhteyshenkilo.setNimi("Ylli Yhteyshenkilö");
        yhteyshenkilo.setYhteyshenkiloOid("1.3.445864.474584848");
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
        result.setOppilaitosKoodi("0032902");
        result.setWwwOsoite("http://www.organisaatio.ab");
        result.setViranomaistiedotuksenEmail("viranomaiset@email.com");
        result.setKoulutusneuvonnanEmail("kouutus@email.com");
        result.setKriisitiedotuksenEmail("kriisi@email.com");

        
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
        postiosoite.setPostilokero("PL 231");
        postiosoite.setOsoiteTyyppi("posti");
        postiosoite.setPostinumero("33101");
        postiosoite.setPostitoimipaikka("Tampere");
        postiosoite.setExtraRivi("Postit paperiset");
        postiosoite.setYhteystietoOid("1.2.246.562.5.140081871815");
        result.addPostiosoite(postiosoite);
        
        yhteyshenkilo = new OrganisaatioYhteystietoDto();
        yhteyshenkilo.setEmail("rehtori.reijo@opetus.fi");
        yhteyshenkilo.setNimi("Reijo Rehtori");
        yhteyshenkilo.setNimike("Rehtori");
        yhteyshenkilo.setYhteyshenkiloOid("1.3.445864.2323235451");
        result.addYhteyshenkilö(yhteyshenkilo);
        
        yhteyshenkilo = new OrganisaatioYhteystietoDto();
        yhteyshenkilo.setEmail("apuri.pertti@opetus.fi");
        yhteyshenkilo.setNimi("Pertti Apuri");
        yhteyshenkilo.setNimike("Apulaisrehtori");
        yhteyshenkilo.setYhteyshenkiloOid("1.3.445868.154845151");
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
