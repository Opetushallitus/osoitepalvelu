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

    this.listTutkintotoimikuntas = function(success, error) {
        _get('api/koodisto/tutkintotoimikuntas', success, error);
    };

    this.listTutkintotoimikuntaRoolis = function(success, error) {
        _get('api/koodisto/tutkintotoimikuntaRoolis', function(data) {
            if (data && data.length) {
                angular.forEach(data, function(koodi) {
                    koodi.nimi = LocalisationService.t('tutkintotoimikunta_rooli_'+koodi.koodiId);
                    koodi.lyhytNimi = LocalisationService.t('tutkintotoimikunta_rooli_'+koodi.koodiId);
                });
                success(data);
            } else {
                // Data not yet available:
                success( TutkintotoimikuntaRoolis );
            }
        }, error);
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

    this.listTutkintos = function(success, error) {
        _get('api/koodisto/tutkinto', success, error);
    };

    this.listKoulutusalas = function(success, error) {
        _get('api/koodisto/koulutusala', success, error);
    };

    this.listOpintoalas = function(success, error) {
        _get('api/koodisto/opintoala', success, error);
    };

    this.listOpintoalasByKoulutusalas = function(koulutusalas, success, error) {
        if (!koulutusalas || koulutusalas.length < 1) {
            _get('api/koodisto/opintoala', success, error);
        } else {
            $log.info("Listing opintoalas by koulutusalas.");
            $log.info(koulutusalas);
            _post('api/koodisto/opintoala', {koulutusala: koulutusalas}, success, error);
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
            _post('api/koodisto/koulutus', {opintoala: opintoalas, tyyppi: tyyppis}, success, error);
        }
    };

    this.listKoulutus = function(success, error) {
        _get('api/koodisto/koulutus', success, error);
    };

    this.listKoulutusTyyppis = function(success, error) {
        _get('api/koodisto/koulutustyyppi', success, error);
    };

    this.listKoulutusLajis = function(success, error) {
        _get('api/koodisto/koulutuslaji', success, error);
    };

    this.listKielis = function(success, error) {
        _get('api/koodisto/kieli', success, error);
    };

}]);