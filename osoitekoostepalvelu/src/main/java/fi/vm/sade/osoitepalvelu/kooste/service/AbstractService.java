package fi.vm.sade.osoitepalvelu.kooste.service;

import fi.vm.sade.osoitepalvelu.kooste.common.exception.AuthorizationException;
import fi.vm.sade.osoitepalvelu.kooste.common.exception.NotFoundException;
import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractService {

    protected String getLoggedInUserOid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    protected <T> T found(T obj) throws NotFoundException {
        if (obj == null) {
            throw new NotFoundException("Entity not found by primary key.");
        }
        return obj;
    }

    protected void ensureLoggedInUser(String ownerUsername) {
        if (!EqualsHelper.equals(ownerUsername, getLoggedInUserOid())) {
            throw new AuthorizationException("Authenticated user " + getLoggedInUserOid()
                    + " does not have access right to given entity.");
        }
    }
}
