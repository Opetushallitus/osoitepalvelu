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

package fi.vm.sade.osoitepalvelu.kooste.mvc.dto;

import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.MyInformationDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SourceRegister;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * User: ratamaa
 * Date: 17.9.2014
 * Time: 12:13
 */
public class EmailSettingsParametersDto implements Serializable {
    @ApiModelProperty("Käyttäjän omat tiedot /cas/me:n palauttamassa muodossa.")
    private MyInformationDto me;
    @ApiModelProperty("Käytetyt lähderekisterit.")
    private Set<SourceRegister> sourceRegisters = new HashSet<SourceRegister>();
    @ApiModelProperty("Haussa käytetty esityskieli")
    private Locale language;

    public MyInformationDto getMe() {
        return me;
    }

    public void setMe(MyInformationDto me) {
        this.me = me;
    }

    public Set<SourceRegister> getSourceRegisters() {
        return sourceRegisters;
    }

    public void setSourceRegisters(Set<SourceRegister> sourceRegisters) {
        this.sourceRegisters = sourceRegisters;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }
}
