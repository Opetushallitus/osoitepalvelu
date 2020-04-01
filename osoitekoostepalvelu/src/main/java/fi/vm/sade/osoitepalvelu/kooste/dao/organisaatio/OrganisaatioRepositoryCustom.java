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
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface OrganisaatioRepositoryCustom {

    List<Organisaatio> findOrganisaatios(OrganisaatioYhteystietoCriteriaDto criteria,
                                                Locale orderByLocale);

    List<Organisaatio> findOrganisaatiosByOids(List<String> oids, Locale orderByLocale);

    List<Organisaatio> findChildren(Collection<String> parentOids,
                                           OrganisaatioYhteystietoCriteriaDto organisaatioCriteria,
                                           Locale orderByLocale);
}
