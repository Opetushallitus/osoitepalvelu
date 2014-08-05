package fi.vm.sade.osoitepalvelu.kooste.service.aitu;
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

import java.io.Serializable;
import java.util.List;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituKielisyys;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituOppilaitosCriteria;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituToimikuntaCriteria;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituOppilaitosResultDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituOsoitepalveluResultsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituToimikuntaResultDto;

/**
 * Created by ratamaa on 15.4.2014.
 */
public interface AituService extends Serializable {

    /**
     * @return all the roolis with at least one jasenyys voimassa in any AITU's tutkintoimikuntas
     */
    List<String> findVoimassaOlevatRoolit();

    /**
     * @param criteria for toimikuntas and jasens
     * @param orderByNimiKieli AITU's keili to order by
     * @return AituToimikuntas with only the jasens matching the given c
     */
    List<AituToimikuntaResultDto> findToimikuntasWithMatchingJasens(AituToimikuntaCriteria criteria,
                                                                    AituKielisyys orderByNimiKieli);

    /**
     * @param criteria for AITU oppilaitos and sopimus
     * @param orderingKielisyys AITU's keili to order by
     * @return AituOppilaitosResultDto with matchins sopimus
     */
    List<AituOppilaitosResultDto> findNayttotutkinnonJarjestajas(AituOppilaitosCriteria criteria,
                                                                 AituKielisyys orderingKielisyys);

    /**
     * @param results to override the data in MongoDB with
     */
    void refreshData(AituOsoitepalveluResultsDto results);

    /**
     * Refreshes the data in MongoDB repository by fetching new data
     * from AITU route.
     *
     * @param requestContext for the AITU route
     */
    void refreshData(CamelRequestContext requestContext);
}
