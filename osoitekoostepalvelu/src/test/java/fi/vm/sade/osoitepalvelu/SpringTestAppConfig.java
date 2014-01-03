package fi.vm.sade.osoitepalvelu;

import fi.vm.sade.osoitepalvelu.kooste.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/30/13
 * Time: 9:27 AM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan(basePackages = {
        "fi.vm.sade.osoitepalvelu.kooste.common",
        "fi.vm.sade.osoitepalvelu.kooste.domain",
        "fi.vm.sade.osoitepalvelu.kooste.dao",
        "fi.vm.sade.osoitepalvelu.kooste.service"
})
@ImportResource("classpath:spring/test-application-context.xml")
@Import(value={MongoTestConfig.class})
@PropertySource({"classpath:/osoitekoostepalvelu.properties", "classpath:/test.properties"})
public class SpringTestAppConfig {
    private static final int SECONDS_TO_MS_FACTOR = 1000;
    
    @Autowired
    private Environment env;

    @Bean
    public Config config() {
        Config config = new Config();
        config.setCacheTimeoutMillis(Integer.parseInt(env.getProperty("koodisto.cache.livetime.seconds")) * SECONDS_TO_MS_FACTOR);
        return config;
    }
}
