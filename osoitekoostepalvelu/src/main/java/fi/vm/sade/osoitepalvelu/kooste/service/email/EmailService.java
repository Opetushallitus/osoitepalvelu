package fi.vm.sade.osoitepalvelu.kooste.service.email;

import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.EmailSendSettingsDto;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 11:23 AM
 */
public interface EmailService {

    /**
     * @return the email send settings for Viestipalvelu.
     */
    EmailSendSettingsDto getEmailSendSettings();

}
