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

import fi.vm.sade.osoitepalvelu.kooste.domain.Organisaatio;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoCriteriaDto;
import org.joda.time.DateTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Repository
public interface OrganisaatioRepository extends CrudRepository<Organisaatio, Long>, Serializable, OrganisaatioRepositoryCustom{

    @Query(value = "SELECT cachedAt FROM organisaatio ORDER BY cachedAt asc LIMIT 1")
    DateTime findOldestCachedEntry();

    @Query(value = "SELECT oid FROM organisaatio")
    List<String> findAllOids();

    @Query(value = "SELECT oid FROM organisaatio WHERE oppilaitoskoodi = :oppilaitosKoodi LIMIT 1")
    String findOidByOppilaitoskoodi(@Param("oppilaitosKoodi") String oppilaitosKoodi);

    @Query(value = "SELECT * FROM organisaatio WHERE ytunnus = :yTunnus LIMIT 1")
    Optional<Organisaatio> findByYtunnus(@Param("yTunnus") String yTunnus);

}
