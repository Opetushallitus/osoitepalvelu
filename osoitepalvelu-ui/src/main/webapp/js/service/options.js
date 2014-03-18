/**
 * Created by ratamaa on 12/4/13.
 */
OsoiteKoostepalvelu.service('OptionsService', function($log, $http, TutkintotoimikuntaRoolis, LocalisationService,
                commonErrorHandler) {

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
});