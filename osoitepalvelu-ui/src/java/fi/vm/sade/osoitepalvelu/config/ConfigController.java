package fi.vm.sade.valintaperusteet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author kkammone
 */
@Controller
public class ConfigController {

    @Value("${valintaperusteet-ui.valintaperuste-service-url.rest}")
    private String valintaperusteServiceRestURL;

    @Value("${valintaperusteet-ui.koodisto-service-url.rest}")
    private String koodistoServiceRestURL;

    @Value("${valintaperusteet-ui.tarjona-service-url.rest}")
    private String tarjontaServiceRestURL;

    @Value("${valintaperusteet-ui.valintalaskentakoostepalvelu-service-url.rest}")
    private String valintalaskentakoostepalvelu;

    @Value("${auth.mode:}")
    private String authMode;

    @RequestMapping(value = "/configuration.js", method = RequestMethod.GET, produces = "text/javascript")
    @ResponseBody
    public String index() {
        StringBuilder b = new StringBuilder();
        append(b, "SERVICE_URL_BASE", valintaperusteServiceRestURL);
        append(b, "KOODISTO_URL_BASE", koodistoServiceRestURL);
        append(b, "TARJONTA_URL_BASE", tarjontaServiceRestURL);
        append(b, "VALINTALASKENTAKOOSTE_URL_BASE", valintalaskentakoostepalvelu);

        append(b, "TEMPLATE_URL_BASE", "");

        if (!authMode.isEmpty()) {
            append(b, "AUTH_MODE", authMode);
            if (authMode.trim().equalsIgnoreCase("dev")) {
                b.append("$('head').append('<script type=\"text/javascript\" src=\"/servers/cas/myroles\"></script>')");
            }
        }

        return b.toString();
    }

    private void append(StringBuilder b, String key, String value) {
        b.append(key);
        b.append(" = \"");
        b.append(value);
        b.append("\";\n");
    }

}
