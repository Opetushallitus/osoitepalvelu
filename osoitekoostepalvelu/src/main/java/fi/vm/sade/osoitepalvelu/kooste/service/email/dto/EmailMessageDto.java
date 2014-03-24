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
    private String callingProcess;
    private String from;
    private String replyTo;
    private String subject;
    private String body;
    private List<Object> attachInfo = new ArrayList<Object>();

    public String getCallingProcess() {
        return callingProcess;
    }

    public void setCallingProcess(String callingProcess) {
        this.callingProcess = callingProcess;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
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

    public List<Object> getAttachInfo() {
        return attachInfo;
    }

    public void setAttachInfo(List<Object> attachInfo) {
        this.attachInfo = attachInfo;
    }
}
