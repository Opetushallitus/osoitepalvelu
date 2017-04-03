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

import com.google.common.base.Function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 2:15 PM
 */
public class HenkiloYhteystietoRyhmaDto implements Serializable {
    private static final long serialVersionUID = -4818147124155038149L;

    public static final String TYOOSOITE_KUVAUS = "yhteystietotyyppi2";

    private Long id;
    private String ryhmaKuvaus;
    private String ryhmaAlkuperaTieto;
    private boolean readOnly;
    private List<HenkiloYhteystietoDto> yhteystieto = new ArrayList<HenkiloYhteystietoDto>();

    public static final Function<HenkiloYhteystietoRyhmaDto, String> SAHKOPOSTI
            = new Function<HenkiloYhteystietoRyhmaDto, String>() {
        public String apply(HenkiloYhteystietoRyhmaDto from) {
            return from.findArvo(HenkiloYhteystietoDto.YHTESYTIETO_SAHKOPOSTI); // NOSONAR (http://sourceforge.net/p/findbugs/bugs/1139/)
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRyhmaKuvaus() {
        return ryhmaKuvaus;
    }

    public void setRyhmaKuvaus(String ryhmaKuvaus) {
        this.ryhmaKuvaus = ryhmaKuvaus;
    }

    public String getRyhmaAlkuperaTieto() {
        return ryhmaAlkuperaTieto;
    }

    public void setRyhmaAlkuperaTieto(String ryhmaAlkuperaTieto) {
        this.ryhmaAlkuperaTieto = ryhmaAlkuperaTieto;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public List<HenkiloYhteystietoDto> getYhteystieto() {
        return yhteystieto;
    }

    public void setYhteystieto(List<HenkiloYhteystietoDto> yhteystieto) {
        this.yhteystieto = yhteystieto;
    }

    public String findArvo(String yhteystietoTyyppi) {
        for (HenkiloYhteystietoDto yhteystieto : this.yhteystieto) {
            if (yhteystietoTyyppi.equals(yhteystieto.getYhteystietoTyyppi())) {
                return yhteystieto.getYhteystietoArvo();
            }
        }
        return null;
    }
}
