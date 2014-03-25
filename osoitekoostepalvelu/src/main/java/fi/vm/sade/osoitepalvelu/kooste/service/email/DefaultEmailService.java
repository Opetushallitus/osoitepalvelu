package fi.vm.sade.osoitepalvelu.kooste.service.email;

import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.EmailSendSettingsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.AuthenticationServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.MyInformationDto;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${viestipalvelu.emailsend.calling.process:'Osoitetietojarjestelma'}")
    private String callingProcess;

    @Value("${viestipalvelu.emailsend.email.from:'oph_tiedotus@oph.fi'}")
    private String emailFrom;

    @Value("${viestipalvelu.emailsend.email.subject:''}")
    private String defaultSubject;

    @Value("${viestipalvelu.emailsend.email.body:''}")
    private String defaultBody;

    @Autowired
    private AuthenticationServiceRoute authenticationServiceRoute;

    @Override
    public EmailSendSettingsDto getEmailSendSettings() {
        EmailSendSettingsDto settings  =  new EmailSendSettingsDto();
        settings.setEndpointUrl(emailSendEmailUrl);
        settings.getEmail().setCallingProcess(callingProcess);
        settings.getEmail().setFrom(emailFrom);

        MyInformationDto myInfo  =  authenticationServiceRoute.getMe();
        settings.getEmail().setReplyTo(myInfo.getEmail());
        if (settings.getEmail().getReplyTo() == null) {
            settings.getEmail().setReplyTo(myInfo.getFirstName() + "." + myInfo.getLastName() + "@oph.fi");
        }

        return settings;
    }
}
