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
OsoiteKoostepalvelu.service('OptionsService', ["$log", "$http", "LocalisationService",
        "commonErrorHandler",
        function($log, $http, LocalisationService, commonErrorHandler) {

    // Cache here only means that user should refresh the page (or re-navigate to it) in order to referesh the options.
    // We can avoid a number of requests when going back to the search from the results.
    var _getCache = {},
        _postCache = {},
    _get = function( url, success, error ) {
        var lang = LocalisationService.getLocale();
        if( _getCache[url+lang] ) {
            success( _getCache[url+lang] );
        } else {
            $http.get(url, {params: {lang: lang}} ).success(function(data) {
                _getCache[url] = data;
                success(data);
            }).error(error || commonErrorHandler);
        }
    },
    _post = function( url, params, success, error ) {
        params.lang = LocalisationService.getLocale();
        var k = JSON.stringify({url: url, params: params});
        if( _postCache[k] ) {
            success( _postCache[k] );
        } else {
            $http.post(url, {}, {params: params}).success(function(data) {
                _postCache[k] = data;
                success(data);
            }).error(error || commonErrorHandler);
        }
    };

    this.listKoulutaRoolis = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'kayttoikeusryhma'), success, error);
    };

    this.listAipalRoolis = function(success, error) {
        // TODO:
        success( [] );
    };

    this.listOrganisaationKielis = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'opetuskieli'), success, error);
    };

    this.listAvis = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'avi'), success, error);
    };

    this.listMaakuntas = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'maakunta'), success, error);
    };

    this.listKuntas = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'kunta'), success, error);
    };

    this.listOppilaitostyyppis = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'oppilaitostyyppi'), success, error);
    };

    this.listOmistajatyyppis = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'omistajatyyppi'), success, error);
    };

    this.listVuosiluokkas = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'vuosiluokka'), success, error);
    };

    this.listKoultuksenjarjestajas = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'koulutuksenjarjestaja'), success, error);
    };

    this.listTutkintos = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'tutkinto'), success, error);
    };

    this.listKoulutusalas = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'koulutusala'), success, error);
    };

    this.listOpintoalas = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'opintoala'), success, error);
    };

    this.listOpintoalasByKoulutusalas = function(koulutusalas, success, error) {
        if (!koulutusalas || koulutusalas.length < 1) {
            _get(window.url('osoitekoostepalvelu.koodisto', 'opintoala'), success, error);
        } else {
            $log.info("Listing opintoalas by koulutusalas.");
            $log.info(koulutusalas);
            _post(window.url('osoitekoostepalvelu.koodisto', 'opintoala'), {koulutusala: koulutusalas}, success, error);
        }
    };

    this.listKoulutusByOpintoalasOrTyyppis = function(opintoalas, tyyppis, success, error) {
        if ((!opintoalas || opintoalas.length < 1)
                && (!tyyppis || tyyppis.length < 1)) {
            success([]);
            //_get('api/koodisto/koulutus', success, error); // too many results -> slow UI
        } else {
            $log.info("Listing koulutus by opintoalas or tyyppis.");
            $log.info(opintoalas);
            $log.info(tyyppis);
            _post(window.url('osoitekoostepalvelu.koodisto', 'koulutus'), {opintoala: opintoalas, tyyppi: tyyppis}, success, error);
        }
    };

    this.listKoulutus = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'koulutus'), success, error);
    };

    this.listKoulutusTyyppis = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'koulutustyyppi'), success, error);
    };

    this.listKoulutusLajis = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'koulutuslaji'), success, error);
    };

    this.listKielis = function(success, error) {
        _get(window.url('osoitekoostepalvelu.koodisto', 'kieli'), success, error);
    };

}]);