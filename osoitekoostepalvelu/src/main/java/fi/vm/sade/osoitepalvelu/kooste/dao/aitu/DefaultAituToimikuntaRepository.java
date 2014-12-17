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

package fi.vm.sade.osoitepalvelu.kooste.dao.aitu;

import com.google.common.collect.Collections2;

import fi.vm.sade.osoitepalvelu.kooste.common.util.CriteriaHelper;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituToimikuntaCriteria;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.AituSopimusDto;

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

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.LoggerFactory;

/**
 * Created by ratamaa on 15.4.2014.
 */
@Repository
public class DefaultAituToimikuntaRepository extends SimpleMongoRepository<AituToimikunta, String>
        implements AituToimikuntaRepository {
    private static final long serialVersionUID = -1466189419172139869L;
    
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AituOppilaitosRepository aituOppilaitosRepository;

    public DefaultAituToimikuntaRepository(MongoEntityInformation<AituToimikunta, String> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
    }

    @Autowired
    public DefaultAituToimikuntaRepository(MongoRepositoryFactory factory, MongoOperations mongoOperations) {
        this(factory.<AituToimikunta, String>getEntityInformation(AituToimikunta.class), mongoOperations);
    }

    @Override
    public List<AituToimikunta> findToimikuntas(AituToimikuntaCriteria toimikuntaCriteria, AituKielisyys orberByNimi) {
        CriteriaHelper.Conditions conditions = conditions(toimikuntaCriteria, true);
        List<AituToimikunta> result = getMongoOperations().find(Query.query(conditions.applyTo(new Criteria()))
                .with(new Sort("nimi." + orberByNimi.getAituKieli())), AituToimikunta.class);
        if (toimikuntaCriteria.isToimikuntaEmails()) {
            // Haetaan myös ne toimikunnat joilla on sähköpostiosoite
            CriteriaHelper.Conditions emailconditions = conditions(toimikuntaCriteria, false);
            List<AituToimikunta> result2 = getMongoOperations().find(Query.query(emailconditions.applyTo(new Criteria()))
                .with(new Sort("nimi." + orberByNimi.getAituKieli())), AituToimikunta.class);
            if (result2.isEmpty()) {
                logger.error("Email address not found for any toimikunta");
            }
            result.addAll(result2);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findToimikuntaIds(AituToimikuntaCriteria toimikuntaCriteria) {
        return getMongoOperations().getCollection(AituToimikunta.class.getAnnotation(Document.class).collection())
                .distinct("_id",  conditions(toimikuntaCriteria, true).applyTo(new Criteria()).getCriteriaObject());
    }

    protected CriteriaHelper.Conditions conditions(AituToimikuntaCriteria toimikuntaCriteria, boolean jasenet) {
        CriteriaHelper.Conditions conditions = new CriteriaHelper.Conditions();
        if (toimikuntaCriteria.isIdsUsed()) {
            conditions.add(new Criteria("_id").in(toimikuntaCriteria.getIdsIn()));
        }
        if (toimikuntaCriteria.isKielisyysUsed()) {
            conditions.add(new Criteria("kielisyys").in(toimikuntaCriteria.getKielisyysIn()));
        }
        if (toimikuntaCriteria.isToimikausiUsed()) {
            conditions.add(new Criteria("toimikausi").in(AituToimikunta.AituToimikausi.names(toimikuntaCriteria.getToimikausisIn())));
        }
        if (jasenet) {
            if (toimikuntaCriteria.isJasenKielisyysUsed()) {
                conditions.add(new Criteria("jasenyydet.aidinkieli").in(toimikuntaCriteria.getJasenKielisyysIn()));
            }
            if (toimikuntaCriteria.isJasenRoolisUsed()) {
                conditions.add(new Criteria("jasenyydet.rooli").in(toimikuntaCriteria.getJasensInRoolis()));
            }
            if (toimikuntaCriteria.isOnlyVoimassaOlevat()) {
                conditions.add(new Criteria("jasenyydet.voimassa").is(true));
            }
        } else {
            if (toimikuntaCriteria.isToimikuntaEmails()) {
                conditions.add(new Criteria("sahkoposti").regex(Pattern.compile(".*@.*")));
            }
        }
        if (toimikuntaCriteria.isTutkintoUsed() || toimikuntaCriteria.isOpintoalaUsed()
                || toimikuntaCriteria.isOppilaitoskoodiUsed()) {
            Collection<String> toimikuntasBySopimusehdot = Collections2.transform(
                    aituOppilaitosRepository.findMatchingSopimukset(toimikuntaCriteria.toOppilaitosCriteria(),
                            null, null), AituSopimusDto.TOIMIKUNTA);
            conditions.add(new Criteria("_id").in(toimikuntasBySopimusehdot));
        }
        return conditions;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findVoimassaOlevatRoolit() {
        return getMongoOperations().getCollection(AituToimikunta.class.getAnnotation(Document.class).collection())
                .distinct("jasenyydet.rooli", Criteria.where("jasenyydet.voimassa").is(true).getCriteriaObject());
    }
}
