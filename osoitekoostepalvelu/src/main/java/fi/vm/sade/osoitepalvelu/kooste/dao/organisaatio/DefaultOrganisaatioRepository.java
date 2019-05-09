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

package fi.vm.sade.osoitepalvelu.kooste.dao.organisaatio;

import com.google.common.collect.Collections2;
import fi.vm.sade.osoitepalvelu.kooste.common.util.CriteriaHelper;
import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.FilterableOrganisaatio;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:32 AM
 */
@Repository
public class DefaultOrganisaatioRepository extends SimpleMongoRepository<OrganisaatioDetails, String>
        implements OrganisaatioRepository {
    private static final long serialVersionUID = 3725230640750779168L;

    private final MongoOperations mongoOperations;

    public DefaultOrganisaatioRepository(MongoEntityInformation<OrganisaatioDetails,
            String> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.mongoOperations = mongoOperations;
    }

    @Autowired
    public DefaultOrganisaatioRepository(MongoRepositoryFactory factory, MongoOperations mongoOperations) {
        this(factory.<OrganisaatioDetails, String>
                getEntityInformation(OrganisaatioDetails.class), mongoOperations);
    }

    @Override
    public List<OrganisaatioDetails> findOrganisaatios(OrganisaatioYhteystietoCriteriaDto organisaatioCriteria,
                                                       Locale orderByLocale) {
        CriteriaHelper.Conditions conditions = new CriteriaHelper.Conditions();

        filterByCriteria(conditions, organisaatioCriteria);

        return mongoOperations.find(Query.query(conditions.applyTo(new Criteria())).with(new Sort("nimi."
                        +orderByLocale.getLanguage().toLowerCase())), OrganisaatioDetails.class);
    }

    @Override
    public List<OrganisaatioDetails> findOrganisaatiosByOids(List<String> oids, Locale orderByLocale) {
        if (oids == null || oids.isEmpty()) {
            return new ArrayList<OrganisaatioDetails>();
        }
        return mongoOperations.find(Query.query(new Criteria("oid").in(oids)).with(new Sort("nimi."
                +orderByLocale.getLanguage().toLowerCase())), OrganisaatioDetails.class);

    }

    private void filterByCriteria(CriteriaHelper.Conditions conditions,
                                  OrganisaatioYhteystietoCriteriaDto organisaatioCriteria) {
        if (organisaatioCriteria.isVainAktiiviset()) {
            conditions.add(new Criteria().orOperator(new Criteria("lakkautusPvm").is(null),
                    new Criteria("lakkautusPvm").gt(new LocalDate().toDateTimeAtStartOfDay().toDate())));
        }
        if (organisaatioCriteria.isOrganisaatioTyyppiUsed()) {
            conditions.add(new Criteria("tyypit").in(organisaatioCriteria.getOrganisaatioTyyppis()));
        }
        if (organisaatioCriteria.isOidUsed()) {
            conditions.add(new Criteria("oid").in(organisaatioCriteria.getOidList()));
        }
        if (organisaatioCriteria.isKuntaUsed()) {
            conditions.add(new Criteria("kotipaikkaUri").in(organisaatioCriteria.getKuntaList()));
        }
        if (organisaatioCriteria.isYtunnusUsed()) {
            conditions.add(new Criteria("ytunnus").in(organisaatioCriteria.getYtunnusList()));
        }
        if (organisaatioCriteria.isOpetusKieliUsed()) {
            conditions.add(CriteriaHelper.inAnyKoodiVersion(new Criteria(), "kieletUris",
                    organisaatioCriteria.getKieliList()));
        }
        if (organisaatioCriteria.isVuosiluokkaUsed()) {
            conditions.add(CriteriaHelper.inAnyKoodiVersion(new Criteria(), "vuosiluokat",
                    organisaatioCriteria.getVuosiluokkaList()));
        }
        if (organisaatioCriteria.isOppilaitostyyppiUsed()) {
            conditions.add(CriteriaHelper.inAnyKoodiVersion(new Criteria(), "oppilaitosTyyppiUri",
                    organisaatioCriteria.getOppilaitostyyppiList()));
        }
    }

    @Override
    public List<OrganisaatioDetails> findChildren(Collection<String> parentOids,
                      OrganisaatioYhteystietoCriteriaDto organisaatioCriteria, Locale orderByLocale) {
        if (parentOids == null || parentOids.isEmpty()) {
            return new ArrayList<OrganisaatioDetails>();
        }
        CriteriaHelper.Conditions conditions = new CriteriaHelper.Conditions();

        conditions.add(CriteriaHelper.inParentOids(new Criteria(), "parentOidPath", parentOids));
        filterByCriteria(conditions, organisaatioCriteria);

        return mongoOperations.find(Query.query(conditions.applyTo(new Criteria())).with(new Sort("nimi."
                +orderByLocale.getLanguage().toLowerCase())), OrganisaatioDetails.class);
    }

    @Override
    public DateTime findOldestCachedEntry() {

        // Tehty ihan kyselyllä järjestäen cachedAt. Jostain syystä alempi alkuperäinen ei palauta tietoja oikein.
        Query q = Query.query(new Criteria());
        q.fields().include("cachedAt");
        q.limit(1);
        q.with(new Sort(Sort.Direction.ASC, "cachedAt"));
        List<OrganisaatioDetails> list = mongoOperations.find(q, OrganisaatioDetails.class);
        if(list.size() == 1) {
            return list.get(0).getCachedAt();
        } else {
            return null;
        }
        /*
        Aggregation agg = newAggregation(
            group("oid").min("cachedAt").as("oldestCacheTime")
        );

        return getMongoOperations().aggregate(agg, OrganisaatioDetails.class.getAnnotation(Document.class).collection(),
                DateTime.class).getUniqueMappedResult();
         */
    }

    @Override
    public List<String> findAllOids() {
        Query q = Query.query(new Criteria());
        q.fields().include("_id");
        return new ArrayList<String>(Collections2.transform(mongoOperations
                .find(q, OrganisaatioDetails.class), FilterableOrganisaatio.GET_OID));
    }

    @Override
    public String findOidByOppilaitoskoodi(String oppilaitosKoodi) {
        @SuppressWarnings("unchecked")
        List<String> oids = mongoOperations
                        .getCollection(OrganisaatioDetails.class.getAnnotation(Document.class).collection())
                .distinct("_id", Criteria.where("oppilaitosKoodi").is(oppilaitosKoodi).getCriteriaObject());
        if (oids == null || oids.isEmpty()) {
            return null;
        }
        return oids.get(0);
    }

    @Override
    public Optional<OrganisaatioDetails> findByYtunnus(String ytunnus) {
        Criteria criteria = Criteria.where("ytunnus").is(ytunnus);
        return Optional.ofNullable(mongoOperations.findOne(Query.query(criteria), OrganisaatioDetails.class));
    }
}
