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

package fi.vm.sade.osoitepalvelu.kooste.route.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 1:48 PM
 */
public class HenkiloDetailsDto extends HenkiloListResultDto implements Serializable {
    private static final long serialVersionUID = 501895048885418517L;

    public String getNimi() {
        String kutsumanimi = getKutsumanimi();
        if (kutsumanimi == null) {
            return this.getEtunimet() + " " + this.getSukunimi();
        } else {
            return kutsumanimi + " " + this.getSukunimi();
        }
    }

    public List<HenkiloYhteystietoRyhmaDto> getTyoOsoitees() {
        List<HenkiloYhteystietoRyhmaDto> tyoOsoittees = new ArrayList<HenkiloYhteystietoRyhmaDto>();
        for (HenkiloYhteystietoRyhmaDto ryhma: getYhteystiedotRyhma()) {
            if (HenkiloYhteystietoRyhmaDto.TYOOSOITE_KUVAUS.equals(ryhma.getRyhmaKuvaus())) {
                tyoOsoittees.add(ryhma);
            }
        }
        return tyoOsoittees;
    }
}
