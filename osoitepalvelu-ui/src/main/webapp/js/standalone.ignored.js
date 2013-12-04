/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.factory('DummySaves', function() {
    return [
        {id: 12345,     name: 'Ruotsinkielisten lukioiden opot'},
        {id: 12346,     name: 'Kaikkien oppilaitosten rehtorit paitsi ahvenanmaalta'},
        {id: 789148,    name: 'Varsinais-suomen ammattioppilaitokset'},
        {id: 885415,    name: 'AIPAL ja KOULUTA -vastuukäyttäjät'}
    ];
});

OsoiteKoostepalvelu.factory('DummyAVIs', function() {
    return [
        {id: 232323, name: 'Kaikki alueet paitsi Ahvenanmaa'},
        {id: 123154, name: 'Etelä-Suomen AVI'},
        {id: 123155, name: 'Lapin AVI'}
    ];
});
OsoiteKoostepalvelu.factory('DummyMaakuntas', function() {
    return [
        {id: 123153, name: 'Ahvenanmaa'},
        {id: 123155, name: 'Pirkanmaa'},
        {id: 123154, name: 'Pohjois-Karjala'},
        {id: 232323, name: 'Uusimaa'}
    ];
});
OsoiteKoostepalvelu.factory('DummyKuntas', function() {
    return [
        {id: 456, name: 'Espoo'},
        {id: 123, name: 'Helsinki'},
        {id: 485, name: 'Jyväskylä'},
        {id: 789, name: 'Tampere'},
        {id: 789, name: 'Turku'},
        {id: 790, name: 'Seinäjöki'},
        {id: 484, name: 'Oulu'}
    ];
});
OsoiteKoostepalvelu.factory('DummyOppilaitostyyppis', function() {
    return [
        {id: 1, name: 'Ammattikorkeakoulut'},
        {id: 2, name: 'Kesäyliopistot'},
        {id: 3, name: 'Lukiot'}
    ];
});
OsoiteKoostepalvelu.factory('DummyOmistajatyyppis', function() {
    return [
        {id: 1, name: 'Ahvenanmaa'},
        {id: 2, name: 'Kunta'},
        {id: 3, name: 'Kuntaryhmä'},
        {id: 4, name: 'Ohjeistajatyyppi tuntematon'},
        {id: 5, name: 'Valtio'},
        {id: 6, name: 'Yksityinen'}
    ];
});
OsoiteKoostepalvelu.factory('DummyVuosiluokkas', function() {
    return [
        {id: 1, name: 'esiopetus'},
        {id: 2, name: 'vuosiluokka 1'},
        {id: 3, name: 'vuosiluokka 2'},
        {id: 4, name: 'vuosiluokka 3'},
        {id: 5, name: 'vuosiluokka 4'},
        {id: 6, name: 'vuosiluokka 5'},
        {id: 7, name: 'vuosiluokka 6'},
        {id: 8, name: 'vuosiluokka 7'},
        {id: 9, name: 'vuosiluokka 8'},
        {id: 10, name: 'vuosiluokka 9'}
    ];
});
OsoiteKoostepalvelu.factory('DummyKoulutuksenjarjestajas', function() {
    return [
        {id: 1, name: 'Helsingin kaupunki'},
        {id: 2, name: 'Helsingin Konservatorion Säätiö'},
        {id: 3, name: 'Helsingin normaalilyseo'}
    ];
});

OsoiteKoostepalvelu.factory('DummyResults', function() {
    return [
        {identifier: 56121, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56122, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56110, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56121, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56122, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56110, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56121, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56122, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56110, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56121, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56122, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56110, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56121, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56122, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56110, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56121, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56122, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56110, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56121, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56122, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56110, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56121, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56122, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56110, targetGroup: "Aalto-yliopiston urheiluseura"}
    ];
});

OsoiteKoostepalvelu.service('SavesService', function($log, DummySaves, $filter, FilterHelper) {
    var deletedIds = [];

    this.list = function(success) {
        success( $filter('filter')(DummySaves, FilterHelper.extractedFieldNotInArray(deletedIds, "id") ) );
    };

    this.deleteSave = function(id, success) {
        deletedIds.push(id);
        success();
    };
});

OsoiteKoostepalvelu.service('SearchService', function($log, DummyResults) {
    var _terms = {},
        _targetGroups = [],
        _searchType = null,
        _addressFields = [];

    this.updateSearchType = function(type, addressFields) {
        _searchType = type;
        _addressFields = addressFields;
    };

    this.updateTargetGroups = function(targetGroups) {
        _targetGroups = targetGroups;
    };

    this.updateTerms = function(terms) {
        _terms = terms;
    };

    this.search = function(success) {
        $log.info(_searchType);
        $log.info(_addressFields);
        $log.info(_targetGroups);
        $log.info(_terms);
        success( DummyResults );
    };
});

OsoiteKoostepalvelu.service('OptionsService', function($log,
                               DummyAVIs, DummyMaakuntas, DummyKuntas,
                               DummyOppilaitostyyppis, DummyOmistajatyyppis,
                               DummyVuosiluokkas, DummyKoulutuksenjarjestajas ) {
    this.listAvis = function(success) {
        success( DummyAVIs );
    };

    this.listMaakuntas = function(success) {
        success( DummyMaakuntas );
    };

    this.listKuntas = function(success) {
        success( DummyKuntas );
    };

    this.listOppilaitostyyppis = function(success) {
        success( DummyOppilaitostyyppis );
    };

    this.listOmistajatyyppis = function(success) {
        success( DummyOmistajatyyppis );
    };

    this.listVuosiluokkas = function(success) {
        success( DummyVuosiluokkas );
    };

    this.listKoultuksenjarjestajas = function(success) {
        success( DummyKoulutuksenjarjestajas );
    };
});
