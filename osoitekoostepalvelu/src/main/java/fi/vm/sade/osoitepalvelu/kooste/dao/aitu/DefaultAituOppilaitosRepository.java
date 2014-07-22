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
import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.common.util.CriteriaHelper;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituOppilaitosCriteria;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituOppilaitos;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.AituSopimusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ratamaa on 15.4.2014.
 */
@Repository
public class DefaultAituOppilaitosRepository extends SimpleMongoRepository<AituOppilaitos, String>
        implements AituOppilaitosRepository {

    @Autowired
    private AituToimikuntaRepository aituToimikuntaRepository;

    public DefaultAituOppilaitosRepository(MongoEntityInformation<AituOppilaitos, String> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
    }

    @Autowired
    public DefaultAituOppilaitosRepository(MongoRepositoryFactory factory, MongoOperations mongoOperations) {
        this(factory.<AituOppilaitos, String>getEntityInformation(AituOppilaitos.class), mongoOperations);
    }

    @Override
    public List<AituOppilaitos> findOppilaitos(AituOppilaitosCriteria oppilaitosCriteria, AituKielisyys orberByNimi) {
        CriteriaHelper.Conditions conditions = new CriteriaHelper.Conditions()
                .addGiven(new Criteria("oppilaitoskoodi").in(oppilaitosCriteria.getOppilaitoskoodiIn()),
                        oppilaitosCriteria.isOppilaitoskoodiUsed())
                .addGiven(new Criteria("sopimukset.tutkinnot.tutkintotunnus").in(oppilaitosCriteria.getTutkintoTunnusIn()),
                        oppilaitosCriteria.isTutkintoUsed())
                .addGiven(new Criteria("sopimukset.tutkinnot.opintoalatunnus").in(oppilaitosCriteria.getOpintoalaTunnusIn()),
                        oppilaitosCriteria.isOpintoalaUsed());
        Query query = Query.query(conditions.applyTo(new Criteria()));
        if (orberByNimi != null) {
            query = query.with(new Sort("nimi." + orberByNimi.getAituKieli()));
        }
        return getMongoOperations().find(query, AituOppilaitos.class);
    }

    @Override
    public List<AituSopimusDto> findMatchingSopimukset(AituOppilaitosCriteria oppilaitosCriteria, List<String> matchedToimikuntas,
                                                       AituKielisyys orberByNimi) {
        List<AituOppilaitos> oppilaitos = findOppilaitos(oppilaitosCriteria, orberByNimi);
        if (oppilaitosCriteria.isRelatedToimikuntaResultsNeeded() && matchedToimikuntas == null) {
            matchedToimikuntas = aituToimikuntaRepository.findToimikuntaIds(oppilaitosCriteria.toRelatedToimikuntaCriteria());
        }
        return new ArrayList<AituSopimusDto>(Collections2.filter(CollectionHelper.collect(oppilaitos, AituOppilaitos.SOPIMUKSET),
                oppilaitosCriteria.createSopimusPredicate(matchedToimikuntas)));
    }

}
