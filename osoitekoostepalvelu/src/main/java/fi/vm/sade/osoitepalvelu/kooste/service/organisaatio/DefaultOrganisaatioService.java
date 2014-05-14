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
    public List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            OrganisaatioYhteystietoCriteriaDto criteria, Locale locale, CamelRequestContext requestContext) {
        List<OrganisaatioDetails> results;
        // Previously search done by remote call to organisaatio service. Unfortunately, though, it could not serve us
        // with parent/children related searches/conditions and was also quite slow:
//        results = organisaatioServiceRoute.findOrganisaatioYhteystietos(criteria, requestContext);
//        results = new ArrayList<OrganisaatioDetails>(Collections2.filter(results, createResultAfterFilter(criteria)));
//        return results;

        // So instead, we search from the "local" MongoDB where all the organisaatios are cached.


        // Disabling the conditions that should not effect searches by parent/children relation and only applied in the
        // end results:
        criteria.setUseKieli(false);
        criteria.setUseKunta(false);
        criteria.setUseOrganisaatioTyyppi(false);

        if (criteria.getNumberOfUsedConditions() < 1) {
            // We can use these now because no other conditions are used (that would result in search by parent/children)
            // and can thus only search once:
            criteria.useAll();
            results = organisaatioRepository.findOrganisaatios(criteria, locale);
        } else {
            // If both ytunnus and oppilaitostyyppi conditions are used, first search by ytunnus (koulutuksen järjestäjä)
            // and secondly search their children by oppilaitostyyppi:
            boolean bothYtunnusAndOppilaitotyyppiUsed = criteria.isYtunnusUsed() && criteria.isOppilaitostyyppiUsed();
            if (bothYtunnusAndOppilaitotyyppiUsed) {
                criteria.setUseYtunnus(true);
                criteria.setUseOppilaitotyyppi(false);
            }

            // First search for oranisaatios and then their children:
            results = organisaatioRepository.findOrganisaatios(criteria, locale);

            // Searching under possibly koulutuksen järjestäjä conditioned results by disabling y-tunnus
            // on the second (and possible third) round:
            criteria.setUseYtunnus(false);
            if (bothYtunnusAndOppilaitotyyppiUsed) {
                // if both y-tunnus and oppilaitostyyppi were meant to be used, apply oopilaitostyyppi condition
                // to the children of previously searched koulutuksen järjestäjäs and merge the results:
                criteria.setUseOppilaitotyyppi(true);
                boolean includeParents = false; /* <- previous results not included  */
                results = mergeWithChildren(results, criteria, locale, includeParents );
            }

            // Searching under possibly oppilaitostyyppi conditioned results by disabling it on the second(/third) round:
            criteria.setUseOppilaitotyyppi(false);
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
    public void updateOrganisaatioYtunnusDetails(CamelRequestContext requestContext) {
        OrganisaatioHierarchyResultsDto results = organisaatioServiceRoute.findOrganisaatioHierachy(requestContext);
        logger.info("UPDATING organisaatio's ytunnus details.");
        int count = updateYtunnusResults(results.getOrganisaatiot());
        logger.info("UPDATED " + count + " organisaatio's ytunnus details.");
    }

    @Override
    public List<String> findAllOidsOfCachedOrganisaatios() {
        return organisaatioRepository.findAllOids();
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
    public OrganisaatioDetailsDto getdOrganisaatioByOid(@PartialCacheKey String oid, CamelRequestContext requestContext) {
        if (isCacheUsed()) {
            OrganisaatioDetails details = organisaatioRepository.findOne(oid);
            if (details != null && isCacheUsable(details.getCachedAt())) {
                logger.debug("MongoDB cached organisaatio {}", oid);
                OrganisaatioDetailsDto detailsDto = dtoConverter.convert(details, new OrganisaatioDetailsDto());
                return detailsDto;
            }
        }
        OrganisaatioDetailsDto dto = organisaatioServiceRoute.getdOrganisaatioByOid(oid, requestContext);
        if (isCacheUsed()) {
            OrganisaatioDetails details = dtoConverter.convert(dto, new OrganisaatioDetails());
            organisaatioRepository.save(details);
            logger.info("Persisted organisaatio {} to MongoDB cache.", oid);
        }
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
