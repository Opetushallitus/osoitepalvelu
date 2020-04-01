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

package fi.vm.sade.osoitepalvelu.kooste.service.organisaatio;

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public interface OrganisaatioService extends Serializable {

    /**
     * @param criteria for organisaatios' yhteystietos
     * @param locale to use when sorting results by name
     * @param requestContext the context for HTTP request received by the application to operate in
     * @return the yhteystietos for the organisaatios matching the search criteria
     */
    List<OrganisaatioYhteystietoHakuResultDto> findOrganisaatioYhteystietos(
            OrganisaatioYhteystietoCriteriaDto criteria,
            Locale locale, CamelRequestContext requestContext);

    /**
     * @param oid of the organisaatio
     * @param requestContext the context for HTTP request received by the application to operate in
     * @return details for the organisaatio
     */
    OrganisaatioDetailsDto getdOrganisaatioByOid(String oid, CamelRequestContext requestContext);

    /**
     * @param oid of the organisaatio to purge from cache
     */
    void purgeOrganisaatioByOidCache(String oid);

    /**
     * @return all organisaatios in cache
     */
    List<String> findAllOidsOfCachedOrganisaatios();

    /**
     * @param oppilaitosKoodi to search OID for
     * @return the first found organisaatio OID by given oppilaitoskoodi or null if not found
     */
    String findOidByOppilaitoskoodi(String oppilaitosKoodi);
}
