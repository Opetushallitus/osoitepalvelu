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

import com.wordnik.swagger.annotations.ApiModelProperty;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;

import java.util.Locale;

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 10:23 AM
 */
public interface SearchResultPresentation {

    @ApiModelProperty("Hakutulosten esityskieli")
    Locale getLocale();

    boolean isResultRowIncluded(SearchResultRowDto row);

    boolean isOrganisaatioEmailOnlyEmailIncluded();

    @ApiModelProperty("Näytetäänkö organisaation nimi -saraketta")
    boolean isOrganisaationNimiIncluded();

    @ApiModelProperty("Näytetäänkö organisaatiotunnistesaraketta")
    boolean isOrganisaatiotunnisteIncluded();

    @ApiModelProperty("Näytetäänkö Y-tunnus -saraketta")
    boolean isYtunnusIncluded();

    @ApiModelProperty("Näytetäänkö yritysmuotosaraketta")
    boolean isYritysmuotoIncluded();

    @ApiModelProperty("Näytetäänkö yhteyshenkilösaraketta")
    boolean isYhteyshenkiloIncluded();

    @ApiModelProperty("Näyteätänkö postiosoitetta")
    boolean isPositosoiteIncluded();

    @ApiModelProperty("Näytetäänkö käyntiosoitetta")
    boolean isKayntiosoiteIncluded();

    @ApiModelProperty("Näytetäänkö puhelinnumerosaraketta")
    boolean isPuhelinnumeroIncluded();

    @ApiModelProperty("Näytetäänkö WWW-osoite-saraketta")
    boolean isWwwOsoiteIncluded();

    @ApiModelProperty("Näytetäänkö viranomaistiedotuksen sähköpostiosoite -saraketta")
    boolean isViranomaistiedotuksenSahkopostiosoiteIncluded();

    @ApiModelProperty("Näytetäänkö koulutusneuvonnan sähköpostiosoite -saraketta")
    boolean isKoulutusneuvonnanSahkopostiosoiteIncluded();

    @ApiModelProperty("Näytetäänkö kriisitiedotuksen sähköpostiosoite -saraketta")
    boolean isKriisitiedotuksenSahkopostiosoiteIncluded();

    @ApiModelProperty("Näytetäänkö varhaiskasvatuksen yhteyshenkilö -saraketta")
    boolean isVarhaiskasvatuksenYhteyshenkiloIncluded();

    @ApiModelProperty("Näytetäänkö varhaiskasvatuksen sähköposti -saraketta")
    boolean isVarhaiskasvatuksenEmailIncluded();

    @ApiModelProperty("Näytetäänkö organisaation sähköpostiosoite -saraketta")
    boolean isOrganisaatioEmailIncluded();

    @ApiModelProperty("Näytetäänkö organisaation sijaintikunta -saraketta")
    boolean isOrganisaationSijaintikuntaIncluded();

    @ApiModelProperty("Näytetäänkö yhteyshenkilön sähköpostiosoite -saraketta")
    boolean isYhteyshenkiloEmailIncluded();

    @ApiModelProperty("Onko haun kohderyhmissä näyttötutkinnon vastuuhenkilö")
    boolean isNayttotutkinnonJarjestajaVastuuhenkilosIncluded();

    @ApiModelProperty("Onko haun kohderyhmissä näyttötutkinnon järjestäjän organisaatio")
    boolean isNayttotutkinnonJarjestajaOrganisaatiosIncluded();
}
