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

package fi.vm.sade.osoitepalvelu.kooste.service.email;

import java.io.Serializable;
import java.util.Set;

import fi.vm.sade.osoitepalvelu.kooste.mvc.dto.EmailSettingsParametersDto;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.EmailSendSettingsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.MyInformationDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SourceRegister;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 11:23 AM
 */
public interface EmailService extends Serializable {

    /**
     * @param parameters the email parameters
     * @return the email send settings for Viestipalvelu.
     */
    EmailSendSettingsDto getEmailSendSettings(EmailSettingsParametersDto parameters);

}
