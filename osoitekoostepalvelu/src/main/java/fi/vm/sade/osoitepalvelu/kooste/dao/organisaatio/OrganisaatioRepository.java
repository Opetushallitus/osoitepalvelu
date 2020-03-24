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

import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoCriteriaDto;
import org.joda.time.DateTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jdbc.repository.query.Query;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface OrganisaatioRepository extends Serializable, CrudRepository<OrganisaatioDetails, Long> {

    List<OrganisaatioDetails> findOrganisaatios(OrganisaatioYhteystietoCriteriaDto criteria,
                                                Locale orderByLocale);

    List<OrganisaatioDetails> findOrganisaatiosByOids(List<String> oids, Locale orderByLocale);

    List<OrganisaatioDetails> findChildren(Collection<String> parentOids,
                                           OrganisaatioYhteystietoCriteriaDto organisaatioCriteria,
                                           Locale orderByLocale);

    @Query(value = "SELECT cachedAt FROM organisaatiodetails ORDER BY cachedAt asc LIMIT 1")
    DateTime findOldestCachedEntry();

    @Query(value = "SELECT oid FROM organisaatiodetails")
    List<String> findAllOids();

    @Query(value = "SELECT oid FROM organisaatiodetails WHERE oppilaitoskoodi = :oppilaitosKoodi LIMIT 1")
    String findOidByOppilaitoskoodi(@Param("oppilaitosKoodi") String oppilaitosKoodi);

    @Query(value = "SELECT * FROM organisaatiodetails WHERE ytunnus = :yTunnus LIMIT 1")
    Optional<OrganisaatioDetails> findByYtunnus(@Param("yTunnus") String yTunnus);
}
