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

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import fi.vm.sade.osoitepalvelu.kooste.common.route.DefaultCamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.common.util.CollectionHelper;
import fi.vm.sade.osoitepalvelu.kooste.mvc.dto.EmailSettingsParametersDto;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.EmailSendSettingsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.EmailSourceRegisterDto;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.MyInformationDto;
import fi.vm.sade.osoitepalvelu.kooste.route.AuthenticationServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloDetailsDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.HenkiloYhteystietoRyhmaDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SourceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 11:24 AM
 */
@Service
public class DefaultEmailService extends AbstractService implements EmailService {
    private static final long serialVersionUID = -4706566607303050449L;

    @Value("${viestipalvelu.emailsend.endpoint.uri}")
    private String emailSendEmailUrl;

    @Value("${viestipalvelu.emailsend.calling.process:'Osoitetietojarjestelma'}")
    private String callingProcess;

    @Value("${viestipalvelu.emailsend.email.from:'oph_tiedotus@oph.fi'}")
    private String emailFrom;

    @Value("${viestipalvelu.emailsend.email.sender:'Opetushallitus'}")
    private String emailSender;

    @Value("${viestipalvelu.emailsend.email.organisaatioOid:''}")
    private String defaultOrganisaatioOid;

    @Value("${viestipalvelu.emailsend.email.templateName:'osoitepalvelu_email'}")
    private String emailTemplateName;

    @Value("${viestipalvelu.emailsend.email.subject:''}")
    private String emailDefaultSubject;

    @Value("${viestipalvelu.emailsend.email.body:''}")
    private String emailDefaultBody;

    @Autowired(required = false)
    private AuthenticationServiceRoute authenticationServiceRoute;

    @Override
    public EmailSendSettingsDto getEmailSendSettings(EmailSettingsParametersDto parameters) {
        MyInformationDto myInfo = parameters.getMe();
        Set<SourceRegister> sourceRegisters = parameters.getSourceRegisters();
        EmailSendSettingsDto settings  =  new EmailSendSettingsDto();
        settings.setEndpointUrl(emailSendEmailUrl);
        settings.getEmail().setCallingProcess(callingProcess);
        settings.getEmail().setFrom(emailFrom);
        settings.getEmail().setSender(emailSender);
        settings.getEmail().setSubject(emailDefaultSubject);
        settings.getEmail().setBody(emailDefaultBody);
        if (emailTemplateName != null && emailTemplateName.length() > 0) {
            settings.getEmail().setTemplateName(emailTemplateName);
        }
        if (parameters.getLanguage() != null) {
            settings.getEmail().setLanguageCode(parameters.getLanguage().getLanguage().toUpperCase());
        } else {
            settings.getEmail().setLanguageCode(DEFAULT_LOCALE.getLanguage().toUpperCase());
        }

        final HenkiloDetailsDto myHenkiloDetails = authenticationServiceRoute.getHenkiloTiedot(myInfo.getOid(),
                new DefaultCamelRequestContext());
//        log(read("henkilo", myInfo.getOid()));
        settings.getEmail().setOrganizationOid(
                myHenkiloDetails.findFirstAktiivinenOrganisaatioOid().or(defaultOrganisaatioOid));

        settings.getEmail().setReplyTo(
                CollectionHelper.first(myHenkiloDetails.getTyoOsoitees(), HenkiloYhteystietoRyhmaDto.SAHKOPOSTI)
                .or(Optional.fromNullable(myInfo.getEmail()))
                .or(new SahkopostiByKutsumanimiAndSukunimiProvider(myHenkiloDetails)));

        if (sourceRegisters != null) {
            settings.getEmail().setSourceRegister(new ArrayList<EmailSourceRegisterDto>());
            for (SourceRegister register : SourceRegister.values()) {
                if (sourceRegisters.contains(register)) {
                    settings.getEmail().getSourceRegister().add(new EmailSourceRegisterDto(register.name()));
                }
            }
        }

        return settings;
    }

    /**
     * Implementation of OSTJ-64
     */
    protected class SahkopostiByKutsumanimiAndSukunimiProvider implements Supplier<String> {
        private HenkiloDetailsDto details;

        protected SahkopostiByKutsumanimiAndSukunimiProvider(HenkiloDetailsDto details) {
            this.details = details;
        }

        @Override
        public String get() {
            return replaceSpecialCharactersAndLowerCase(
                    firstNameOf(Optional.fromNullable(details.getKutsumanimi())
                            .or(details.getEtunimet()))
                            + "." + details.getSukunimi() + "@oph.fi");
        }

        private String firstNameOf(String nimi) {
            // Jos etunimessä on välilyöntejä, palautetaan sitä ennen oleva osa
            if(nimi != null && nimi.contains(" ")) {
                return nimi.substring(0, nimi.indexOf(' ')).trim();
            }
            return nimi;
        }

        /**
         * Korvaa ääkköset aalla ja oolla
         * @param email sähköpostiosoite
         * @return sama sähköpostiosoite, mutta muuttaa Ää=Aa ja Öö=Oo sekä laittaa kaikki pienillä kirjaimilla
         */
        private String replaceSpecialCharactersAndLowerCase(String email) {
            // Kaikki pieniksi kirjaimiksi ja ääkköset aakkosiksi
            email = email.toLowerCase();
            email = email.replaceAll("ä", "a");
            email = email.replaceAll("ö", "o");

            return email;
        }
    }
}
