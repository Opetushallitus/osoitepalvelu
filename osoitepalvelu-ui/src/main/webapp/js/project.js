
var OsoiteKoostepalvelu = angular.module('OsoiteKoostepalvelu', ['ngRoute', 'Helpers', 'I18n', 'ui.bootstrap', 'ui.select2'])
.config(function($routeProvider) {
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
.factory('SearchTypes', function(i18n) {
    return [
        {type: 'EMAIL',     name: i18n.email_search_type},
        {type: 'LETTER',    name: i18n.letter_search_type},
        {type: 'CONTACT',   name: i18n.contact_search_type}
    ];
})
.factory('AddressFields', function(i18n) {
    return [
        {type: 'ORGANIAATIO_NIMI',          name: i18n.address_field_organisaatio_nimi},
        {type: 'ORGANIAATIO_TUNNISTE',      name: i18n.address_field_organisaatio_tunniste},
        {type: 'YHTEYSHENKILO',             name: i18n.address_field_yhteyshenkilo},
        {type: 'POSTIOSOITE',               name: i18n.address_field_postiosoite},
        {type: 'KATU_POSTINUMERO',          name: i18n.address_field_katu_postinumero},
        {type: 'PL_POSTINUMERO',            name: i18n.address_field_pl_postinumero},
        {type: 'PUHELINNUMERO',             name: i18n.address_field_puhelinnumero},
        {type: 'FAXINUMERO',                name: i18n.address_field_faxinumero},
        {type: 'INTERNET_OSOITE',           name: i18n.address_field_internet_osoite},
        {type: 'VIRANOMAISTIEDOTUS_EMAIL',  name: i18n.address_field_viranomaistiedotus_email},
        {type: 'KOULUTUSNEUVONNAN_EMAIL',   name: i18n.address_field_koulutusneuvonnan_email},
        {type: 'KRIISITIEDOTUKSEN_EMAIL',   name: i18n.address_field_kriisitiedotuksen_email},
        {type: 'ORGANISAATIO_SIJAINTIKUNTA',name: i18n.address_field_organisaatio_sijaintikunta},
        {type: 'OPASJAKELUMAARAT',          name: i18n.address_field_opasjakelumaarat}
    ];
})
.factory('TargetGroups', function(i18n) {
    return [
        {type: 'JARJESTAJAT_YLLAPITAJAT',   name: i18n.target_group_jarjestajat_yllapitajat,
            options: [
                {type: "ORGANISAATIO",      name: i18n.target_group_option_organisaatio},
                {type: "YHTEYSHENKILO",     name: i18n.target_group_option_yhteyshenkilo},
                {type: "KRIISITIEDOTUS",    name: i18n.target_group_option_kriisitiedotus}
            ]
        },
        {type: 'OPPILAITOKSET',             name: i18n.target_group_oppilaitos,
            options: [
                {type: "ORGANISAATIO",      name: i18n.target_group_option_organisaatio},
                {type: "REHTORI",           name: i18n.target_group_option_rehtori},
                {type: "KOULUTUSNEVONTA",   name: i18n.target_group_option_koulutusneuvonta}
            ]
        },
        {type: 'OPETUSPISTEET',             name: i18n.target_group_opetuspisteet,
            options: [
                {type: "ORGANISAATIO",      name: i18n.target_group_option_organisaatio},
                {type: "YHTEYSHENKILO",     name: i18n.target_group_option_yhteyshenkilo},
                {type: "KOULUTUSNEVONTA",   name: i18n.target_group_option_koulutusneuvonta}
            ]
        },
        {type: 'OPPISOPIMUSTOIMPISTEET',    name: i18n.target_group_oppisopimustoimipisteet,
            options: [
                {type: "ORGANISAATIO",      name: i18n.target_group_option_organisaatio},
                {type: "YHTEYSHENKILO",     name: i18n.target_group_option_yhteyshenkilo}
            ]
        },
        {type: 'MUUT_ORGANISAATIOT',        name: i18n.target_group_muut_organisaatiot,
            options: []
        },
        {type: 'KOULUTA_KAYTTAJAT',         name: i18n.target_group_kouluta_kayttajat,
            options: []
        },
        {type: 'AIPAL_KAYTTAJAT',           name: i18n.target_group_aipal_kayttajat,
            options: []
        }
    ];
});
