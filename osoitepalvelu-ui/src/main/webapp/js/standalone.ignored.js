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

OsoiteKoostepalvelu.factory("DummyTutkintotoimikuntas", function() {
    return [
        {koodiId: 1544854, nimi:'Ajoneuvonosturinkuljettajan tutkintotoimikunta'},
        {koodiId: 1544855, nimi:'Examenskommissionen inom företagarbranschen och töretagsekonomi'}
    ];
});

OsoiteKoostepalvelu.factory("DummyKoulutaRoolis", function() {
    return [
        {koodiId: 1, nimi:'ARVOSANAPAIV'},
        {koodiId: 2, nimi:'HAKEMUSTIETALLENTAJA'}
    ];
});
OsoiteKoostepalvelu.factory("DummyAipalRoolis", function() {
    return [
        {koodiId: 1, nimi:'AIPAL-koulutuksen/tutkinnon järjestäjän käyttäjä'}
    ];
});
OsoiteKoostepalvelu.factory("DummyOrganisaatioKielis", function() {
    return [
        {code: 'fi', nimi:'suomi'},
        {code: 'sv', nimi:'ruotsi'},
        {code: 'en', nimi:'englanti'}
    ];
});
OsoiteKoostepalvelu.factory('DummyAVIs', function() {
    return [
        {koodiId: 232323, nimi: 'Kaikki alueet paitsi Ahvenanmaa'},
        {koodiId: 123154, nimi: 'Etelä-Suomen AVI'},
        {koodiId: 123155, nimi: 'Lapin AVI'}
    ];
});
OsoiteKoostepalvelu.factory('DummyMaakuntas', function() {
    return [
        {koodiId: 123153, nimi: 'Ahvenanmaa'},
        {koodiId: 123155, nimi: 'Pirkanmaa'},
        {koodiId: 123154, nimi: 'Pohjois-Karjala'},
        {koodiId: 232323, nimi: 'Uusimaa'}
    ];
});
OsoiteKoostepalvelu.factory('DummyKuntas', function() {
    return [
        {koodiId: 456, nimi: 'Espoo'},
        {koodiId: 123, nimi: 'Helsinki'},
        {koodiId: 485, nimi: 'Jyväskylä'},
        {koodiId: 789, nimi: 'Tampere'},
        {koodiId: 789, nimi: 'Turku'},
        {koodiId: 790, nimi: 'Seinäjöki'},
        {koodiId: 484, nimi: 'Oulu'}
    ];
});
OsoiteKoostepalvelu.factory('DummyOppilaitostyyppis', function() {
    return [
        {koodiId: 1, nimi: 'Ammattikorkeakoulut'},
        {koodiId: 2, nimi: 'Kesäyliopistot'},
        {koodiId: 3, nimi: 'Lukiot'}
    ];
});
OsoiteKoostepalvelu.factory('DummyOmistajatyyppis', function() {
    return [
        {koodiId: 1, nimi: 'Ahvenanmaa'},
        {koodiId: 2, nimi: 'Kunta'},
        {koodiId: 3, nimi: 'Kuntaryhmä'},
        {koodiId: 4, nimi: 'Ohjeistajatyyppi tuntematon'},
        {koodiId: 5, nimi: 'Valtio'},
        {koodiId: 6, nimi: 'Yksityinen'}
    ];
});
OsoiteKoostepalvelu.factory('DummyVuosiluokkas', function() {
    return [
        {koodiId: 1, nimi: 'esiopetus'},
        {koodiId: 2, nimi: 'vuosiluokka 1'},
        {koodiId: 3, nimi: 'vuosiluokka 2'},
        {koodiId: 4, nimi: 'vuosiluokka 3'},
        {koodiId: 5, nimi: 'vuosiluokka 4'},
        {koodiId: 6, nimi: 'vuosiluokka 5'},
        {koodiId: 7, nimi: 'vuosiluokka 6'},
        {koodiId: 8, nimi: 'vuosiluokka 7'},
        {koodiId: 9, nimi: 'vuosiluokka 8'},
        {koodiId: 10, nimi: 'vuosiluokka 9'}
    ];
});
OsoiteKoostepalvelu.factory('DummyKoulutuksenjarjestajas', function() {
    return [
        {koodiId: 1, nimi: 'Helsingin kaupunki'},
        {koodiId: 2, nimi: 'Helsingin Konservatorion Säätiö'},
        {koodiId: 3, nimi: 'Helsingin normaalilyseo'}
    ];
});

