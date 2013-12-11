package fi.vm.sade.osoitepalvelu.kooste.mvc;

import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/11/13
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMvcController {

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Entity not found by primary key")  // 404
    @ExceptionHandler(NotFoundException.class)
    public void notFound() {
    }

}
