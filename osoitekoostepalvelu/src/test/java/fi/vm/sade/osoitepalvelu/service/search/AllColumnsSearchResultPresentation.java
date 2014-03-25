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

package fi.vm.sade.osoitepalvelu.service.search;

import fi.vm.sade.osoitepalvelu.kooste.service.search.SearchResultPresentation;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.OidAndTyyppiPair;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 12:50 PM
 */
public class AllColumnsSearchResultPresentation implements SearchResultPresentation, Serializable {
    private Locale locale;
    private Set<OidAndTyyppiPair> nonIncludedOids;

    public AllColumnsSearchResultPresentation() {
    }

    public AllColumnsSearchResultPresentation(Locale locale) {
        this.locale = locale;
    }

    public AllColumnsSearchResultPresentation(Locale locale, Set<OidAndTyyppiPair> nonIncludedOids) {
        this(locale);
        this.nonIncludedOids = nonIncludedOids;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public boolean isResultRowIncluded(SearchResultRowDto row) {
        return this.nonIncludedOids == null || !this.nonIncludedOids.contains(row.getOidAndTyyppiPair());
    }

    @Override
    public boolean isOrganisaationNimiIncluded() {
        return true;
    }

    @Override
    public boolean isOrganisaatiotunnisteIncluded() {
        return true;
    }

    @Override
    public boolean isYhteyshenkiloIncluded() {
        return true;
    }

    @Override
    public boolean isPositosoiteIncluded() {
        return true;
    }

    @Override
    public boolean isKatuosoiteIncluded() {
        return true;
    }

    @Override
    public boolean isPLIncluded() {
        return true;
    }

    @Override
    public boolean isPostinumeroIncluded() {
        return true;
    }

    @Override
    public boolean isPuhelinnumeroIncluded() {
        return true;
    }

    @Override
    public boolean isFaksinumeroIncluded() {
        return true;
    }

    @Override
    public boolean isWwwOsoiteIncluded() {
        return true;
    }

    @Override
    public boolean isViranomaistiedotuksenSahkopostiosoiteIncluded() {
        return true;
    }

    @Override
    public boolean isKoulutusneuvonnanSahkopostiosoiteIncluded() {
        return true;
    }

    @Override
    public boolean isKriisitiedotuksenSahkopostiosoiteIncluded() {
        return true;
    }

    @Override
    public boolean isOrganisaationSijaintikuntaIncluded() {
        return true;
    }

    @Override
    public boolean isYhteyshenkiloEmailIncluded() {
        return true;
    }
}
