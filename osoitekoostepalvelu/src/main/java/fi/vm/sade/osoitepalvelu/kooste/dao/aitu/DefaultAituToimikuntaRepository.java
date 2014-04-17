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

import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituToimikuntaCriteria;
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
        Criteria criteria = new Criteria();
        if (toimikuntaCriteria.isKielisyysUsed()) {
            criteria = criteria.and("kielisyys").in(AituKielisyys.aituKielis(toimikuntaCriteria.getKielisyysIn()));
        }
        if (toimikuntaCriteria.isJasenRoolisUsed()) {
            criteria = criteria.and("jasenyydet.rooli").in(toimikuntaCriteria.getJasensInRoolis());
        }
        if (toimikuntaCriteria.isIdsUsed()) {
            criteria = criteria.and("id").in(toimikuntaCriteria.getIdsIn());
        }
        if (toimikuntaCriteria.isOnlyVoimassaOlevat()) {
            criteria = criteria.and("jasenyydet.voimassa").is(true);
        }
        return getMongoOperations().find(Query.query(criteria).with(new Sort("nimi."+orberByNimi.getAituKieli())),
                AituToimikunta.class);
    }

    @Override
    public List<String> findVoimassaOlevatRoolit() {
        return getMongoOperations().getCollection(AituToimikunta.class.getAnnotation(Document.class).collection())
                .distinct("jasenyydet.rooli", Criteria.where("jasenyydet.voimassa").is(true).getCriteriaObject());
    }
}
