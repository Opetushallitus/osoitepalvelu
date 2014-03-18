
OsoiteKoostepalvelu.config(function($routeProvider, $provide) {
    $routeProvider.when('/results', {
            controller:'ResultsController',
            templateUrl:'partials/results.html'
    }).when('/', {
            controller:'SearchController',
            templateUrl:'partials/searchForm.html'
    })
    .otherwise({
            redirectTo:'/'
    });
})
.factory('commonErrorHandler', function($log, LocalisationService, flash) {
    return function(data, status, headers, config) {
        $log.error("Error "+status+": ", data);
        if( data.messageKey ) {
            flash('error', LocalisationService.t( data.messageKey, data.messageParams )
                + (data.errorCode ? " ("+data.errorCode+")" : "") );
        }
    };
})
.factory('SearchTypes', function(LocalisationService) {
    return [
        {type: 'EMAIL',         name: LocalisationService.t('email_search_type')},
        /*{type: 'SEND_LETTER',   name: LocalisationService.t('send_letter_search_type')},*/
        {type: 'LETTER',        name: LocalisationService.t('letter_search_type')},
        {type: 'CONTACT',       name: LocalisationService.t('contact_search_type')}
    ];
})
.factory('EmptyTerms', function() {
    return {
        tutkintotoimikuntas: [],
        tutkintotoimikuntaRoolis: [],
        koulutaRoolis: [],
        aipalRoolis: [],
        organisaationKielis: [],
        avis: [],
        maakuntas : [],
        kuntas : [],
        oppilaitostyyppis: [],
        omistajatyyppis: [],
        vuosiluokkas: [],
        koultuksenjarjestajas: []
    };
})
.factory('AddressFields', function(LocalisationService) {
    return [
        {type: 'ORGANIAATIO_NIMI',          name: LocalisationService.t('address_field_organisaatio_nimi')},
        {type: 'ORGANIAATIO_TUNNISTE',      name: LocalisationService.t('address_field_organisaatio_tunniste')},
        //{type: 'YHTEYSHENKILO',             name: LocalisationService.t('address_field_yhteyshenkilo')},
        {type: 'POSTIOSOITE',               name: LocalisationService.t('address_field_postiosoite')},
        {type: 'KATU_POSTINUMERO',          name: LocalisationService.t('address_field_katu_postinumero')},
        {type: 'PL_POSTINUMERO',            name: LocalisationService.t('address_field_pl_postinumero')},
        {type: 'PUHELINNUMERO',             name: LocalisationService.t('address_field_puhelinnumero')},
        {type: 'FAXINUMERO',                name: LocalisationService.t('address_field_faxinumero')},
        {type: 'INTERNET_OSOITE',           name: LocalisationService.t('address_field_internet_osoite')},
        //{type: 'VIRANOMAISTIEDOTUS_EMAIL',  name: LocalisationService.t('address_field_viranomaistiedotus_email')},
        //{type: 'KOULUTUSNEUVONNAN_EMAIL',   name: LocalisationService.t('address_field_koulutusneuvonnan_email')},
        //{type: 'KRIISITIEDOTUKSEN_EMAIL',   name: LocalisationService.t('address_field_kriisitiedotuksen_email')},
        {type: 'ORGANISAATIO_SIJAINTIKUNTA',name: LocalisationService.t('address_field_organisaatio_sijaintikunta')}
    ];
})
.factory("ReceiverTypes", function(LocalisationService) {
    return [
        {type: 'ORGANISAATIO',              name: LocalisationService.t('receiver_field_organisaatio')},
        {type: 'YHTEYSHENKILO',             name: LocalisationService.t('receiver_field_yhteyshenkilo')}
    ];
})
.factory("TutkintotoimikuntaRoolis", function(LocalisationService) {
    return [
        {type: 'PUHEENJOHTAJA', name: LocalisationService.t('target_group_option_puheenjohtaja')},
        {type: 'SIHTEERI',      name: LocalisationService.t('target_group_option_sihteeri')},
        {type: 'JASENET',       name: LocalisationService.t('target_gorup_option_jasenet')}
    ];
})
.factory('TargetGroups', function(LocalisationService, TutkintotoimikuntaRoolis) {
    return [
        {type: 'JARJESTAJAT_YLLAPITAJAT',   name: LocalisationService.t('target_group_jarjestajat_yllapitajat'),
            options: [
                {type: "ORGANISAATIO",      name: LocalisationService.t('target_group_option_organisaatio')}//,
                //{type: "YHTEYSHENKILO",     name: LocalisationService.t('target_group_option_yhteyshenkilo')},
                //{type: "KRIISITIEDOTUS",    name: LocalisationService.t('target_group_option_kriisitiedotus')}
            ]
        },
        {type: 'OPPILAITOKSET',             name: LocalisationService.t('target_group_oppilaitos'),
            options: [
                {type: "ORGANISAATIO",      name: LocalisationService.t('target_group_option_organisaatio')}//,
                //{type: "REHTORI",           name: LocalisationService.t('target_group_option_rehtori')},
                //{type: "KOULUTUSNEVONTA",   name: LocalisationService.t('target_group_option_koulutusneuvonta')}
            ]
        },
        {type: 'OPETUSPISTEET',             name: LocalisationService.t('target_group_opetuspisteet'),
            options: [
                {type: "ORGANISAATIO",      name: LocalisationService.t('target_group_option_organisaatio')}//,
                //{type: "YHTEYSHENKILO",     name: LocalisationService.t('target_group_option_yhteyshenkilo')},
                //{type: "KOULUTUSNEVONTA",   name: LocalisationService.t('target_group_option_koulutusneuvonta')}
            ]
        },
        /*{type: 'OPPISOPIMUSTOIMPISTEET',    name: LocalisationService.t('target_group_oppisopimustoimipisteet'),
            options: [
                {type: "ORGANISAATIO",      name: LocalisationService.t('target_group_option_organisaatio')},
                {type: "YHTEYSHENKILO",     name: LocalisationService.t('target_group_option_yhteyshenkilo')}
            ]
        },*/
        /*{type: 'MUUT_ORGANISAATIOT',        name: LocalisationService.t('target_group_muut_organisaatiot'),
            options: [
                {type: 'TUNNUKSENHALTIJAT', name: LocalisationService.t('target_group_option_tunnuksenhaltijat')}
            ]
        },*/
        {type: 'TUTKINTOTOIMIKUNNAT',       name: LocalisationService.t('target_group_tutkintotoimikunnat'),
            options: angular.copy(TutkintotoimikuntaRoolis)
        },
        {type: 'KOULUTA_KAYTTAJAT',         name: LocalisationService.t('target_group_kouluta_kayttajat'),
            options: [
                {type: 'TUNNUKSENHALTIJAT', name: LocalisationService.t('target_group_option_tunnuksenhaltijat')}
            ]
        },
        {type: 'AIPAL_KAYTTAJAT',           name: LocalisationService.t('target_group_aipal_kayttajat'),
            options: [
                {type: 'TUNNUKSENHALTIJAT', name: LocalisationService.t('target_group_option_tunnuksenhaltijat')}
            ]
        }
    ];
});

