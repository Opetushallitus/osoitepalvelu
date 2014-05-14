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

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.OrganisaatioYhteystietoHakuResultDto;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:32 AM
 */
public interface OrganisaatioRepository extends MongoRepository<OrganisaatioDetails, String> {

    List<OrganisaatioDetails> findOrganisaatios(OrganisaatioYhteystietoCriteriaDto criteria,
                                                Locale orderByLocale);

    List<OrganisaatioDetails> findOrganisaatiosByOids(List<String> oids, Locale orderByLocale);

    List<OrganisaatioDetails> findChildren(Collection<String> parentOids,
                                           OrganisaatioYhteystietoCriteriaDto organisaatioCriteria,
                                           Locale orderByLocale);

    DateTime findOldestCachedEntry();

    List<String> findAllOids();
}
