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
import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.FilterableOrganisaatio;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.Collections.emptyList;

public class OrganisaatioRepositoryCustomImpl implements OrganisaatioRepositoryCustom {
    private static final long serialVersionUID = 3725230640750779168L;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public List<OrganisaatioDetails> findOrganisaatios(OrganisaatioYhteystietoCriteriaDto organisaatioCriteria,
                                                       Locale orderByLocale) {
        String sql = "SELECT * FROM organisaatiodetails where";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        filterByCriteria(organisaatioCriteria, sql, mapSqlParameterSource);

        return namedParameterJdbcTemplate.queryForList(sql, mapSqlParameterSource, OrganisaatioDetails.class);
        //return mongoOperations.find(Query.query(conditions.applyTo(new Criteria())).with(Sort.by("nimi."
        //                +orderByLocale.getLanguage().toLowerCase())), OrganisaatioDetails.class);
    }

    @Override
    public List<OrganisaatioDetails> findOrganisaatiosByOids(List<String> oids, Locale orderByLocale) {
        if (oids == null || oids.isEmpty()) {
            return new ArrayList<OrganisaatioDetails>();
        }

        String sql = "SELECT * FROM organisaatiodetails where oid In (:oidit) ORDER BY nimi."
                + orderByLocale.getLanguage().toLowerCase();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("oidit", oids);
        return namedParameterJdbcTemplate.queryForList(sql, mapSqlParameterSource, OrganisaatioDetails.class);

        //return mongoOperations.find(Query.query(new Criteria("oid").in(oids)).with(Sort.by("nimi."
         //       +orderByLocale.getLanguage().toLowerCase())), OrganisaatioDetails.class);

    }

    private void filterByCriteria(OrganisaatioYhteystietoCriteriaDto organisaatioCriteria, String sql, MapSqlParameterSource mapSqlParameterSource) {
        if (organisaatioCriteria.isVainAktiiviset()) {
            sql += " AND lakkautusPvm IS NULL OR lakkautusPvm > :dateNow";
            mapSqlParameterSource.addValue("dateNow", new LocalDate().toDateTimeAtStartOfDay().toDate());
        }
        if (organisaatioCriteria.isOrganisaatioTyyppiUsed()) {
            sql += " AND tyypit IN (:organisaatioTyypit)";
            mapSqlParameterSource.addValue("organisaatioTyypit", organisaatioCriteria.getOrganisaatioTyyppis());
        }
        if (organisaatioCriteria.isOidUsed()) {
            sql += " AND oid IN (:oidit)";
            mapSqlParameterSource.addValue("oidit", organisaatioCriteria.getOidList());
        }
        if (organisaatioCriteria.isKuntaUsed()) {
            sql += " AND kotipaikkaUri IN (:kotipaikkaUrit)";
            mapSqlParameterSource.addValue("kotipaikkaUri", organisaatioCriteria.getKuntaList());
        }
        if (organisaatioCriteria.isYtunnusUsed()) {
            sql += " AND ytunnus IN (:ytunnukset)";
            mapSqlParameterSource.addValue("ytunnukset", organisaatioCriteria.getYtunnusList());
        }
        /* TODO how to implement
        if (organisaatioCriteria.isKoulutuslupaExistsUsed()) {
            if (organisaatioCriteria.getKoulutusLupaExists()) {
                conditions.add(new Criteria("koulutusluvat").exists(true).ne(emptyList()));
            } else {
                conditions.add(new Criteria("koulutusluvat").exists(true).size(0));
            }
        } */
        if (organisaatioCriteria.isKoulutuslupaUsed()) {
            sql += " AND koulutusluvat IN (:koulutusluvat)";
            mapSqlParameterSource.addValue("koulutusluvat", organisaatioCriteria.getKoulutuslupaList());
            // conditions.add(new Criteria("koulutusluvat").in(organisaatioCriteria.getKoulutuslupaList()));
        }
        /* TODO
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
        */
    }

    @Override
    public List<OrganisaatioDetails> findChildren(Collection<String> parentOids,
                      OrganisaatioYhteystietoCriteriaDto organisaatioCriteria, Locale orderByLocale) {
        if (parentOids == null || parentOids.isEmpty()) {
            return new ArrayList<OrganisaatioDetails>();
        }
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        String parentOidSql = "";
        int i = 0;
        for (String oid : parentOids) {
            //criteria[i++] = Criteria.where(key).regex(Pattern.compile("\\|"+oid+"\\|"));
            parentOidSql = parentOidSql + "parentOidPath LIKE %|:oid"+(i++)+"|% OR ";
            mapSqlParameterSource.addValue("oid"+(i++), oid);
        }

        String sql = "SELECT * FROM organisaatiodetails where " + parentOidSql;
        filterByCriteria(organisaatioCriteria, sql, mapSqlParameterSource);

       return namedParameterJdbcTemplate.queryForList(sql, mapSqlParameterSource, OrganisaatioDetails.class);

        //conditions.add(CriteriaHelper.inParentOids(new Criteria(), "parentOidPath", parentOids));

        //return mongoOperations.find(Query.query(conditions.applyTo(new Criteria())).with(Sort.by("nimi."
        //        +orderByLocale.getLanguage().toLowerCase())), OrganisaatioDetails.class);
    }
/*
    @Override
    public DateTime findOldestCachedEntry() {

        Query q = Query.query(new Criteria());
        q.fields().include("cachedAt");
        q.limit(1);
        q.with(Sort.by(Sort.Direction.ASC, "cachedAt"));
        List<OrganisaatioDetails> list = mongoOperations.find(q, OrganisaatioDetails.class);
        if(list.size() == 1) {
            return list.get(0).getCachedAt();
        } else {
            return null;
        }
    }

    @Override
    public List<String> findAllOids() {
        Query q = Query.query(new Criteria());
        q.fields().include("_id");
        return new ArrayList<String>(Collections2.transform(mongoOperations
                .find(q, OrganisaatioDetails.class), FilterableOrganisaatio.GET_OID));
    }
    */
    /*
    @Override
    public String findOidByOppilaitoskoodi(String oppilaitosKoodi) {
        List<String> oids = mongoOperations.findDistinct(Query.query(Criteria.where("oppilaitosKoodi").is(oppilaitosKoodi)),"_id",OrganisaatioDetails.class,String.class);
        if (oids == null || oids.isEmpty()) {
            return null;
        }
        return oids.get(0);
    }
    */

}
