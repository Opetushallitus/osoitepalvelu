package fi.vm.sade.osoitepalvelu.kooste;

import fi.vm.sade.osoitepalvelu.kooste.service.kooste.config.OsoitepalveluCamelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan(basePackageClasses = SpringApp.class)
@ImportResource("classpath:spring/application-context.xml")
@Import(value={MongoConfig.class, OsoitepalveluCamelConfig.class})
@PropertySource({"classpath:/osoitekoostepalvelu.properties", "file://${user.home}/oph-configuration/common.properties"})
public class SpringApp {
    @Autowired
    private Environment env;

    public static class Config {
        private int cacheTimeoutMillis;

        public int getCacheTimeoutMillis() {
            return cacheTimeoutMillis;
        }

        public void setCacheTimeoutMillis(int cacheTimeoutMillis) {
            this.cacheTimeoutMillis = cacheTimeoutMillis;
        }
    }

    @Bean
    public Config config() {
        Config config = new Config();
        config.setCacheTimeoutMillis(Integer.parseInt(env.getProperty("koodisto.cache.livetime.seconds"))*1000);
        return config;
    }

}
