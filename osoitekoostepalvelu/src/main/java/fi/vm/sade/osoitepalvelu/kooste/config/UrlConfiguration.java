package fi.vm.sade.osoitepalvelu.kooste.config;

import fi.vm.sade.properties.OphProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.nio.file.Paths;

@Configuration
public class UrlConfiguration {
    @Bean
    public OphProperties properties() {
        OphProperties properties = new OphProperties("/osoitekoostepalvelu-oph.properties");
        properties.addOptionalFiles("/osoitekoostepalvelu.properties");
        properties.addOptionalFiles(Paths.get(System.getProperties().getProperty("user.home"), "/oph-configuration/common.properties").toString());
        properties.addOptionalFiles("/ui.app.properties");
        properties.addOptionalFiles("/ui.env.properties");
        return properties;
    }
}
