package fi.vm.sade.osoitepalvelu.kooste.service.email;

import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.EmailSendSettingsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 11:24 AM
 */
@Service
public class DefaultEmailService extends AbstractService implements EmailService {

    @Value("${viestipalvelu.emailsend.endpoint.uri}")
    private String emailSendEmailUrl;

    @Override
    public EmailSendSettingsDto getEmailSendSettings() {
        EmailSendSettingsDto settings = new EmailSendSettingsDto();
        settings.setEndpointUrl( emailSendEmailUrl );
        settings.getEmail().setSenderOid( getLoggedInUserOid() );
        // TODO: resolve currenlty logged in user's other details.
        return settings;
    }
}
