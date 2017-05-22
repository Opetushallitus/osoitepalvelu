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

import fi.vm.sade.auditlog.Audit;
import org.apache.camel.util.StopWatch;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.generic.healthcheck.HealthChecker;
import fi.vm.sade.osoitepalvelu.kooste.dao.organisaatio.OrganisaatioRepository;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;

/**
 * User: ratamaa
 * Date: 5/12/14
 * Time: 3:00 PM
 */
@Component
@HealthCheckerName("organisaatio-cache-status")
public class OrganisaatioCacheHealthCheck extends AbstractService implements HealthChecker {

    @Autowired
    private OrganisaatioRepository organisaatioRepository;

    @Value("${organisaatio.cache.livetime.seconds}")
    private long cacheTimeoutSeconds;

    @Override
    public Object checkHealth() throws Throwable {
        final StopWatch watch  =  new StopWatch();
        final long count = organisaatioRepository.count();
        final DateTime oldestEntry =  organisaatioRepository.findOldestCachedEntry();
        if (oldestEntry == null) {
            throw new IllegalStateException("No cached organisatios.");
        }
        if (!isCacheUsable(oldestEntry)) {
            throw new IllegalStateException("Organisaatio-cache too old. Oldest entry from " + oldestEntry);
        }
        final long resultTook  =  watch.stop();
        return new LinkedHashMap<Object, Object>() {
            private static final long serialVersionUID = 6709620862376707410L;
        {
            put("status", "OK");
            put("organisaatiot-count", count);
            put("oldest-entry", oldestEntry.toString());
            put("response-time", ""  +  resultTook  +  " ms");
        } };
    }

    private boolean isCacheUsable(DateTime updatedAt) {
        return updatedAt.plus((cacheTimeoutSeconds) * MILLIS_IN_SECOND).plusHours(2).compareTo(new DateTime()) > 0;
    }
}
