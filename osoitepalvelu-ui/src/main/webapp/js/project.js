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

OsoiteKoostepalvelu.config(["$routeProvider", "$provide", function($routeProvider, $provide) {
    $routeProvider.when('/results', {
            controller: 'ResultsController',
            templateUrl: 'partials/results.html'
    }).when('/', {
            controller: 'SearchController',
            templateUrl: 'partials/searchForm.html'
    })
    .otherwise({
            redirectTo:'/'
    });
}])
.factory('NoCacheInterceptor', function () {
    return {
        request: function (config) {
            if (config.method && config.method == 'GET' && config.url.indexOf('html') === -1){
                var separator = config.url.indexOf('?') === -1 ? '?' : '&';
                config.url = config.url+separator+'noCache=' + new Date().getTime();
            }
            return config;
        }
    };
})
.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('NoCacheInterceptor');
}])
.config(['flashProvider',function (flashProvider) {
    flashProvider.errorClassnames.push('alert-danger');
}])
.factory('commonErrorHandler', ["$log", "LocalisationService", "flash", function($log, LocalisationService, flash) {
    return function(data, status, headers, config) {
        $log.error("Error "+status+": ", data);
        if (data.messageKey) {
            flash.error = LocalisationService.t( data.messageKey, data.messageParams )
                          + (data.errorCode ? " ("+data.errorCode+")" : "");
        }
    };
}])
.factory('SearchTypes', ["LocalisationService", function(LocalisationService) {
    return [
        {type: 'EMAIL',         name: LocalisationService.t('email_search_type')},
        /*{type: 'SEND_LETTER',   name: LocalisationService.t('send_letter_search_type')},*/
        {type: 'LETTER',        name: LocalisationService.t('letter_search_type')},
        {type: 'CONTACT',       name: LocalisationService.t('contact_search_type')}
    ];
}])
.factory('EmptyTerms', function() {
    return {
        tutkintotoimikuntas: [],
        tutkintotoimikuntaRoolis: [],
        tutkintotoimikuntaKielis: [],
        tutkintotoimikuntaJasenKielis: [],
        tutkintotoimikuntaToimikausis: [],
        koulutaRoolis: [],
        organisaationKielis: [],
        avis: [],
        maakuntas : [],
        kuntas : [],
        koulutusalas: [],
        opintoalas: [],
        koulutus: [],
        koulutustyyppis: [],
        koulutuslajis: [],
        opetusKielis: [],
        oppilaitostyyppis: [],
        omistajatyyppis: [],
        vuosiluokkas: [],
        koultuksenjarjestajas: []
    };
})
.factory('Osoitekielis', ["LocalisationService", function(LocalisationService) {
    return [
        {code: 'fi', name: LocalisationService.t('kieli_fi')},
        {code: 'sv', name: LocalisationService.t('kieli_sv')},
        {code: 'en', name: LocalisationService.t('kieli_en')}
    ];
}])
.factory('Aitukielis', ["LocalisationService", function(LocalisationService) {
    return [
        {code: 'fi', name: LocalisationService.t('kieli_fi')},
        {code: 'sv', name: LocalisationService.t('kieli_sv')},
        {code: '2k', name: LocalisationService.t('kieli_2k')},
        {code: 'en', name: LocalisationService.t('kieli_en')}
    ];
}])
.factory('TutkintotoimikuntaToimikausis', ['LocalisationService', function(LocalisationService) {
    return [
        {type: 'voimassa', nimi: LocalisationService.t('tutkintoimikunta_toimikausi_voimassa')},
        {type: 'tuleva', nimi: LocalisationService.t('tutkintoimikunta_toimikausi_tuleva')},
        {type: 'mennyt', nimi: LocalisationService.t('tutkintoimikunta_toimikausi_mennyt')}
    ];
}])
.factory('AddressFields', ["LocalisationService", function(LocalisationService) {
    return [
        {type: 'ORGANISAATIO_NIMI',          name: LocalisationService.t('address_field_organisaatio_nimi')},
        {type: 'ORGANISAATIO_TUNNISTE',      name: LocalisationService.t('address_field_organisaatio_tunniste')},
        //{type: 'YHTEYSHENKILO',             name: LocalisationService.t('address_field_yhteyshenkilo')},
        {type: 'POSTIOSOITE',               name: LocalisationService.t('address_field_postiosoite')},
        {type: 'KAYNTIOSOITE',              name: LocalisationService.t('address_field_kayntiosoite')},
        {type: 'PUHELINNUMERO',             name: LocalisationService.t('address_field_puhelinnumero')},
        {type: 'FAXINUMERO',                name: LocalisationService.t('address_field_faxinumero')},
        {type: 'INTERNET_OSOITE',           name: LocalisationService.t('address_field_internet_osoite')},
        {type: 'EMAIL_OSOITE',              name: LocalisationService.t('address_field_email_osoite')},
        //{type: 'VIRANOMAISTIEDOTUS_EMAIL',  name: LocalisationService.t('address_field_viranomaistiedotus_email')},
        //{type: 'KOULUTUSNEUVONNAN_EMAIL',   name: LocalisationService.t('address_field_koulutusneuvonnan_email')},
        {type: 'KRIISITIEDOTUKSEN_EMAIL',   name: LocalisationService.t('address_field_kriisitiedotuksen_email')},
        {type: 'ORGANISAATIO_SIJAINTIKUNTA',name: LocalisationService.t('address_field_organisaatio_sijaintikunta')}
    ];
}])
.factory("TutkintotoimikuntaRoolis", ["LocalisationService", function(LocalisationService) {
    return [
        {type: 'JASENET', name: LocalisationService.t('target_gorup_option_jasenet')},
        {type: 'VIRANOMAIS_EMAIL', name: LocalisationService.t('target_group_option_viranomaissahkoposti'), selected: true}
        //,{type: 'TUTKINTOTOIMIKUNTA', name: LocalisationService.t('target_gorup_option_tutkintotoimikunta')}
//        {type: 'PUHEENJOHTAJA', name: LocalisationService.t('target_group_option_puheenjohtaja')},
//        {type: 'SIHTEERI',      name: LocalisationService.t('target_group_option_sihteeri')},
//        {type: 'JASENET',       name: LocalisationService.t('target_gorup_option_jasenet')}
    ];
}])
.factory('TargetGroups', ["LocalisationService", "TutkintotoimikuntaRoolis",
        function(LocalisationService, TutkintotoimikuntaRoolis) {
    return [
        {type: 'JARJESTAJAT_YLLAPITAJAT',   name: LocalisationService.t('target_group_jarjestajat_yllapitajat'),
            options: [
                {type: "ORGANISAATIO",      name: LocalisationService.t('target_group_option_organisaatio'),
                hide: true}//,
                //{type: "YHTEYSHENKILO",     name: LocalisationService.t('target_group_option_yhteyshenkilo')},
                //{type: "KRIISITIEDOTUS",    name: LocalisationService.t('target_group_option_kriisitiedotus')}
            ]
        },
        {type: 'OPPILAITOKSET',             name: LocalisationService.t('target_group_oppilaitos'),
            options: [
                {type: "ORGANISAATIO",      name: LocalisationService.t('target_group_option_organisaatio'),
                hide: true}//,
                //{type: "REHTORI",           name: LocalisationService.t('target_group_option_rehtori')},
                //{type: "KOULUTUSNEVONTA",   name: LocalisationService.t('target_group_option_koulutusneuvonta')}
            ]
        },
        {type: 'OPETUSPISTEET',             name: LocalisationService.t('target_group_opetuspisteet'),
            options: [
                {type: "ORGANISAATIO",      name: LocalisationService.t('target_group_option_organisaatio'),
                hide: true}//,
                //{type: "YHTEYSHENKILO",     name: LocalisationService.t('target_group_option_yhteyshenkilo')},
                //{type: "KOULUTUSNEVONTA",   name: LocalisationService.t('target_group_option_koulutusneuvonta')}
            ]
        },
        {type: 'KOULUTUKSEN_TARJOAJAT', name: LocalisationService.t("target_group_koulutuksen_tarjoajat"),
            options: [
                {type: 'KOULUTUSTOIMIJA', name: LocalisationService.t("target_group_option_koulutustoimija"), selected: true},
                {type: 'OPPILAITOS', name: LocalisationService.t("target_group_option_oppilaitos"), selected: true},
                {type: 'TOIMIPISTE',  name: LocalisationService.t('target_group_option_toimipiste'), selected: true}
            ]
        },
        {type: 'OPPISOPIMUSTOIMPISTEET',    name: LocalisationService.t('target_group_oppisopimustoimistot'),
            options: [
                {type: "ORGANISAATIO",      name: LocalisationService.t('target_group_option_organisaatio'),
                hide: true}/*,
                {type: "YHTEYSHENKILO",     name: LocalisationService.t('target_group_option_yhteyshenkilo')}*/
            ]
        },
        /*{type: 'MUUT_ORGANISAATIOT',        name: LocalisationService.t('target_group_muut_organisaatiot'),
            options: [
                {type: 'TUNNUKSENHALTIJAT', name: LocalisationService.t('target_group_option_tunnuksenhaltijat')}
            ]
        },*/
        {type: 'TUTKINTOTOIMIKUNNAT',       name: LocalisationService.t('target_group_tutkintotoimikunnat'),
            options: angular.copy(TutkintotoimikuntaRoolis),
            hideOptions: false
        },
        {type: 'NAYTTOTUTKINNON_JARJESTAJAT', name: LocalisationService.t("target_group_nayttotutkinnon_jarjestajat"),
            options: [
                {type: 'JARJESTAJA_ORGANISAATIO', name: LocalisationService.t("target_group_option_jarjestajaorganisaatio"),
                selected: true},
                {type: 'TUTKINTOVASTAAVA',  name: LocalisationService.t('target_group_option_tutkintovastaava')}
            ]
        },
        {type: 'KOULUTA_KAYTTAJAT',         name: LocalisationService.t('target_group_kayttajat'),
            options: [
                {type: 'TUNNUKSENHALTIJAT', name: LocalisationService.t('target_group_option_tunnuksenhaltijat'),
                hide: true}
            ]
        },
        {type: 'TYOELAMAPALVELUT', name: 'target_group_tyoelamapalvelut',
            options: [
                {type: "ORGANISAATIO",      name: LocalisationService.t('target_group_option_organisaatio')
                // {
                    // type: "TYOELAMAJARJESTO", name: 'target_group_option_tyoelamajarjesto'
                    // hide: true
                }
            ]
        }
    ];
}]);

