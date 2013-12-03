
angular.module('OsoiteKoostepalvelu', ['ngRoute', 'I18n', 'ui.bootstrap', 'ui.select2'])
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
.service('SearchService', function($log) {
    var _terms = {};

    this.updateTerms = function(terms) {
        _terms = terms;
    };

    this.search = function() {
        $log.info("Search called. Terms:");
        $log.info(_terms);
        return [
            {firstName: "Dummy", lastName: "Result"}
        ];
    };
})
.factory('SearchTypes', function(i18n) {
    return [
        {type: 'EMAIL', name: i18n.email_search_type},
        {type: 'LETTER', name: i18n.letter_search_type},
        {type: 'CONTACT', name: i18n.contact_search_type}
    ];
})
.factory('TargetGroups', function(i18n) {
    return [
        {type: 'OPPILAITOKSET', name: i18n.target_group_oppilaitos,
            options: [
                {type: "ORGANISAATIO",  name: i18n.target_group_oppilaitos_organisaatio},
                {type: "REHTORI",       name: i18n.target_group_oppilaitos_rehtori},
                {type: "KOULUTUSNEVONTA",  name: i18n.target_group_oppilaitos_koulutusneuvonta}
            ]
        },
        {type: 'MUU', name: 'Muu testi', options: []}
    ];
})
.factory('Saves', function() {
    return [
        {id: 12345, name: 'Ruotsinkielisten lukioiden opot'},
        {id: 12346, name: 'Kaikkien oppilaitosten rehtorit paitsi ahvenanmaalta'},
        {id: 789148, name: 'Varsinais-suomen ammattioppilaitokset'},
        {id: 885415, name: 'AIPAL ja KOULUTA -vastuukäyttäjät'}
    ];
});
