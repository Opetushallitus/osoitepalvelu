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

package fi.vm.sade.osoitepalvelu.kooste.route;

import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoVersioDto;

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/12/14
 * Time: 3:13 PM
 */
public interface KoodistoRoute extends Serializable {

    /**
     * Hakee tietyn koodiston kaikki koodit annetun koodistotyypin perusteella.
     *
     * @param koodistoTyyppi
     *            Haettavan koodiston tyyppi.
     * @return Lista koodeista, jotka kuuluvat valittuun koodistoon.
     */
    List<KoodiDto> findKooditKoodistonTyyppilla(KoodistoDto.KoodistoTyyppi koodistoTyyppi);

    /**
     * Hakee tietyn koodin versiot.
     *
     * @param koodistoTyyppi
     * @return
     */
    List<KoodistoVersioDto> findKoodistonVersiot(KoodistoDto.KoodistoTyyppi koodistoTyyppi);

    List<KoodiDto> findKooditKoodistonVersiolleTyyppilla(KoodistoDto.KoodistoTyyppi koodistoTyyppi, long versio);

    /**
     * @param koodiUri the koodiUri to get children for
     * @return the koodis
     */
    List<KoodiDto> findKoodisByParent(String koodiUri);

    /**
     * @param koodiUri the koodiUri to get parents for
     * @return the koodis
     */
    List<KoodiDto> findKoodisByChild(String koodiUri);

    boolean isFindCounterUsed();

    void setFindCounterUsed(boolean findCounterUsed);

    long getFindCounterValue();
}
