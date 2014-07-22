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

import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.EmailSendSettingsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.MyInformationDto;
import fi.vm.sade.osoitepalvelu.kooste.service.route.AuthenticationServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.HenkiloDetailsDto;
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

    @Value("${viestipalvelu.emailsend.email.organisaatioOid:''}")
    private String defaultOrganisaatioOid;

    @Autowired
    private AuthenticationServiceRoute authenticationServiceRoute;

    @Override
    public EmailSendSettingsDto getEmailSendSettings(MyInformationDto myInfo) {
        EmailSendSettingsDto settings  =  new EmailSendSettingsDto();
        settings.setEndpointUrl(emailSendEmailUrl);
        settings.getEmail().setCallingProcess(callingProcess);
        settings.getEmail().setFrom(emailFrom);

        HenkiloDetailsDto myDetails = authenticationServiceRoute.getHenkiloTiedot(myInfo.getOid(),
                new DefaultCamelRequestContext());
        log(read("henkilo",myInfo.getOid()));
        settings.getEmail().setOrganizationOid(
                myDetails.findFirstAktiivinenOrganisaatioOid().or(defaultOrganisaatioOid));

        settings.getEmail().setReplyTo(myInfo.getEmail());
        if (settings.getEmail().getReplyTo() == null) {
            settings.getEmail().setReplyTo(replaceSpecialCharacters(myInfo.getFirstName() + "." + myInfo.getLastName() + "@oph.fi"));
        }

        return settings;
    }

    /**
     * Korvaa ääkköset aalla ja oolla
     * @param email sähköpostiosoite
     * @return sama sähköpostiosoite, mutta muuttaa Ää=Aa ja Öö=Oo sekä laittaa kaikki pienillä kirjaimilla
     */
    private String replaceSpecialCharacters(String email) {
        if(email == null) {
            return null;
        }
        // Kaikki pieniksi kirjaimiksi ja ääkköset aakkosiksi
        email = email.toLowerCase();
        email = email.replaceAll("ä", "a");
        email = email.replaceAll("ö", "o");
        
        return email;
    }
}
