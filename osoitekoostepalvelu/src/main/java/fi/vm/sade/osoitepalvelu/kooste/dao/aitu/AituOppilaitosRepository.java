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

import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituOppilaitosCriteria;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituOppilaitos;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.AituSopimusDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ratamaa on 15.4.2014.
 */
public interface AituOppilaitosRepository extends MongoRepository<AituOppilaitos, String> {

    /**
     * @param oppilaitosCriteria to filter AituOppilaitos with
     * @param orberByNimi name field to order the results by
     * @return the list of AituOppilaitos
     */
    List<AituOppilaitos> findOppilaitos(AituOppilaitosCriteria oppilaitosCriteria, AituKielisyys orberByNimi);

    /**
     * @param oppilaitosCriteria to filter AituOppilaitos and sopimukset with
     * @param orberByNimi name field of AituOppilaitos to sort the results by (secondary sort order by sopimus order)
     * @return the list of mathcing AituSopimusDto from AituOppilaitos matching the criteria
     */
    List<AituSopimusDto> findMatchingSopimukset(AituOppilaitosCriteria oppilaitosCriteria, AituKielisyys orberByNimi);
}
