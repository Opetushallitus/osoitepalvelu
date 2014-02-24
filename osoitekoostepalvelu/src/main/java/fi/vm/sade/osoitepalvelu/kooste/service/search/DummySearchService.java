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

package fi.vm.sade.osoitepalvelu.kooste.service.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OsoitteistoDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 2:54 PM
 */
@Service
public class DummySearchService implements SearchService {

    @Override
    public OrganisaatioResultsDto find(SearchTermsDto terms) {
        // TODO:

        OrganisaatioResultsDto results = new OrganisaatioResultsDto();

        OrganisaatioResultDto result = new OrganisaatioResultDto();
        List<String> tyypit = new ArrayList<String>();
        tyypit.add("Opetuspiste");
        HashMap<String, String> nimi = new HashMap<String, String>();
        nimi.put("sv", "Svenska text");
        nimi.put("fi", "Suomeksi nimi");
        result.setNimi(nimi);
        result.setTyypit(tyypit);
        result.setEmailOsoite("organisaatio2@example.com");
        result.setFaksinumero("09123123");
        result.setKotikunta("Helsinki");
        result.setOid("1.2.246.562.10.38898719687");
        result.setPuhelinnumero("0955544412");
        result.setToimipistekoodi("0032901");
        result.setWwwOsoite("http://www.kimitoon.fi/barn-och-utbildning/skolor/svenskaskolor/kimitoonsgymnasi");

        OsoitteistoDto postiosoite = new OsoitteistoDto();
        postiosoite.setKieli("fi");
        postiosoite.setOsoite("Mannerheiminkatu 48B");
        postiosoite.setOsoiteTyyppi("posti");
        postiosoite.setPostinumero("00101");
        postiosoite.setPostitoimipaikka("Helsinki");
        postiosoite.setExtraRivi("Ostolaskut");
        postiosoite.setYhteystietoOid("1.2.246.562.5.140081871810");
        result.addPostiosoite(postiosoite);

        OrganisaatioYhteystietoDto yhteyshenkilo = new OrganisaatioYhteystietoDto();
        yhteyshenkilo.setEmail("ylli.yhteyshenkilo@email.fi");
        yhteyshenkilo.setNimi("Ylli Yhteyshenkilö");
        yhteyshenkilo.setNimike("Yhteyshenkilö");
        result.addYhteyshenkilö(yhteyshenkilo);

        results.addResult(result);
        return results;
    }

}
