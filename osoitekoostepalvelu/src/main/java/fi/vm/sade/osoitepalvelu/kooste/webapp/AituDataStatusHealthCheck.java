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

package fi.vm.sade.osoitepalvelu.kooste.webapp;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.generic.healthcheck.HealthChecker;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituOppilaitosRepository;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.AituToimikuntaRepository;

/**
 * Created by ratamaa on 15.4.2014.
 */
@Component
@HealthCheckerName("aitu-data-status")
public class AituDataStatusHealthCheck implements HealthChecker {

    @Autowired
    private AituOppilaitosRepository oppilaitosRepository;

    @Autowired
    private AituToimikuntaRepository toimikuntaRepository;

    @Override
    public Object checkHealth() throws Throwable {
        final long oppilaitosCount = oppilaitosRepository.count();
        final long toimikuntasCount = toimikuntaRepository.count();
        if (oppilaitosCount < 1) {
            throw new IllegalStateException("AITU data not fetched. oppilaitosCount=0");
        }
        if (toimikuntasCount < 1) {
            throw new IllegalStateException("AITU data not fetched. toimikuntasCount=0");
        }
        return new LinkedHashMap<Object, Object>() { 
            private static final long serialVersionUID = 7523073763130134460L;
        {
            put("status", "OK");
            put("number-of-oppilaitos", oppilaitosCount);
            put("number-of-toimikuntas", toimikuntasCount);
        } };
    }
}
