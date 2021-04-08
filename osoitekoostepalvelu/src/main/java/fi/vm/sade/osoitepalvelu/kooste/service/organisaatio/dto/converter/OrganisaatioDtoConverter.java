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
import java.util.stream.Collectors;

@Component
public class OrganisaatioDtoConverter extends AbstractDtoConverter {
    private static final long serialVersionUID = -3701212052731186195L;

    public OrganisaatioYhteystietoHakuResultDto convert(OrganisaatioDetails from, OrganisaatioYhteystietoHakuResultDto to) {
        convertValue(from, to);
        to.setPostiosoite(resolveAddresses(OrganisaatioYhteysosoiteDto.OsoiteTyyppi.posti.name(), from.getYhteystiedot()));
        to.setKayntiosoite(resolveAddresses(OrganisaatioYhteysosoiteDto.OsoiteTyyppi.kaynti.name(), from.getYhteystiedot()));
        to.setKriisitiedotuksenEmail(resolveKriisitiedoituksenEmail(from.getYhteystietoArvos()));
        return to;
    }

    private List<OrganisaatioYhteysosoiteDto> resolveAddresses(String osoitetyyppi, List<OrganisaatioDetailsYhteystietoDto> yhteystiedot) {
        return yhteystiedot
                .stream()
                .filter(yhteystieto -> osoitetyyppi.equals(yhteystieto.getOsoiteTyyppi()))
                .map(yhteystieto -> convert(yhteystieto, new OrganisaatioYhteysosoiteDto()))
                .collect(Collectors.toList());
    }

    private String resolveKriisitiedoituksenEmail(List<OrganisaatioYhteystietoElementtiDto> yhteystietoArvos) {
        return yhteystietoArvos
                .stream()
                .filter(yhteystietoArvo -> yhteystietoArvo.getElementtiTyyppi() != null)
                .filter(yhteystietoArvo -> yhteystietoArvo.getElementtiTyyppi() != null)
                .filter(yhteystietoArvo -> "Kriisitiedotuksen sähköpostiosoite".equals(yhteystietoArvo.getTyyppiNimi()))
                .findFirst()
                .map(yhteystietoArvo -> yhteystietoArvo.getArvo())
                .orElseGet(() -> null);
    }
}
