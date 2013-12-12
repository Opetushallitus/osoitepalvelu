package fi.vm.sade.osoitepalvelu.kooste;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

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
@Import(MongoConfig.class)
public class SpringApp {
}
