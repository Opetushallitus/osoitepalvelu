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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fi.vm.sade.log.client.Logger;
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
@Qualifier("dummy")
public class DummySearchService implements SearchService {

    @Autowired
    private Logger sadeLogger;

    @Override
    public OrganisaatioResultsDto find(SearchTermsDto terms) {
        // TODO: Haun toteutus
        // Testataan käyttöliittymää suhteellisen suurella datamäärällä. 5000 oli vielä myös OK mutta
        // käytännössä jäävät yleensä pienemmäksi.

        OrganisaatioResultsDto results = new OrganisaatioResultsDto();

        for( long i = 1; i <= 1000; ++i ) {
            OrganisaatioResultDto result = new OrganisaatioResultDto();
            List<String> tyypit = new ArrayList<String>();
            tyypit.add("Opetuspiste");
            HashMap<String, String> nimi = new HashMap<String, String>();
            nimi.put("sv", "Svenska text "+i);
            nimi.put("fi", "Suomeksi nimi "+i);
            result.setNimi(nimi);
            result.setTyypit(tyypit);
            result.setEmailOsoite("organisaatio"+i+"@example.com");
            result.setFaksinumero("09123123");
            result.setKotikunta("Helsinki");
            result.setOid("1.2.246.562.10."+(38898719687l+i));
            result.setPuhelinnumero("0955544412");
            result.setToimipistekoodi(""+(32901+i));
            result.setOppilaitosKoodi("" + (32901 + i));
            result.setWwwOsoite("http://www.kimitoon.fi/barn-och-utbildning/skolor/svenskaskolor/kimitoonsgymnasi");

            OsoitteistoDto postiosoite = new OsoitteistoDto();
            postiosoite.setKieli("fi");
            postiosoite.setOsoite("Mannerheiminkatu 48B");
            postiosoite.setOsoiteTyyppi("posti");
            postiosoite.setPostinumero("00101");
            postiosoite.setPostitoimipaikka("Helsinki");
            postiosoite.setExtraRivi("Ostolaskut");
            postiosoite.setYhteystietoOid("1.2.246.562.5."+(140081871810l+i));
            result.addPostiosoite(postiosoite);

            OrganisaatioYhteystietoDto yhteyshenkilo = new OrganisaatioYhteystietoDto();
            yhteyshenkilo.setYhteyshenkiloOid("1.2.3.4."+(45654858484l+i));
            yhteyshenkilo.setEmail("ylli.yhteyshenkilo@email.fi");
            yhteyshenkilo.setNimi("Ylli Yhteyshenkilö");
            yhteyshenkilo.setNimike("Yhteyshenkilö");
            result.addYhteyshenkilö(yhteyshenkilo);

            results.addResult(result);
        }

        return results;
    }

}