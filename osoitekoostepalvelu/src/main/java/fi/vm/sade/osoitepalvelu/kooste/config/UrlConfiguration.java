package fi.vm.sade.osoitepalvelu.kooste.config;

import fi.vm.sade.properties.OphProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class UrlConfiguration extends OphProperties {
    public UrlConfiguration() {
        addFiles("/osoitekoostepalvelu-oph.properties");
        addOptionalFiles("/osoitekoostepalvelu.properties");
        addOptionalFiles(Paths.get(System.getProperties().getProperty("user.home"), "/oph-configuration/common.properties").toString());
    }
}
