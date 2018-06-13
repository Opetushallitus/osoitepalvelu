package fi.vm.sade.osoitepalvelu.kooste.service.search;

import java.util.Locale;

public interface MessageService {
    String getMessage(String key, Locale locale, Object... params);
}
