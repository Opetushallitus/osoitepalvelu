package fi.vm.sade.osoitepalvelu.kooste.webapp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 3:47 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface HealthCheckerName {
    public String value();
}
