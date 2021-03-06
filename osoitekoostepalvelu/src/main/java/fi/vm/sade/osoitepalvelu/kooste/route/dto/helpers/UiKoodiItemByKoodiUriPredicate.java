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

package fi.vm.sade.osoitepalvelu.kooste.route.dto.helpers;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.service.koodisto.dto.UiKoodiItemDto;

/**
 * User: ratamaa
 * Date: 3/20/14
 * Time: 2:23 PM
 */
public class UiKoodiItemByKoodiUriPredicate implements Predicate<UiKoodiItemDto> {
    private String uri;

    public UiKoodiItemByKoodiUriPredicate(String uri) {
        this.uri  =  uri;
    }

    @Override
    public boolean apply(UiKoodiItemDto item) {
        return EqualsHelper.areEquals(uri, item.getKoodiUri());
    }
}