package fi.vm.sade.osoitepalvelu.kooste.configuration;

import fi.vm.sade.properties.OphProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
