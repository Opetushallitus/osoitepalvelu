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

import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituToimikuntaCriteria;
import fi.vm.sade.osoitepalvelu.kooste.domain.AituToimikunta;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ratamaa on 15.4.2014.
 */
public interface AituToimikuntaRepository extends MongoRepository<AituToimikunta,String> {

    /**
     * @param toimikuntaCriteria
     * @return all AituToimikuntas
     */
    List<AituToimikunta> findToimikuntas(AituToimikuntaCriteria toimikuntaCriteria, AituKielisyys orderByNimi);

    /**
     * @param toimikuntaCriteria
     * @return all ids of AituToimikuntas
     */
    List<String> findToimikuntaIds(AituToimikuntaCriteria toimikuntaCriteria);

    List<String> findVoimassaOlevatRoolit();
}
