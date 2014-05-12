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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.PartialCacheKey;
import com.googlecode.ehcache.annotations.TriggersRemove;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.dao.organisaatio.OrganisaatioRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.dto.converter.OrganisaatioDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.service.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:38 AM
 */
@Service
public class DefaultOrganisaatioService extends AbstractService implements OrganisaatioService {

    @Autowired
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Autowired(required = false)
    private OrganisaatioRepository organisaatioRepository;

    @Autowired
    private OrganisaatioDtoConverter dtoConverter;

    @Value("${organisaatio.cache.livetime.seconds}")
    private long cacheTimeoutSeconds;

    @Override
//    @Cacheable(cacheName = "organisaatioHakuResultsCache")
    public List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            /*@PartialCacheKey*/ OrganisaatioYhteystietoCriteriaDto criteria,
            Predicate<FilterableOrganisaatio> afterFilterOrganisaatio,
            /*@PartialCacheKey*/ Locale locale,
            CamelRequestContext requestContext) {
//        return organisaatioServiceRoute.findOrganisaatioYhteystietos(criteria, requestContext);

        List<OrganisaatioDetails> results;
        if (criteria.getNumberOfUsedConditions() < 1) {
            results = organisaatioRepository.findOrganisaatios(criteria, locale);
        } else {
            boolean bothYtunnusAndOppilaitotyyppiUsed = criteria.isYtunnusUsed() && criteria.isOppilaitostyyppiUsed();
            if (bothYtunnusAndOppilaitotyyppiUsed) {
                criteria.setUseYtunnus(true);
                criteria.setUseOppilaitotyyppi(false);
            }
            results = organisaatioRepository.findOrganisaatios(criteria, locale);

            criteria.setUseYtunnus(false);
            if (bothYtunnusAndOppilaitotyyppiUsed) {
                criteria.setUseOppilaitotyyppi(true);
                results = mergeWithChildren(results, criteria, locale, false);
            }
            criteria.setUseOppilaitotyyppi(false);
            results = mergeWithChildren(results, criteria, locale, true);
            criteria.setUseYtunnus(true);
            criteria.setUseOppilaitotyyppi(true);

            results = mergeParents(results, locale);
        }

        if (afterFilterOrganisaatio != null) {
            results = new ArrayList<OrganisaatioDetails>(Collections2.filter(results, afterFilterOrganisaatio));
        }

        return dtoConverter.convert(results, new ArrayList<OrganisaatioYhteystietoHakuResultDto>(),
                OrganisaatioYhteystietoHakuResultDto.class);
    }

    private class OidFromOrganisaatio implements Function<OrganisaatioDetails, String> {
        public String apply(OrganisaatioDetails organisaatio) {
            return organisaatio.getOid();
        }
    }

    private List<String> extractParentOids(List<OrganisaatioDetails> organisaatios) {
        List<String> allParents = new ArrayList<String>();
        for (OrganisaatioDetails organisaatio : organisaatios) {
            allParents.addAll(parserParentOidPath(organisaatio));
        }
        return allParents;
    }

    private List<OrganisaatioDetails> mergeWithChildren(List<OrganisaatioDetails> results,
                                                        OrganisaatioYhteystietoCriteriaDto criteria, Locale locale,
                                                        boolean includeParents) {
        List<OrganisaatioDetails> allChildren = organisaatioRepository.findChildren(Collections2.transform(results,
                new OidFromOrganisaatio()), criteria, locale);

        Set<String> added = new TreeSet<String>();
        Map<String,List<OrganisaatioDetails>> childrenByAllParents = new TreeMap<String, List<OrganisaatioDetails>>();
        for (OrganisaatioDetails child : allChildren) {
            List<String> parentOids = parserParentOidPath(child);
            for (String parentOid : parentOids) {
                List<OrganisaatioDetails> parentsChildren = childrenByAllParents.get(parentOid);
                if (parentsChildren == null) {
                    parentsChildren = new ArrayList<OrganisaatioDetails>();
                    childrenByAllParents.put(parentOid, parentsChildren);
                }
                parentsChildren.add(child);
            }
        }

        List<OrganisaatioDetails> mergedResults = new ArrayList<OrganisaatioDetails>();
        for (OrganisaatioDetails result : results) {
            if (includeParents && !added.contains(result.getOid())) {
                mergedResults.add(result);
                added.add(result.getOid());
            }
            List<OrganisaatioDetails> parentsChildren = childrenByAllParents.get(result.getOid());
            if (parentsChildren != null) {
                for (OrganisaatioDetails child : parentsChildren) {
                    if (!added.contains(child.getOid())) {
                        mergedResults.add(child);
                        added.add(child.getOid());
                    }
                }
            }
        }

        return mergedResults;
    }

    private List<OrganisaatioDetails> mergeParents(List<OrganisaatioDetails> results, Locale locale) {
        List<OrganisaatioDetails> allParents = organisaatioRepository.findOrganisaatiosByOids(extractParentOids(results),
                locale);
        Map<String,OrganisaatioDetails> byOids = new TreeMap<String, OrganisaatioDetails>();
        for (OrganisaatioDetails parent : allParents) {
            byOids.put(parent.getOid(), parent);
        }

        Set<String> added = new TreeSet<String>();
        List<OrganisaatioDetails> mergedResults = new ArrayList<OrganisaatioDetails>();
        for (OrganisaatioDetails result : results) {
            List<String> parentOids = parserParentOidPath(result);
            for (String parentOid : parentOids) {
                OrganisaatioDetails parent = byOids.get(parentOid);
                if (parent != null && !added.contains(parent.getOid())) {
                    mergedResults.add(parent);
                    added.add(parent.getOid());
                }
            }
            if (!added.contains(result.getOid())) {
                mergedResults.add(result);
                added.add(result.getOid());
            }
        }

        return mergedResults;
    }

    private List<String> parserParentOidPath(OrganisaatioDetails child) {
        List<String> parentOids = new ArrayList<String>();
        for (String pathPart : child.getParentOidPath()) {
            List<String> pathParts = new ArrayList<String>(Arrays.asList(pathPart.split("\\|")));
            if (pathParts.size() > 1 && "".equals(pathParts.get(0))) {
                pathParts.remove(0);
            }
            if (pathParts.size() > 1 && "".equals(pathParts.get(pathParts.size()-1))) {
                pathParts.remove(pathParts.get(pathParts.size()-1));
            }
            parentOids.addAll(pathParts);
        }
        return parentOids;
    }

    @Override
    public void updateOrganisaatioYtunnusDetails(CamelRequestContext requestContext) {
        OrganisaatioHierarchyResultsDto results = organisaatioServiceRoute.findOrganisaatioHierachy(requestContext);
        logger.info("UPDATING organisaatio's ytunnus details.");
        int count = updateYtunnusResults(results.getOrganisaatiot());
        logger.info("UPDATED " + count + " organisaatio's ytunnus details.");
    }

    private int updateYtunnusResults(List<OrganisaatioHierarchyDto> organisaatiot) {
        int updated = 0;
        for (OrganisaatioHierarchyDto organisaatio : organisaatiot) {
            OrganisaatioDetails details = organisaatioRepository.findOne(organisaatio.getOid());
            if (details != null && organisaatio.getYtunnus() != null) {
                details.setYtunnus(organisaatio.getYtunnus());
                organisaatioRepository.save(details);
                updated++;
            }
            updated += updateYtunnusResults(organisaatio.getChildren());
        }
        return updated;
    }

    @Override
    @Cacheable(cacheName = "organisaatioByOidCache")
    public OrganisaatioDetailsDto getdOrganisaatioByOid(@PartialCacheKey String oid,
                                                                        CamelRequestContext requestContext) {
        if (isCacheUsed()) {
            OrganisaatioDetails details = organisaatioRepository.findOne(oid);
            if (details != null && isCacheUsable(details.getCachedAt())) {
                logger.debug("MongoDB cached organisaatio {}", oid);
                OrganisaatioDetailsDto detailsDto = dtoConverter.convert(details, new OrganisaatioDetailsDto());
                detailsDto.setFresh(false);
                return detailsDto;
            }
        }
        OrganisaatioDetailsDto dto = organisaatioServiceRoute.getdOrganisaatioByOid(oid, requestContext);
        if (isCacheUsed()) {
            OrganisaatioDetails details = dtoConverter.convert(dto, new OrganisaatioDetails());
            organisaatioRepository.save(details);
            logger.info("Persisted organisaatio {} to MongoDB cache.", oid);
        }
        dto.setFresh(true);
        return dto;
    }

    @Override
    @TriggersRemove(cacheName = "organisaatioByOidCache")
    public void purgeOrganisaatioByOidCache(@PartialCacheKey String oid) {
        OrganisaatioDetails details = organisaatioRepository.findOne(oid);
        if (details != null) {
            organisaatioRepository.delete(details);
        }
    }

    private boolean isCacheUsable(DateTime updatedAt) {
        return updatedAt.plus(cacheTimeoutSeconds * MILLIS_IN_SECOND).compareTo(new DateTime()) > 0;
    }

    private boolean isCacheUsed() {
        return organisaatioRepository != null && cacheTimeoutSeconds >= 0;
    }

    public long getCacheTimeoutSeconds() {
        return cacheTimeoutSeconds;
    }

    public void setCacheTimeoutSeconds(long cacheTimeoutSeconds) {
        this.cacheTimeoutSeconds = cacheTimeoutSeconds;
    }
}
