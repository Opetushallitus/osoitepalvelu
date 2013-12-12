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
        {identifier: 56124, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56125, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56116, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56127, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56128, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56119, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56131, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56132, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56150, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56141, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56142, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56160, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56161, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56172, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56170, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56181, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56182, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56190, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56191, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56183, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56184, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56192, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56194, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56155, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56156, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56163, targetGroup: "Aalto-yliopiston urheiluseura"},
        {identifier: 56164, targetGroup: "Aalto-yliopistokiinteistöt Oy"},
        {identifier: 56175, targetGroup: "Aalto-yliopiston Sähköinsinöörikilta"},
        {identifier: 56176, targetGroup: "Aalto-yliopiston urheiluseura"}
    ];
});

OsoiteKoostepalvelu.service('SavesService', function($log, DummySaves, $filter, FilterHelper) {
    var deletedIds = [];

    this.list = function(success) {
        success( $filter('filter')(DummySaves, FilterHelper.extractedFieldNotInArray(deletedIds, "id") ) );
    };

    this.saveSearch = function(save, success) {
        success(1234);
    };

    this.updateSearch = function(save, success, error) {
        success([]);
    };

    this.getSearch = function(id, success) {
        success({});
    };

    this.deleteSearch = function(id, success) {
        deletedIds.push(id);
        success();
    };
});

OsoiteKoostepalvelu.factory('SearchResultProvider', function(DummyResults) {
    return function(details) {
        details.callback( DummyResults );
    }
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
