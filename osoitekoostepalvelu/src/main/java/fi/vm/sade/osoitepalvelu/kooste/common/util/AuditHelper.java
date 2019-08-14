package fi.vm.sade.osoitepalvelu.kooste.common.util;

import fi.vm.sade.auditlog.User;
import fi.vm.sade.javautils.http.HttpServletRequestUtils;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Optional;

public final class AuditHelper {

    private AuditHelper() {
    }

    public static User getUser() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return getUser(((ServletRequestAttributes) requestAttributes).getRequest());
        }
        return new User(getIp(), null, null);
    }

    public static User getUser(HttpServletRequest request) {
        Optional<Oid> oid = getOid(request);
        InetAddress ip = getIp(request);
        String session = getSession(request).orElse(null);
        String userAgent = getUserAgent(request).orElse(null);

        if (oid.isPresent()) {
            return new User(oid.get(), ip, session, userAgent);
        } else {
            return new User(ip, session, userAgent);
        }
    }

    public static Optional<Oid> getOid(HttpServletRequest request) {
        return Optional.ofNullable(request.getUserPrincipal()).map(Principal::getName).flatMap(AuditHelper::createOid);
    }

    private static Optional<Oid> createOid(String oid) {
        try {
            return Optional.of(new Oid(oid));
        } catch (GSSException e) {
            return Optional.empty();
        }
    }

    public static InetAddress getIp(HttpServletRequest request) {
        try {
            return InetAddress.getByName(HttpServletRequestUtils.getRemoteAddress(request));
        } catch (UnknownHostException e) {
            return getIp();
        }
    }

    public static InetAddress getIp() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return InetAddress.getLoopbackAddress();
        }
    }

    public static Optional<String> getSession(HttpServletRequest request) {
        return Optional.ofNullable(request.getSession(false)).map(HttpSession::getId);
    }

    public static Optional<String> getUserAgent(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("User-Agent"));
    }

}
