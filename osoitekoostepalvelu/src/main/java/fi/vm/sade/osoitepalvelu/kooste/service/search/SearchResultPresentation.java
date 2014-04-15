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

import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Locale;

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 10:23 AM
 */
public interface SearchResultPresentation {

    Locale getLocale();

    boolean isResultRowIncluded(SearchResultRowDto row);

    boolean isOrganisaationNimiIncluded();

    boolean isOrganisaatiotunnisteIncluded();

    boolean isYhteyshenkiloIncluded();

    boolean isPositosoiteIncluded();

    boolean isKayntiosoiteIncluded();

    boolean isPuhelinnumeroIncluded();

    boolean isFaksinumeroIncluded();

    boolean isWwwOsoiteIncluded();

    boolean isViranomaistiedotuksenSahkopostiosoiteIncluded();

    boolean isKoulutusneuvonnanSahkopostiosoiteIncluded();

    boolean isKriisitiedotuksenSahkopostiosoiteIncluded();

    boolean isOrganisaatioEmailIncluded();

    boolean isOrganisaationSijaintikuntaIncluded();

    boolean isYhteyshenkiloEmailIncluded();
}
