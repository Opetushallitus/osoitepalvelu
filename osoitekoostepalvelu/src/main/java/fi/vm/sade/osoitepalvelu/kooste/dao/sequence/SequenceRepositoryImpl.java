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

package fi.vm.sade.osoitepalvelu.kooste.dao.sequence;

// import fi.vm.sade.osoitepalvelu.kooste.domain.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceRepositoryImpl implements SequenceRepository {
    private static final long serialVersionUID = -8315391466293877817L;

    private static final String SAVED_SEARCH_ID_SEQUENCE_NAME  =  "savedSearch";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public long getNextSavedSearchIdSequence() {
        return increaseCounter(SAVED_SEARCH_ID_SEQUENCE_NAME);
    }

    public long increaseCounter(String sequenceName) {
        String sqlUpdate = "UPDATE sequence SET sequence = sequence + 1 where name = :sequencename";
        String sqlFind = "SELECT sequence FROM sequence where name = :sequencename";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("sequencename", sequenceName);

        namedParameterJdbcTemplate.update(sqlUpdate, mapSqlParameterSource);
        return namedParameterJdbcTemplate.query(sqlFind, mapSqlParameterSource);
        /*
        Query query  =  new Query(Criteria.where("name").is(sequenceName));
        Update update  =  new Update().inc("sequence", 1);
        Sequence seq  =  mongoTemplate.findAndModify(query, update, Sequence.class);
        if (seq == null) {
            seq = new Sequence();
            seq.setName(sequenceName);
            seq.setSequence(1L);
            mongoTemplate.save(seq);
        }
        return seq.getSequence();
        */
    }
}
