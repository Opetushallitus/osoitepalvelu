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
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OrganisaatioYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OsoitteistoDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(JUnit4.class)
public class ApiToJsonTest {
    
    @Test
    public void generateResultsJsonExample() {
        List<OrganisaatioResultDto> results  =  new ArrayList<OrganisaatioResultDto>();
        
        OrganisaatioResultDto result  =  new OrganisaatioResultDto();
        List<String> tyypit  =  new ArrayList<String>();
        tyypit.add("Opetuspiste");
        HashMap<String, String> nimi  =  new HashMap<String, String>();
        nimi.put("sv", "Svenska text");
        nimi.put("fi", "Suomeksi nimi");
        result.setNimi(nimi);
        result.setTyypit(tyypit);
        result.setEmailOsoite("organisaatio2@email.com");
        result.setKotikunta("Helsinki");
        result.setOid("1.2.246.562.10.38898719687");
        result.setPuhelinnumero("0955544412");
        result.setToimipistekoodi("0032901");
        result.setOppilaitosKoodi("0032901");
        result.setWwwOsoite("http://www.kimitoon.fi/barn-och-utbildning/skolor/svenskaskolor/kimitoonsgymnasi");
        result.setViranomaistiedotuksenEmail("viranomaisille@email.com");
        result.setKoulutusneuvonnanEmail("Koulutusneuvonta@email.com");
        result.setKriisitiedotuksenEmail("kriisitiedotus@email.com");
        
        OsoitteistoDto postiosoite  =  new OsoitteistoDto();
        postiosoite.setKieli("fi");
        postiosoite.setPostilokero("PL 3");
        postiosoite.setOsoite("");
        postiosoite.setOsoiteTyyppi("posti");
        postiosoite.setPostinumero("00101");
        postiosoite.setPostitoimipaikka("Helsinki");
        postiosoite.setExtraRivi("Ostolaskut");
        postiosoite.setYhteystietoOid("1.2.246.562.5.140081871810");
        result.addPostiosoite(postiosoite);
        
        OrganisaatioYhteystietoDto yhteyshenkilo  =  new OrganisaatioYhteystietoDto();
        yhteyshenkilo.setEmail("ylli.yhteyshenkilo@email.fi");
        yhteyshenkilo.setNimike("Yhteyshenkilö");
        yhteyshenkilo.setNimi("Ylli Yhteyshenkilö");
        yhteyshenkilo.setYhteyshenkiloOid("1.3.445864.474584848");
        result.addYhteyshenkilo(yhteyshenkilo);
        
        results.add(result);
        
        result  =  new OrganisaatioResultDto();
        tyypit  =  new ArrayList<String>();
        tyypit.add("Toimipiste");
        result.setTyypit(tyypit);
        nimi  =  new HashMap<String, String>();
        nimi.put("sv", "Högskola");
        nimi.put("fi", "Tre Korkeakoulu");
        result.setNimi(nimi);
        result.setEmailOsoite("esimerkki@email.com");
        result.setKotikunta("Tampere");
        result.setOid("1.2.246.562.10.388987196872");
        result.setPuhelinnumero("03555666000");
        result.setToimipistekoodi("0032902");
        result.setOppilaitosKoodi("0032902");
        result.setWwwOsoite("http://www.organisaatio.ab");
        result.setViranomaistiedotuksenEmail("viranomaiset@email.com");
        result.setKoulutusneuvonnanEmail("kouutus@email.com");
        result.setKriisitiedotuksenEmail("kriisi@email.com");

        postiosoite  =  new OsoitteistoDto();
        postiosoite.setKieli("fi");
        postiosoite.setPostilokero("PL 231");
        postiosoite.setOsoiteTyyppi("posti");
        postiosoite.setPostinumero("33101");
        postiosoite.setPostitoimipaikka("Tampere");
        postiosoite.setExtraRivi("Postit paperiset");
        postiosoite.setYhteystietoOid("1.2.246.562.5.140081871815");
        result.addPostiosoite(postiosoite);
        
        yhteyshenkilo  =  new OrganisaatioYhteystietoDto();
        yhteyshenkilo.setEmail("rehtori.reijo@opetus.fi");
        yhteyshenkilo.setNimi("Reijo Rehtori");
        yhteyshenkilo.setNimike("Rehtori");
        yhteyshenkilo.setYhteyshenkiloOid("1.3.445864.2323235451");
        result.addYhteyshenkilo(yhteyshenkilo);
        
        yhteyshenkilo  =  new OrganisaatioYhteystietoDto();
        yhteyshenkilo.setEmail("apuri.pertti@opetus.fi");
        yhteyshenkilo.setNimi("Pertti Apuri");
        yhteyshenkilo.setNimike("Apulaisrehtori");
        yhteyshenkilo.setYhteyshenkiloOid("1.3.445868.154845151");
        result.addYhteyshenkilo(yhteyshenkilo);
        
        results.add(result);
        
        printJson(results);
    }


    private void printJson(Object object) {
        ObjectMapper mapper  =  new ObjectMapper();
        mapper.getSerializationConfig().without(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        try {
            System.out.println(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
