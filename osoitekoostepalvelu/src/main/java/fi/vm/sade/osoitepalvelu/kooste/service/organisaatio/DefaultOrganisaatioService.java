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

package fi.vm.sade.osoitepalvelu.kooste.service.organisaatio;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.PartialCacheKey;
import com.googlecode.ehcache.annotations.TriggersRemove;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.dao.cache.OrganisaatioCacheRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioYksityiskohtaisetTiedot;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.dto.converter.OrganisaatioDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYksityiskohtaisetTiedotDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:38 AM
 */
@Service
public class DefaultOrganisaatioService extends AbstractService implements OrganisaatioService {
    public static final int MILLIS_IN_SECOND = 1000;

    @Autowired
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Autowired(required = false)
    private OrganisaatioCacheRepository organisaatioCacheRepository;

    @Autowired
    private OrganisaatioDtoConverter dtoConverter;

    @Value("${organisaatio.cache.livetime.seconds}")
    private long cacheTimeoutSeconds;


    @Override
    @Cacheable(cacheName = "organisaatioHakuResultsCache")
    public List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            @PartialCacheKey OrganisaatioYhteystietoCriteriaDto criteria,
            CamelRequestContext requestContext) {
        return organisaatioServiceRoute.findOrganisaatioYhteystietos(criteria, requestContext);
    }

    @Override
    @Cacheable(cacheName = "organisaatioByOidCache")
    public OrganisaatioYksityiskohtaisetTiedotDto getdOrganisaatioByOid(@PartialCacheKey String oid,
                                                                        CamelRequestContext requestContext) {
        if (isCacheUsed()) {
            OrganisaatioYksityiskohtaisetTiedot details = organisaatioCacheRepository.findOne(oid);
            if (details != null && isCacheUsable(details.getCachedAt())) {
                logger.info("MongoDB cached organisaatio {}", oid);
                return dtoConverter.convert(details, new OrganisaatioYksityiskohtaisetTiedotDto());
            }
        }
        OrganisaatioYksityiskohtaisetTiedotDto dto = organisaatioServiceRoute.getdOrganisaatioByOid(oid, requestContext);
        if (isCacheUsed()) {
            OrganisaatioYksityiskohtaisetTiedot details = dtoConverter.convert(dto,
                    new OrganisaatioYksityiskohtaisetTiedot());
            organisaatioCacheRepository.save(details);
            logger.info("Persisted rganisaatio {} to MongoDB cache.", oid);
        }
        return dto;
    }

    @Override
    @TriggersRemove(cacheName = "organisaatioByOidCache")
    public void purgeOrganisaatioByOidCache(@PartialCacheKey String oid) {
        OrganisaatioYksityiskohtaisetTiedot details = organisaatioCacheRepository.findOne(oid);
        if (details != null) {
            organisaatioCacheRepository.delete(details);
        }
    }

    private boolean isCacheUsable(DateTime updatedAt) {
        return updatedAt.plus(cacheTimeoutSeconds * MILLIS_IN_SECOND).compareTo(new DateTime()) > 0;
    }

    private boolean isCacheUsed() {
        return organisaatioCacheRepository != null && cacheTimeoutSeconds >= 0;
    }

    public long getCacheTimeoutSeconds() {
        return cacheTimeoutSeconds;
    }

    public void setCacheTimeoutSeconds(long cacheTimeoutSeconds) {
        this.cacheTimeoutSeconds = cacheTimeoutSeconds;
    }

}
