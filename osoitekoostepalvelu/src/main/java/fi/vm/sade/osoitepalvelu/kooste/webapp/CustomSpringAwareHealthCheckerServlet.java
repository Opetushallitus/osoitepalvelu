package fi.vm.sade.osoitepalvelu.kooste.webapp;

import fi.vm.sade.generic.healthcheck.HealthChecker;
import fi.vm.sade.generic.healthcheck.SpringAwareHealthCheckServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 2/26/14
 * Time: 3:44 PM
 */
public class CustomSpringAwareHealthCheckerServlet extends SpringAwareHealthCheckServlet {
    private static final long serialVersionUID = 9131861317010702448L;

    @Override
    protected Map<String, HealthChecker> registerHealthCheckers() {
        Map<String, HealthChecker> checkers  =  ctx.getBeansOfType(HealthChecker.class);
        // Using mongodb as database, not the default JDBC dataSource dependant anonymous class for database.
        Map<String, HealthChecker> checkersByNames  =  new HashMap<String, HealthChecker>();
        for(Map.Entry<String, HealthChecker> checkerBean : checkers.entrySet()) {
            HealthChecker bean  =  checkerBean.getValue();
            if(bean.getClass().isAnnotationPresent(HealthCheckerName.class)) {
                checkersByNames.put(bean.getClass().getAnnotation(HealthCheckerName.class).value(), bean);
            } else {
                checkersByNames.put(checkerBean.getKey(), checkerBean.getValue());
            }
        }
        return checkersByNames;
    }
}
