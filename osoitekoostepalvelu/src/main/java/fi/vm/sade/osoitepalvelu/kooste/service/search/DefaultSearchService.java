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

import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.KoodistoService;
import fi.vm.sade.osoitepalvelu.kooste.service.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.api.OrganisaatioResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchTermsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.converter.SearchResultDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/14/14
 * Time: 2:22 PM
 */
@Service
@Qualifier("actual")
public class DefaultSearchService extends AbstractService implements SearchService {

    @Autowired
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Autowired
    private KoodistoService koodistoService;

    @Autowired
    private SearchResultDtoConverter searchResultDtoConverter;

    @Override
    public OrganisaatioResultsDto find(SearchTermsDto terms) {
        OrganisaatioResultsDto results = new OrganisaatioResultsDto();

        List<OrganisaatioYhteystietoHakuResultDto> organisaatioYhteystietoResults = findOrganisaatios(terms);
        List<OrganisaatioResultDto> convertedResults = searchResultDtoConverter.convert(
                organisaatioYhteystietoResults, new ArrayList<OrganisaatioResultDto>(), OrganisaatioResultDto.class);
        results.getResults().addAll(convertedResults);

        // TOOD: hae henkiloöt

        //log(read("henkilo", "1.2.3.4.238726766"));



        return results;
    }

    protected List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatios(SearchTermsDto terms) {
        OrganisaatioYhteystietoCriteriaDto organisaatioYhteystietosCriteria = new OrganisaatioYhteystietoCriteriaDto();
        List<String> kuntas = resolveKuntaKoodis(terms);
        organisaatioYhteystietosCriteria.setLimit(100); // TODO: How can we have no limit?
        organisaatioYhteystietosCriteria.setKuntaList( kuntas );
        organisaatioYhteystietosCriteria.setKieliList(terms.findTerms("organisaationKielis"));
        organisaatioYhteystietosCriteria.setOppilaitostyyppiList(terms.findTerms("oppilaitostyyppis"));
        organisaatioYhteystietosCriteria.setVuosiluokkaList(terms.findTerms("vuosiluokkas"));
        /// TODO: koultuksenjarjestajas,
        return organisaatioServiceRoute.findOrganisaatioYhteystietos(organisaatioYhteystietosCriteria);
    }

    protected List<String> resolveKuntaKoodis(SearchTermsDto terms) {
        List<String> kuntas = new ArrayList<String>();
        kuntas.addAll(terms.findTerms("kuntas"));
        for( String maakuntaKoodi : terms.findTerms("maakuntas") ) {
            // TODO: mistä koodistosta saa maakuntien kunna? kunnilla ei ole mitään maakuntaan liittyvää metatietoa :/
            // kuntas.addAll( kuntasForMaakunta );
        }
        return kuntas;
    }


    public void setOrganisaatioServiceRoute(OrganisaatioServiceRoute organisaatioServiceRoute) {
        this.organisaatioServiceRoute = organisaatioServiceRoute;
    }

    public void setKoodistoService(KoodistoService koodistoService) {
        this.koodistoService = koodistoService;
    }
}
