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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SaveSearchRepositoryImpl implements SavedSearchRepository {
    private static final long serialVersionUID = -1074205896498542579L;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private SequenceRepository sequenceRepository;
    /*
    @Override
    public List<SavedSearch> findByOwnerUsername(String ownerUsername, Sort order) {
        Criteria criteria  =  Criteria.where("ownerUserOid")
                                    .is(ownerUsername);
        return mongoOperations.find(Query.query(criteria).with(order), SavedSearch.class);
    }
    */

    @Override
    public Long saveNew(SavedSearch savedSearch) {
        savedSearch.setId(sequenceRepository.getNextSavedSearchIdSequence());
        save(savedSearch);
        return savedSearch.getId();
    }
}
