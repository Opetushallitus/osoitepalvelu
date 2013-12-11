package fi.vm.sade.osoitepalvelu.kooste;

import fi.vm.sade.osoitepalvelu.kooste.mvc.SavesController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan(basePackageClasses = SavesController.class)
@ImportResource("classpath:spring/spring-mvc.xml")
@Import(SpringApp.class)
public class SpringMvcApp {
}
