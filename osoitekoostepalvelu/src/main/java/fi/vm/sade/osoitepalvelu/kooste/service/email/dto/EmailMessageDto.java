package fi.vm.sade.osoitepalvelu.kooste.service.email.dto;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 11:13 AM
 *
 * EmailData's email part according for Viestipalvelu according to fi.vm.sade.ryhmasahkoposti:ryhmasahkoposti-api's
 * fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage with default values and without attachments.
 */
public class EmailMessageDto implements Serializable {
    private static final String OSOITEJARJESTELMA_PROCESS_NAME = "Osoitetietojarjestelma";
    private static final String DEFAULT_OWNER_EMAIL = "oph_tiedotus@oph.fi";
    private static final String EMAIL_CONSTANT_CHARSET_UTF8 = "UTF-8";

    private String callingProcess = OSOITEJARJESTELMA_PROCESS_NAME;
    private String ownerEmail = DEFAULT_OWNER_EMAIL;
    private String senderEmail;
    private String senderOid;
    private String senderOidType;
    private String subject;
    private String body;
    private String footer;
    private boolean isHtml=false;
    private String charset = EMAIL_CONSTANT_CHARSET_UTF8;

    public String getCallingProcess() {
        return callingProcess;
    }

    public void setCallingProcess(String callingProcess) {
        this.callingProcess = callingProcess;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderOid() {
        return senderOid;
    }

    public void setSenderOid(String senderOid) {
        this.senderOid = senderOid;
    }

    public String getSenderOidType() {
        return senderOidType;
    }

    public void setSenderOidType(String senderOidType) {
        this.senderOidType = senderOidType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
