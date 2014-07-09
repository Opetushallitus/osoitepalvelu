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

import fi.vm.sade.osoitepalvelu.kooste.common.util.CriteriaHelper;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituToimikuntaCriteria;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituOppilaitos;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;
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

import java.util.List;

/**
 * Created by ratamaa on 15.4.2014.
 */
@Repository
public class DefaultAituToimikuntaRepository extends SimpleMongoRepository<AituToimikunta, String>
        implements AituToimikuntaRepository {

    public DefaultAituToimikuntaRepository(MongoEntityInformation<AituToimikunta, String> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
    }

    @Autowired
    public DefaultAituToimikuntaRepository(MongoRepositoryFactory factory, MongoOperations mongoOperations) {
        this(factory.<AituToimikunta, String>getEntityInformation(AituToimikunta.class), mongoOperations);
    }

    @Override
    public List<AituToimikunta> findToimikuntas(AituToimikuntaCriteria toimikuntaCriteria, AituKielisyys orberByNimi) {
        CriteriaHelper.Conditions conditions = new CriteriaHelper.Conditions();
        if (toimikuntaCriteria.isIdsUsed()) {
            conditions.add(new Criteria("_id").in(toimikuntaCriteria.getIdsIn()));
        }
        if (toimikuntaCriteria.isKielisyysUsed()) {
            conditions.add(new Criteria("kielisyys").in(toimikuntaCriteria.getKielisyysIn()));
        }
        if (toimikuntaCriteria.isJasenKielisyysUsed()) {
            conditions.add(new Criteria("jasenyydet.aidinkieli").in(toimikuntaCriteria.getJasenKielisyysIn()));
        }
        if (toimikuntaCriteria.isJasenRoolisUsed()) {
            conditions.add(new Criteria("jasenyydet.rooli").in(toimikuntaCriteria.getJasensInRoolis()));
        }
        if (toimikuntaCriteria.isToimikausiUsed()) {
            conditions.add(new Criteria("toimikausi").in(AituToimikunta.AituToimikausi.names(toimikuntaCriteria.getToimikausisIn())));
        }
        if (toimikuntaCriteria.isOnlyVoimassaOlevat()) {
            conditions.add(new Criteria("jasenyydet.voimassa").is(true));
        }
        if (toimikuntaCriteria.isTutkintoUsed() || toimikuntaCriteria.isOpintoalaUsed() || toimikuntaCriteria.isOppilaitoskoodiUsed()) {
            List<String> toimikuntasBySopimusehdot = getMongoOperations().getCollection(AituOppilaitos.class.getAnnotation(Document.class).collection())
                .distinct("sopimukset.toimikunta", new CriteriaHelper.Conditions()
                        .addGiven(new Criteria("oppilaitoskoodi").in(toimikuntaCriteria.getOppilaitoskoodiIn()),
                                toimikuntaCriteria.isOppilaitoskoodiUsed())
                        .addGiven(new Criteria("sopimukset.tutkinnot.tutkintotunnus").in(toimikuntaCriteria.getTutkintoTunnusIn()),
                                toimikuntaCriteria.isTutkintoUsed())
                        .addGiven(new Criteria("sopimukset.tutkinnot.opintoalatunnus").in(toimikuntaCriteria.getOpintoalaTunnusIn()),
                                toimikuntaCriteria.isOpintoalaUsed())
                        .applyTo(new Criteria()).getCriteriaObject());
            conditions.add(new Criteria("_id").in(toimikuntasBySopimusehdot));
        }

        return getMongoOperations().find(Query.query(conditions.applyTo(new Criteria()))
                .with(new Sort("nimi." + orberByNimi.getAituKieli())), AituToimikunta.class);
    }

    @Override
    public List<String> findVoimassaOlevatRoolit() {
        return getMongoOperations().getCollection(AituToimikunta.class.getAnnotation(Document.class).collection())
                .distinct("jasenyydet.rooli", Criteria.where("jasenyydet.voimassa").is(true).getCriteriaObject());
    }
}
