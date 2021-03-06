package fi.vm.sade.osoitepalvelu.kooste.mvc;

import java.io.Serializable;

import fi.vm.sade.osoitepalvelu.kooste.mvc.dto.EmailSettingsParametersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.osoitepalvelu.kooste.service.email.EmailService;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.EmailSendSettingsDto;
import fi.vm.sade.osoitepalvelu.kooste.service.email.dto.MyInformationDto;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 11:22 AM
 */
@Api(value="email", description = "Sähköpostin lähetys")
@Controller
@Scope(value =  WebApplicationContext.SCOPE_APPLICATION)
@RequestMapping(value  =  "/email")
public class EmailController extends AbstractMvcController implements Serializable {
    private static final long serialVersionUID = -741888911414782245L;
    
    @Autowired
    private EmailService emailService;

    @ApiOperation("Palauttaa sähköpostilähetykseen liittyvät asetukset Viestintäpalvelulle")
    @RequestMapping(value = "send.settings.json", method  =  RequestMethod.POST)
    @ResponseBody
    public EmailSendSettingsDto getEmailSendSettings(@RequestBody
            @ApiParam(name="parameters", value="Käyttäjän omat tiedot /cas/me:n palauttamassa muodossa sekä lähderekisterit.")
            EmailSettingsParametersDto parameters) {
        return emailService.getEmailSendSettings(parameters);
    }
}
