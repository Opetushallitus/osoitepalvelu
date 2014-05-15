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
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.FilterableOrganisaatio;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:32 AM
 */
@Repository
public class DefaultOrganisaatioRepository extends SimpleMongoRepository<OrganisaatioDetails, String>
        implements OrganisaatioRepository {

    public DefaultOrganisaatioRepository(MongoEntityInformation<OrganisaatioDetails,
            String> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
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

        return getMongoOperations().find(Query.query(conditions.applyTo(new Criteria())).with(new Sort("nimi."
                        +orderByLocale.getLanguage().toLowerCase())), OrganisaatioDetails.class);
    }

    @Override
    public List<OrganisaatioDetails> findOrganisaatiosByOids(List<String> oids, Locale orderByLocale) {
        if (oids == null || oids.isEmpty()) {
            return new ArrayList<OrganisaatioDetails>();
        }
        return getMongoOperations().find(Query.query(new Criteria("oid").in(oids)).with(new Sort("nimi."
                +orderByLocale.getLanguage().toLowerCase())), OrganisaatioDetails.class);

    }

    private void filterByCriteria(CriteriaHelper.Conditions conditions,
                                  OrganisaatioYhteystietoCriteriaDto organisaatioCriteria) {
        if (organisaatioCriteria.isOrganisaatioTyyppiUsed()) {
            conditions.add(new Criteria("tyypit").in(organisaatioCriteria.getOrganisaatioTyyppis()));
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

        return getMongoOperations().find(Query.query(conditions.applyTo(new Criteria())).with(new Sort("nimi."
                +orderByLocale.getLanguage().toLowerCase())), OrganisaatioDetails.class);
    }

    @Override
    public DateTime findOldestCachedEntry() {
        Aggregation agg = newAggregation(
            group().min("cachedAt").as("oldestCacheTime")
        );
        return getMongoOperations().aggregate(agg, OrganisaatioDetails.class.getAnnotation(Document.class).collection(),
                DateTime.class).getUniqueMappedResult();
    }

    @Override
    public List<String> findAllOids() {
        Query q = Query.query(new Criteria());
        q.fields().include("_id");
        return new ArrayList<String>(Collections2.transform(getMongoOperations()
                .find(q, OrganisaatioDetails.class), FilterableOrganisaatio.GET_OID));
    }
}