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

package fi.vm.sade.osoitepalvelu.kooste.service.organisaatio.dto.converter;

import fi.vm.sade.osoitepalvelu.kooste.common.dtoconverter.AbstractDtoConverter;
import fi.vm.sade.osoitepalvelu.kooste.domain.OrganisaatioDetails;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioDetailsYhteystietoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteysosoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoElementtiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.OrganisaatioYhteystietoHakuResultDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: ratamaa
 * Date: 3/25/14
 * Time: 11:40 AM
 */
@Component
public class OrganisaatioDtoConverter extends AbstractDtoConverter {
    private static final long serialVersionUID = -3701212052731186195L;

    // TODO Kommentti
    public OrganisaatioYhteystietoHakuResultDto convert(OrganisaatioDetails from, OrganisaatioYhteystietoHakuResultDto to) {
        convertValue(from, to);
        for (OrganisaatioDetailsYhteystietoDto yhteystieto : from.getYhteystiedot()) {
            List<OrganisaatioYhteysosoiteDto> target = null;
            if (yhteystieto.getOsoiteTyyppi() != null && yhteystieto.getOsoiteTyyppi().endsWith("posti")) {
                target = to.getPostiosoite();
            } else if (yhteystieto.getOsoiteTyyppi() != null && yhteystieto.getOsoiteTyyppi().endsWith("kaynti")) {
                target = to.getKayntiosoite();
            }
            if (target != null) {
                OrganisaatioYhteysosoiteDto yhteystietoTargetDto = convert(yhteystieto, new OrganisaatioYhteysosoiteDto());
                target.add(yhteystietoTargetDto);
            }
        }
        for (OrganisaatioYhteystietoElementtiDto yhteystietoElementtiDto : from.getYhteystietoArvos()) {
            List<OrganisaatioYhteystietoElementtiDto> target = null;
            // Kriisiviestintä
            if(yhteystietoElementtiDto.getElementtiTyyppi() != null
                    && yhteystietoElementtiDto.getTyyppiNimi().endsWith("Kriisitiedotuksen sähköpostiosoite")) {
                target = to.getKriisitiedotuksenEmail();
            }
            if(target != null) {
                target.add(yhteystietoElementtiDto);
            }
        }
        return to;
    }

}
