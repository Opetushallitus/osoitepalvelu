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
        "host.base-uri": "https://itest-virkailija.oph.ware.fi",
        "root.organisaatio.oid":"1.2.246.562.10.00000000001",
        "ui.timeout.short": 10000,
        "ui.timeout.long": 60000,
        "osoitepalveluLocalisationRestUrl": "https://itest-virkailija.oph.ware.fi/lokalisointi/cxf/rest/v1/localisation",
        "casUrl": "cas_me_ophadmin.json",
        "organisaatio.api.rest.url": "https://itest-virkailija.oph.ware.fi:443/organisaatio-service/rest/",
        "authentication-service.rest.url":"https://itest-virkailija.oph.ware.fi/authentication-service/resources/",
    },
    "app" : {}
};
