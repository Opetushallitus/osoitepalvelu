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
OsoiteKoostepalvelu.service('OptionsService', ["$log", "$http", "TutkintotoimikuntaRoolis", "LocalisationService",
        "commonErrorHandler",
        function($log, $http, TutkintotoimikuntaRoolis, LocalisationService, commonErrorHandler) {

    // Cache here only means that user should refresh the page (or re-navigate to it) in order to referesh the options.
    // We can avoid a number of requests when going back to the search from the results.
    var _getCache = {},
    _get = function( url, success, error ) {
        if( _getCache[url] ) {
            success( _getCache[url] );
        } else {
            $http.get(url, {params: {lang: LocalisationService.getLocale()}} ).success(function(data) {
                _getCache[url] = data;
                success(data);
            }).error(error || commonErrorHandler);
        }
    };

    this.listTutkintotoimikuntas = function(success, error) {
        // TODO:
        success( [] );
    };

    this.listTutkintotoimikuntaRoolis = function(success, error) {
        success( TutkintotoimikuntaRoolis );
    };

    this.listKoulutaRoolis = function(success, error) {
        _get('api/koodisto/kayttoikeusryhma', success, error);
    };

    this.listAipalRoolis = function(success, error) {
        // TODO:
        success( [] );
    };

    this.listOrganisaationKielis = function(success, error) {
        _get('api/koodisto/opetuskieli', success, error);
    };

    this.listAvis = function(success, error) {
        _get('api/koodisto/avi', success, error);
    };

    this.listMaakuntas = function(success, error) {
        _get('api/koodisto/maakunta', success, error);
    };

    this.listKuntas = function(success, error) {
        _get('api/koodisto/kunta', success, error);
    };

    this.listOppilaitostyyppis = function(success, error) {
        _get('api/koodisto/oppilaitostyyppi', success, error);
    };

    this.listOmistajatyyppis = function(success, error) {
        _get('api/koodisto/omistajatyyppi', success, error);
    };

    this.listVuosiluokkas = function(success, error) {
        _get('api/koodisto/vuosiluokka', success, error);
    };

    this.listKoultuksenjarjestajas = function(success, error) {
        _get('api/koodisto/koulutuksenjarjestaja', success, error);
    };
}]);