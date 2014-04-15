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
        {koodiUrl: 1544854, nimi:'Ajoneuvonosturinkuljettajan tutkintotoimikunta'},
        {koodiUrl: 1544855, nimi:'Examenskommissionen inom företagarbranschen och töretagsekonomi'}
    ];
});

OsoiteKoostepalvelu.factory("DummyKoulutaRoolis", function() {
    return [
        {koodiUrl: 1, nimi:'ARVOSANAPAIV'},
        {koodiUrl: 2, nimi:'HAKEMUSTIETALLENTAJA'}
    ];
});
OsoiteKoostepalvelu.factory("DummyAipalRoolis", function() {
    return [
        {koodiUrl: 1, nimi:'AIPAL-koulutuksen/tutkinnon järjestäjän käyttäjä'}
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
        {koodiUrl: 232323, nimi: 'Kaikki alueet paitsi Ahvenanmaa'},
        {koodiUrl: 123154, nimi: 'Etelä-Suomen AVI'},
        {koodiUrl: 123155, nimi: 'Lapin AVI'}
    ];
});
OsoiteKoostepalvelu.factory('DummyMaakuntas', function() {
    return [
        {koodiUrl: 123153, nimi: 'Ahvenanmaa'},
        {koodiUrl: 123155, nimi: 'Pirkanmaa'},
        {koodiUrl: 123154, nimi: 'Pohjois-Karjala'},
        {koodiUrl: 232323, nimi: 'Uusimaa'}
    ];
});
OsoiteKoostepalvelu.factory('DummyKuntas', function() {
    return [
        {koodiUrl: 456, nimi: 'Espoo'},
        {koodiUrl: 123, nimi: 'Helsinki'},
        {koodiUrl: 485, nimi: 'Jyväskylä'},
        {koodiUrl: 789, nimi: 'Tampere'},
        {koodiUrl: 789, nimi: 'Turku'},
        {koodiUrl: 790, nimi: 'Seinäjöki'},
        {koodiUrl: 484, nimi: 'Oulu'}
    ];
});
OsoiteKoostepalvelu.factory('DummyOppilaitostyyppis', function() {
    return [
        {koodiUrl: 1, nimi: 'Ammattikorkeakoulut'},
        {koodiUrl: 2, nimi: 'Kesäyliopistot'},
        {koodiUrl: 3, nimi: 'Lukiot'}
    ];
});
OsoiteKoostepalvelu.factory('DummyOmistajatyyppis', function() {
    return [
        {koodiUrl: 1, nimi: 'Ahvenanmaa'},
        {koodiUrl: 2, nimi: 'Kunta'},
        {koodiUrl: 3, nimi: 'Kuntaryhmä'},
        {koodiUrl: 4, nimi: 'Ohjeistajatyyppi tuntematon'},
        {koodiUrl: 5, nimi: 'Valtio'},
        {koodiUrl: 6, nimi: 'Yksityinen'}
    ];
});
OsoiteKoostepalvelu.factory('DummyVuosiluokkas', function() {
    return [
        {koodiUrl: 1, nimi: 'esiopetus'},
        {koodiUrl: 2, nimi: 'vuosiluokka 1'},
        {koodiUrl: 3, nimi: 'vuosiluokka 2'},
        {koodiUrl: 4, nimi: 'vuosiluokka 3'},
        {koodiUrl: 5, nimi: 'vuosiluokka 4'},
        {koodiUrl: 6, nimi: 'vuosiluokka 5'},
        {koodiUrl: 7, nimi: 'vuosiluokka 6'},
        {koodiUrl: 8, nimi: 'vuosiluokka 7'},
        {koodiUrl: 9, nimi: 'vuosiluokka 8'},
        {koodiUrl: 10, nimi: 'vuosiluokka 9'}
    ];
});
OsoiteKoostepalvelu.factory('DummyKoulutuksenjarjestajas', function() {
    return [
        {koodiUrl: 'koulutustoimija_12345', koodiId: '12345-1', nimi: 'Helsingin kaupunki'},
        {koodiUrl: 'koulutustoimija_12346', koodiId: '12345-2', nimi: 'Helsingin Konservatorion Säätiö'},
        {koodiUrl: 'koulutustoimija_12347', koodiId: '12345-3', nimi: 'Helsingin normaalilyseo'}
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

OsoiteKoostepalvelu.service('SavesService', ["$log", "DummySaves", "$filter", "FilterHelper", "SaveConverter",
        function($log, DummySaves, $filter, FilterHelper, SaveConverter) {
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
}]);

OsoiteKoostepalvelu.factory('SearchResultProvider', ["DummyResults", function(DummyResults) {
    return function(details) {
        details.callback( DummyResults );
    }
}]);

OsoiteKoostepalvelu.service('OptionsService', [
               "$log", "DummyAVIs", "DummyMaakuntas", "DummyKuntas", "DummyOppilaitostyyppis", "DummyOmistajatyyppis",
               "DummyVuosiluokkas", "DummyKoulutuksenjarjestajas", "DummyTutkintotoimikuntas",
               "TutkintotoimikuntaRoolis", "DummyKoulutaRoolis", "DummyAipalRoolis", "DummyOrganisaatioKielis",
        function($log, DummyAVIs, DummyMaakuntas, DummyKuntas, DummyOppilaitostyyppis, DummyOmistajatyyppis,
               DummyVuosiluokkas, DummyKoulutuksenjarjestajas, DummyTutkintotoimikuntas, TutkintotoimikuntaRoolis,
               DummyKoulutaRoolis, DummyAipalRoolis, DummyOrganisaatioKielis ) {

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
}]);
