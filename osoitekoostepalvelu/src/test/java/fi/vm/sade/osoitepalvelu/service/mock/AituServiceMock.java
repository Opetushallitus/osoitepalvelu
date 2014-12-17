/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
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

package fi.vm.sade.osoitepalvelu.service.mock;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituKielisyys;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituOppilaitosCriteria;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituToimikuntaCriteria;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituOppilaitos;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituOppilaitosResultDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituOsoitepalveluResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituToimikuntaResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.aitu.AituService;
import fi.vm.sade.osoitepalvelu.kooste.service.aitu.DefaultAituService;
import java.util.List;

/**
 *
 * @author markus
 */
public class AituServiceMock implements AituService {
    private final DefaultAituService defaultAituService;
    private List<AituToimikunta> toimikuntas;
    private List<AituOppilaitos> oppilaitokses;
    private List<String> toimikuntaIds;
    
    public AituServiceMock(DefaultAituService defaultAituService) {
        this.defaultAituService = defaultAituService;
    }
    
    public void setToimikuntas(List<AituToimikunta> toimikuntas) {
        this.toimikuntas = toimikuntas;
    }
    
    public void setOppilaitokses(List<AituOppilaitos> oppilaitokses) {
        this.oppilaitokses = oppilaitokses;
    }

    public void setToimikuntaIds(List<String> toimikuntaIds) {
        this.toimikuntaIds = toimikuntaIds;
    }
    
    @Override
    public List<String> findVoimassaOlevatRoolit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<AituToimikuntaResultDto> findToimikuntasWithMatchingJasens(AituToimikuntaCriteria criteria, AituKielisyys orderByNimiKieli) {
        return defaultAituService.findToimikuntasWithMatchingJasens(criteria, orderByNimiKieli, toimikuntas);
    }

    @Override
    public List<AituOppilaitosResultDto> findNayttotutkinnonJarjestajas(AituOppilaitosCriteria criteria, AituKielisyys orderingKielisyys) {
        return defaultAituService.findNayttotutkinnonJarjestajas(criteria, orderingKielisyys, oppilaitokses, toimikuntaIds);
    }

    @Override
    public void refreshData(AituOsoitepalveluResultsDto results) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void refreshData(CamelRequestContext requestContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