OsoiteKoostepalvelu.factory('DummyResults', function() {
    return {
        presentation: {
                organisaationNimiIncluded: true,
                organisaatiotunnisteIncluded: true,
                yhteyshenkiloIncluded: false,
                yhteyshenkiloEmailIncluded: false,
                positosoiteIncluded: false,
                katuosoiteIncluded: false,
                postinumeroIncluded: false,
                pLIncluded: false,
                postinumeroIncluded: false,
                puhelinnumeroIncluded: false,
                faksinumeroIncluded: false,
                wwwOsoiteIncluded: false,
                viranomaistiedotuksenSahkopostiosoiteIncluded: false,
                koulutusneuvonnanSahkopostiosoiteIncluded: false,
                kriisitiedotuksenSahkopostiosoiteIncluded: false,
                organisaationSijaintikuntaIncluded: false
        },
        rows: [
            {organisaatioOid: 56121, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56122, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56110, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56124, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56125, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56116, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56127, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56128, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56119, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56131, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56132, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56150, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56141, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56142, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56160, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56161, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56172, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56170, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56181, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56182, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56190, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56191, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56183, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56184, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56192, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56194, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56155, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56156, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56163, nimi: "Aalto-yliopiston urheiluseura"},
            {organisaatioOid: 56164, nimi: "Aalto-yliopistokiinteistöt Oy"},
            {organisaatioOid: 56175, nimi: "Aalto-yliopiston Sähköinsinöörikilta"},
            {organisaatioOid: 56176, nimi: "Aalto-yliopiston urheiluseura"}
        ]
    };
});

OsoiteKoostepalvelu.service('SavesService', function($log, DummySaves, $filter, FilterHelper, SaveConverter) {
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
        success(SaveConverter.fromDomain(
            {"id":5,"searchType":"CONTACT","createdAt":1386869725347,"addressFields":["ORGANIAATIO_NIMI","POSTIOSOITE","VIRANOMAISTIEDOTUS_EMAIL"],"targetGroups":[{"type":"OPPISOPIMUSTOIMPISTEET","options":[]},{"type":"MUUT_ORGANISAATIOT","options":["TUNNUKSENHALTIJAT"]},{"type":"TUTKINTOTOIMIKUNNAT","options":["PUHEENJOHTAJA","JASENET"]},{"type":"KOULUTA_KAYTTAJAT","options":["TUNNUKSENHALTIJAT"]},{"type":"AIPAL_KAYTTAJAT","options":["TUNNUKSENHALTIJAT"]}],"terms":[{"type":"tutkintotoimikuntas","values":[]},{"type":"tutkintotoimikuntaRoolis","values":["PUHEENJOHTAJA","JASENET"]},{"type":"koulutaRoolis","values":[]},{"type":"aipalRoolis","values":[]},{"type":"organisaationKielis","values":[]},{"type":"avis","values":[]},{"type":"maakuntas","values":[]},{"type":"kuntas","values":[]},{"type":"oppilaitostyyppis","values":[]},{"type":"omistajatyyppis","values":[]},{"type":"vuosiluokkas","values":[]},{"type":"koultuksenjarjestajas","values":[]}]}
        ));
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
                               DummyVuosiluokkas, DummyKoulutuksenjarjestajas,
                               DummyTutkintotoimikuntas, TutkintotoimikuntaRoolis,
                               DummyKoulutaRoolis, DummyAipalRoolis,
                               DummyOrganisaatioKielis ) {

    this.listTutkintotoimikuntas = function(success) {
        success( DummyTutkintotoimikuntas );
    };

    this.listTutkintotoimikuntaRoolis = function(success) {
        success( TutkintotoimikuntaRoolis );
    };

    this.listKoulutaRoolis = function(success) {
        success( DummyKoulutaRoolis );
    };

    this.listAipalRoolis = function(success) {
        success( DummyAipalRoolis );
    };

    this.listOrganisaationKielis = function(success) {
        success( DummyOrganisaatioKielis );
    };

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
