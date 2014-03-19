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

package fi.vm.sade.osoitepalvelu.kooste.common.route;

import org.apache.camel.model.ProcessorDefinition;

/**
 * User: ratamaa
 * Date: 3/19/14
 * Time: 1:53 PM
 */
public interface ProcessDefinitionProcessor {

    /**
     * @param process definition to be processed
     * @param <T> type for the process definition
     * @return the result process
     */
    <T extends ProcessorDefinition<? extends T>> T process(T process);
}
