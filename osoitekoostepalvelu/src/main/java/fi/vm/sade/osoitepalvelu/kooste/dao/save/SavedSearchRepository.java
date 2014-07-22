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

import fi.vm.sade.osoitepalvelu.kooste.domain.SavedSearch;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:28 PM
 */
public interface SavedSearchRepository extends MongoRepository<SavedSearch, Long>, Serializable {

    List<SavedSearch> findByOwnerUsername(String ownerUsername, Sort order);

    SavedSearch saveNew(SavedSearch savedSearch);

}
