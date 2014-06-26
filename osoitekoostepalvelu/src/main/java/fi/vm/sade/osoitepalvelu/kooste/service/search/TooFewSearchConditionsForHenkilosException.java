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

package fi.vm.sade.osoitepalvelu.kooste.service.search;

import fi.vm.sade.osoitepalvelu.kooste.common.exception.SelfExplainingException;

/**
 * User: ratamaa
 * Date: 5/21/14
 * Time: 10:24 AM
 */
public class TooFewSearchConditionsForHenkilosException extends Exception
        implements SelfExplainingException {
    private static final long serialVersionUID = -8800592326716820202L;

    @Override
    public String getMessageKey() {
        return "too_few_search_conditions_for_henkilos";
    }

    @Override
    public Object[] getMessageParams() {
        return new Object[0];
    }

    @Override
    public String getErrorCode() {
        return "302";
    }
}
