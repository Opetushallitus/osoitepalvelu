/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

/*
 * Help:
 * Add service factory(/js/shared/config.js) to your module.
 * Module name : 'config'.
 * Factory name : 'Config'.
 *
 * FAQ:
 * How to get an environment variable by a key: <factory-object>.env[<string-key>].
 * How to get AngularJS application variable by a key: <factory-object>.app[<string-key>].
 *
 * Example:
 * cfg.env["ui.timeout.short"];
 * result value : 10000
 */
window.CONFIG = {
    "env": {
//        "host.base-uri": "",
        "root.organisaatio.oid":"1.2.246.562.10.00000000001",
        "ui.timeout.short": 10000,
        "ui.timeout.long": 60000,
//        "osoitepalveluLocalisationRestUrl": "",
//        "casMeUrl": "cas_me_test.json",
        "useCasMeUrl": true,
//        "organisaatio.api.rest.url": "",
//        "authentication-service.rest.url":"",
    },
    "app" : {},
    "mode": 'dev-without-backend'
};
