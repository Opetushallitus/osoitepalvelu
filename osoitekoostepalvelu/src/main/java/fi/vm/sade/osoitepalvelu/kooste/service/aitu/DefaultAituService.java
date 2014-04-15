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

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituOppilaitosRepository;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituToimikuntaRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituOppilaitos;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.aitu.dto.converter.AituDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.route.AituRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.AituOsoitepalveluResultsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ratamaa on 15.4.2014.
 */
@Service
public class DefaultAituService extends AbstractService implements AituService {
    @Autowired
    private AituOppilaitosRepository aituOppilaitosRepository;

    @Autowired
    private AituToimikuntaRepository aituToimikuntaRepository;

    @Autowired
    private AituDtoConverter dtoConverter;

    @Autowired
    private AituRoute aituRoute;

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
