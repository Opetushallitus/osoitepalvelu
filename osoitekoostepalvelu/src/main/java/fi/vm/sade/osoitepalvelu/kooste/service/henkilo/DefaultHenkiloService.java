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

package fi.vm.sade.osoitepalvelu.kooste.service.henkilo;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.PartialCacheKey;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.dao.cache.HenkiloCacheRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.HenkiloDetails;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.henkilo.dto.converter.HenkiloDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.route.AuthenticationServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloListResultDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 2:30 PM
 */
@Service
public class DefaultHenkiloService extends AbstractService implements HenkiloService {

    @Autowired(required = false)
    private HenkiloCacheRepository henkiloCacheRepository;

    @Autowired
    private AuthenticationServiceRoute authenticationServiceRoute;

    @Autowired
    private HenkiloDtoConverter dtoConverter;

    @Value("${henkilo.cache.livetime.seconds}")
    private long cacheTimeoutSeconds;

    @Override
    @Cacheable(cacheName = "henkiloHakuCache")
    public List<HenkiloListResultDto> findHenkilos(
            @PartialCacheKey HenkiloCriteriaDto criteria, CamelRequestContext requestContext) {
        return authenticationServiceRoute.findHenkilos(criteria, requestContext);
    }

    @Override
    @Cacheable(cacheName = "henkiloByOidCache")
    public HenkiloDetailsDto getHenkiloTiedot(@PartialCacheKey String oid, CamelRequestContext requestContext) {
        if (isCacheUsed()) {
            HenkiloDetails details = henkiloCacheRepository.findOne(oid);
            if (details != null && isCacheUsable(details.getCachedAt())) {
                logger.debug("MongoDB cached henkilo {}", oid);
                return dtoConverter.convert(details, new HenkiloDetailsDto());
            }
        }
        HenkiloDetailsDto dto = authenticationServiceRoute.getHenkiloTiedot(oid, requestContext);
        if (isCacheUsed()) {
            HenkiloDetails details = dtoConverter.convert(dto, new HenkiloDetails());
            henkiloCacheRepository.save(details);
            logger.debug("Persisted henkilo {} to MongoDB cache.", oid);
        }
        return dto;
    }

    public void setAuthenticationServiceRoute(AuthenticationServiceRoute authenticationServiceRoute) {
        this.authenticationServiceRoute = authenticationServiceRoute;
    }

    private boolean isCacheUsable(DateTime updatedAt) {
        return updatedAt.plus(cacheTimeoutSeconds * MILLIS_IN_SECOND).compareTo(new DateTime()) > 0;
    }

    private boolean isCacheUsed() {
        return henkiloCacheRepository != null && cacheTimeoutSeconds >= 0;
    }

    public long getCacheTimeoutSeconds() {
        return cacheTimeoutSeconds;
    }

    public void setCacheTimeoutSeconds(long cacheTimeoutSeconds) {
        this.cacheTimeoutSeconds = cacheTimeoutSeconds;
    }
}
