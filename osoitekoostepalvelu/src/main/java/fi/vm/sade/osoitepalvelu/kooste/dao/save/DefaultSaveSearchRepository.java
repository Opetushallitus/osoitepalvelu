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

package fi.vm.sade.osoitepalvelu.kooste.dao.save;

import fi.vm.sade.osoitepalvelu.kooste.dao.sequence.SequenceRepository;
import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: ratamaa
 * Date: 12/11/13
 * Time: 3:34 PM
 */
@Repository
public class DefaultSaveSearchRepository extends SimpleMongoRepository<SavedSearch, Long> implements
        SavedSearchRepository {
    @Autowired
    private SequenceRepository sequenceRepository;

    public DefaultSaveSearchRepository(MongoEntityInformation<SavedSearch, Long> metadata,
            MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
    }

    @Autowired
    public DefaultSaveSearchRepository(MongoRepositoryFactory factory, MongoOperations mongoOperations) {
        this(factory.<SavedSearch, Long> getEntityInformation(SavedSearch.class), mongoOperations);
    }

    @Override
    public List<SavedSearch> findByOwnerUsername(String ownerUsername, Sort order) {
        Criteria criteria = new Criteria()
                                    .where("ownerUserOid")
                                        .is(ownerUsername);
        return getMongoOperations().find(Query.query(criteria).with(order), SavedSearch.class);
    }

    @Override
    public SavedSearch saveNew(SavedSearch savedSearch) {
        savedSearch.setId(sequenceRepository.getNextSavedSearchIdSequence());
        return super.save(savedSearch);
    }
}
