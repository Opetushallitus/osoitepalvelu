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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.PartialCacheKey;
import com.googlecode.ehcache.annotations.TriggersRemove;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.AndPredicateAdapter;
import fi.vm.sade.osoitepalvelu.kooste.common.util.KoodiHelper;
import fi.vm.sade.osoitepalvelu.kooste.dao.organisaatio.OrganisaatioRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.route.OrganisaatioServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.dto.converter.OrganisaatioDtoConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultOrganisaatioService extends AbstractService implements OrganisaatioService {
    private static final long serialVersionUID = 6255113288596549870L;

    @Autowired(required = false)
    private OrganisaatioServiceRoute organisaatioServiceRoute;

    @Autowired(required = false)
    private OrganisaatioRepository organisaatioRepository;

    @Autowired
    private OrganisaatioDtoConverter dtoConverter;

    @Value("${organisaatio.cache.livetime.seconds}")
    private long cacheTimeoutSeconds;

    @Override
    @Cacheable(cacheName = "organisaatioHakuResultsCache")
    public List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            @PartialCacheKey OrganisaatioYhteystietoCriteriaDto criteria,
            @PartialCacheKey Locale locale, CamelRequestContext requestContext) {
        List<OrganisaatioDetails> results;
        // Disabling the conditions that should not effect searches by parent/children relation
        // These are only applied in the end results:
        criteria.setUseKieli(false);
        criteria.setUseKunta(false);
        criteria.setUseOrganisaatioTyyppi(false);

        if (criteria.getNumberOfUsedConditions() < 1) {
            // We can use these now because no other conditions are used (that would result in search by parent/children)
            // and can thus only search once:
            criteria.useAll();
            results = organisaatioRepository.findOrganisaatios(criteria, locale);
        } else if (criteria.isOidUsed()) {
            results = organisaatioRepository.findOrganisaatios(criteria, locale);
            results = mergeParents(results, locale);
        } else {
            // Search is done so that only one condiction is on at the same time.
            boolean yTunnusUsed = criteria.isYtunnusUsed();
            boolean oppilaitostyyppiUsed = criteria.isOppilaitostyyppiUsed();

            criteria.setUseYtunnus(false);
            criteria.setUseOppilaitotyyppi(false);

            // Order of search:
            // 1. YTunnus / koulutuksen järjestäjä
            // 2. Oppilaitostyyppi
            // 3. Last round --> parents on

            // First round
            // One of these on: ytunnus, oppilaitostyyppi
            if (yTunnusUsed) {
                // Two rounds possible (ytunnus, oppilaitostyyppi)
                criteria.setUseYtunnus(true);
            }
            else if (oppilaitostyyppiUsed) {
                // One round possible (oppilaitostyyppi)
                criteria.setUseOppilaitotyyppi(true);
            }
            results = organisaatioRepository.findOrganisaatios(criteria, locale);

            criteria.setUseYtunnus(false);
            criteria.setUseOppilaitotyyppi(false);

            // Second round
            // One of these on: oppilaitostyyppi
            if (yTunnusUsed && oppilaitostyyppiUsed) {
                criteria.setUseOppilaitotyyppi(true);
                results = mergeWithChildren(results, criteria, locale, false);
            }

            criteria.setUseYtunnus(false);
            criteria.setUseOppilaitotyyppi(false);

            // Last round round --> include parents
            results = mergeWithChildren(results, criteria, locale, true);

            // we need to merge parents to the search results as well:
            results = mergeParents(results, locale);
        }

        // restore to defaults:
        criteria.useAll();

        results = new ArrayList<OrganisaatioDetails>(Collections2.filter(results, createResultAfterFilter(criteria)));

        return dtoConverter.convert(results, new ArrayList<OrganisaatioYhteystietoHakuResultDto>(),
                OrganisaatioYhteystietoHakuResultDto.class);
    }

    private Predicate<FilterableOrganisaatio> createResultAfterFilter(OrganisaatioYhteystietoCriteriaDto criteria) {
        AndPredicateAdapter<FilterableOrganisaatio> predicate  = new AndPredicateAdapter<FilterableOrganisaatio>();

        final List<String> organisaatioTyyppis  = criteria.getOrganisaatioTyyppis();
        if (!organisaatioTyyppis.isEmpty()) {
            predicate = predicate.and(new Predicate<FilterableOrganisaatio>() {
                public boolean apply(FilterableOrganisaatio result) {
                    for (String tyyppi : result.getTyypit()) {
                        if (organisaatioTyyppis.contains(tyyppi)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        final List<String> kuntas = criteria.getKuntaList();
        if (!kuntas.isEmpty()) {
            predicate = predicate.and(new Predicate<FilterableOrganisaatio>() {
                public boolean apply(FilterableOrganisaatio organisaatio) {
                    return kuntas.contains(organisaatio.getKotipaikka());
                }
            });
        }

        final List<String> kielis = criteria.getKieliList();
        if (!kielis.isEmpty()) {
            predicate = predicate.and(new Predicate<FilterableOrganisaatio>() {
                public boolean apply(FilterableOrganisaatio organisaatio) {
                    for (String kieli : KoodiHelper.removeVersion(organisaatio.getKielet())) {
                        if (kielis.contains(kieli)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        return predicate;
    }

    private List<String> extractParentOids(List<OrganisaatioDetails> organisaatios) {
        List<String> allParents = new ArrayList<String>();
        for (OrganisaatioDetails organisaatio : organisaatios) {
            allParents.addAll(parseParentOidPath(organisaatio));
        }
        return allParents;
    }

    /**
     * Searches for children for which any of their parents is included in the given results and the given criteria
     * applies.
     *
     * @param results to search children for
     * @param criteria to search children with
     * @param locale to order the results by (localization locale for the name)
     * @param includeParents if true, results should include the given results (parents for the searched children)
     * @return the results (given includeParents is true) and their children so that children are inserted after
     * their parents.
     */
    private List<OrganisaatioDetails> mergeWithChildren(List<OrganisaatioDetails> results,
                    OrganisaatioYhteystietoCriteriaDto criteria, Locale locale, boolean includeParents) {
        List<OrganisaatioDetails> allChildren = organisaatioRepository.findChildren(Collections2.transform(results,
                FilterableOrganisaatio.GET_OID), criteria, locale);

        Set<String> added = new TreeSet<String>();
        Map<String,List<OrganisaatioDetails>> childrenByAllParents = new TreeMap<String, List<OrganisaatioDetails>>();
        for (OrganisaatioDetails child : allChildren) {
            List<String> parentOids = parseParentOidPath(child);
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

    /**
     * @param results to merge parents for
     * @param locale for ordering the results (localization locale for the name)
     * @return results with all their possible parents so that parents are inserted before their children in
     * inheritance order
     */
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
            List<String> parentOids = parseParentOidPath(result);
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

    /**
     * Normalizes the organisaatio's parent OID path. In the details returned by the organisaatio-service the form
     * of parentOidPath is an array with (most likely only) single element where the parent OIDs are concatenated by
     * |-character and the value is also prefixed and postfixed with additional |-characters.
     *
     * @param child
     * @return the parentOidPath of child as an array of single OIDs
     */
    private List<String> parseParentOidPath(OrganisaatioDetails child) {
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
    public List<String> findAllOidsOfCachedOrganisaatios() {
        return organisaatioRepository.findAllOids();
    }

    @Override
    public String findOidByOppilaitoskoodi(String oppilaitosKoodi) {
        return organisaatioRepository.findOidByOppilaitoskoodi(oppilaitosKoodi);
    }

    @Override
    @Cacheable(cacheName = "organisaatioByOidCache")
    public OrganisaatioDetailsDto getdOrganisaatioByOid(@PartialCacheKey String oid, CamelRequestContext requestContext) {
        if (isCacheUsed()) {
            OrganisaatioDetails details = organisaatioRepository.findOne(oid);
            if (details != null && isCacheUsable(details.getCachedAt(), requestContext.getCacheCheckMoment())) {
                logger.debug("MongoDB cached organisaatio {}", oid);
                OrganisaatioDetailsDto detailsDto = dtoConverter.convert(details, new OrganisaatioDetailsDto());
                // Voitaisiin tehdä näin, jos saataisiin jostain lista vain aktiivista organisaatioista, mutta nyt
                // täytyy pitää kaikki muistissa, koska muuten jouduttaisiin aina hakemaan lakkautettujen tiedot:
//                if (details.isLakkautettu()) {
//                    organisaatioRepository.delete(details.getOid());
//                    logger.info("Deleted lakkautettu organisaatio {} from MongoDB.", oid);
//                }
                return detailsDto;
            }
        }
        OrganisaatioDetailsDto dto = organisaatioServiceRoute.getdOrganisaatioByOid(oid, requestContext);
        if (isCacheUsed()) {
            OrganisaatioDetails details = dtoConverter.convert(dto, new OrganisaatioDetails());
//            if (!details.isLakkautettu()) {
                organisaatioRepository.save(details);
                logger.debug("Persisted organisaatio {} to MongoDB cache.", oid);
//            } else {
//                organisaatioRepository.delete(details.getOid());
//                logger.info("Ensure deleted lakkautettu organisaatio {} from MongoDB.", oid);
//            }
        }
        return dto;
    }

    @Override
    @TriggersRemove(cacheName = "organisaatioByOidCache")
    public void purgeOrganisaatioByOidCache(@PartialCacheKey String oid) {
        organisaatioRepository.delete(oid);
    }

    private boolean isCacheUsable(DateTime updatedAt, DateTime now) {
        return updatedAt.plus(cacheTimeoutSeconds * MILLIS_IN_SECOND).compareTo(now) > 0
                && updatedAt.isAfter(OrganisaatioDetails.MODEL_CHANGED_AT);
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
