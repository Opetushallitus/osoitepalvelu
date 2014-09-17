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

package fi.vm.sade.osoitepalvelu.kooste.service.email.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 11:13 AM
 *
 * EmailData's email part according for Viestipalvelu according to fi.vm.sade.ryhmasahkoposti:ryhmasahkoposti-api's
 * fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage with default values and without attachments.
 */
public class EmailMessageDto implements Serializable {
    private static final long serialVersionUID = -1790563464167889613L;
    
    private String callingProcess;
    private String from;
    private String sender;
    private String organizationOid;
    private String replyTo;
    private String subject;
    private String body;
    private String templateName;
    private String languageCode;
    private List<Object> attachInfo  =  new ArrayList<Object>();
    private List<EmailSourceRegisterDto> sourceRegister = new ArrayList<EmailSourceRegisterDto>();

    public String getCallingProcess() {
        return callingProcess;
    }

    public void setCallingProcess(String callingProcess) {
        this.callingProcess  =  callingProcess;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from  =  from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo  =  replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject  =  subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body  =  body;
    }

    public List<Object> getAttachInfo() {
        return attachInfo;
    }

    public void setAttachInfo(List<Object> attachInfo) {
        this.attachInfo  =  attachInfo;
    }

    public String getOrganizationOid() {
        return organizationOid;
    }

    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<EmailSourceRegisterDto> getSourceRegister() {
        return sourceRegister;
    }

    public void setSourceRegister(List<EmailSourceRegisterDto> sourceRegister) {
        this.sourceRegister = sourceRegister;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
