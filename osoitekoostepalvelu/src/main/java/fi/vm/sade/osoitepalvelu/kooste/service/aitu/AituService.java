package fi.vm.sade.osoitepalvelu.kooste.service.aitu;
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

import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.AituOsoitepalveluResultsDto;

/**
 * Created by ratamaa on 15.4.2014.
 */
public interface AituService {

    /**
     * @param results to override the data in MongoDB with
     */
    void refreshData( AituOsoitepalveluResultsDto results );

    /**
     * Refreshes the data in MongoDB repository by fetching new data
     * from AITU route.
     *
     * @param requestContext for the AITU route
     */
    void refreshData( CamelRequestContext requestContext );

}
