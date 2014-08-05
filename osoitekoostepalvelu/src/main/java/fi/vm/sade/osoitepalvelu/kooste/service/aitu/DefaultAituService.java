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

package fi.vm.sade.osoitepalvelu.kooste.service.aitu;

import com.google.common.collect.Collections2;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.AndPredicateAdapter;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituKielisyys;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituOppilaitosRepository;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituToimikuntaRepository;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituOppilaitosCriteria;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituToimikuntaCriteria;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituOppilaitos;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.aitu.dto.converter.AituDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.route.AituRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ratamaa on 15.4.2014.
 */
@Service
public class DefaultAituService extends AbstractService implements AituService {
    private static final long serialVersionUID = 7522489326846677935L;

    @Autowired(required = false)
    private AituOppilaitosRepository aituOppilaitosRepository;

    @Autowired(required = false)
    private AituToimikuntaRepository aituToimikuntaRepository;

    @Autowired
    private AituDtoConverter dtoConverter;

    @Autowired(required = false)
    private AituRoute aituRoute;

    @Override
    public List<String> findVoimassaOlevatRoolit() {
        return aituToimikuntaRepository.findVoimassaOlevatRoolit();
    }

    @Override
    public List<AituToimikuntaResultDto> findToimikuntasWithMatchingJasens(AituToimikuntaCriteria criteria,
                                                                           AituKielisyys orderByNimiKieli) {
        List<AituToimikuntaResultDto> results = dtoConverter.convert(
                aituToimikuntaRepository.findToimikuntas(criteria, orderByNimiKieli),
                new ArrayList<AituToimikuntaResultDto>(), AituToimikuntaResultDto.class);
        AndPredicateAdapter<AituJasenyysDto> jasenyysPredicate = criteria.createJasenyysPredicate();
        if (jasenyysPredicate.isFiltering()) {
            for (AituToimikuntaResultDto resultDto : results) {
                resultDto.setJasenyydet(new ArrayList<AituJasenyysDto>(
                        Collections2.filter(resultDto.getJasenyydet(), jasenyysPredicate)));
            }
        }
        return results;
    }

    @Override
    public List<AituOppilaitosResultDto> findNayttotutkinnonJarjestajas(AituOppilaitosCriteria criteria,
                                                                        AituKielisyys orderingKielisyys) {
        List<AituOppilaitosResultDto> results = dtoConverter.convert(
                aituOppilaitosRepository.findOppilaitos(criteria, orderingKielisyys),
                new ArrayList<AituOppilaitosResultDto>(), AituOppilaitosResultDto.class);
        List<String> matchedToimikuntas = null;
        if (criteria.isRelatedToimikuntaResultsNeeded()) {
            matchedToimikuntas = aituToimikuntaRepository.findToimikuntaIds(criteria.toRelatedToimikuntaCriteria());
        }
        AndPredicateAdapter<AituSopimusDto> sopimusPredicate = criteria.createSopimusPredicate(matchedToimikuntas);
        AndPredicateAdapter<AituTutkintoDto> tutkintoPredicate = criteria.createTutkintoPredicate();
        if (sopimusPredicate.isFiltering()) {
            List<AituOppilaitosResultDto> filteredResults = new ArrayList<AituOppilaitosResultDto>();
            for (AituOppilaitosResultDto resultDto : results) {
                resultDto.setSopimukset(new ArrayList<AituSopimusDto>(
                        Collections2.filter(resultDto.getSopimukset(), sopimusPredicate)));
                if (resultDto.getSopimukset().isEmpty()) {
                    // No any matching sopimus
                    continue;
                }
                if (tutkintoPredicate.isFiltering()) {
                    boolean tutkintosExists = false;
                    for (AituSopimusDto sopimus : resultDto.getSopimukset()) {
                        sopimus.setTutkinnot(new ArrayList<AituTutkintoDto>(
                                Collections2.filter(sopimus.getTutkinnot(), tutkintoPredicate)));
                        if (!sopimus.getTutkinnot().isEmpty()) {
                            tutkintosExists = true;
                        }
                    }
                    if (!tutkintosExists) {
                        continue;
                    }
                }
                filteredResults.add(resultDto);
            }
            return filteredResults;
        }
        return results;
    }

    @Override
    public void refreshData(AituOsoitepalveluResultsDto results) {
        List<AituOppilaitos> newOppilaitos = dtoConverter.convert(results.getOppilaitokset(),
                new ArrayList<AituOppilaitos>(), AituOppilaitos.class);
        List<AituToimikunta> newToimikuntas = dtoConverter.convert(results.getToimikunnat(),
                new ArrayList<AituToimikunta>(), AituToimikunta.class);
        aituOppilaitosRepository.deleteAll();
        aituOppilaitosRepository.save(newOppilaitos);

        aituToimikuntaRepository.deleteAll();
        aituToimikuntaRepository.save(newToimikuntas);
    }

    @Override
    public void refreshData(CamelRequestContext requestContext) {
        AituOsoitepalveluResultsDto results = aituRoute.findAituResults(requestContext);
        refreshData(results);
    }
}
