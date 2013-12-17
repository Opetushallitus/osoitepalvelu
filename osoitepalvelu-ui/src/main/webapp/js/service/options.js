/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('OptionsService', function($log, $http, TutkintotoimikuntaRoolis) {

    var _commonErrorHandler = function(e) {
        $log.error(e);
    };

    this.listTutkintotoimikuntas = function(success, error) {
        // TODO:
        success( [] );
    };

    this.listTutkintotoimikuntaRoolis = function(success, error) {
        success( TutkintotoimikuntaRoolis );
    };

    this.listKoulutaRoolis = function(success, error) {
        // TODO:
        success( [] );
    };

    this.listAipalRoolis = function(success, error) {
        // TODO:
        success( [] );
    };

    this.listOrganisaationKielis = function(success, error) {
    	 $http.get('api/koodisto/opetuskieli').success(success).error(error || _commonErrorHandler);
    };

    this.listAvis = function(success, error) {
    	$http.get('api/koodisto/avi').success(success).error(error || _commonErrorHandler);
    };

    this.listMaakuntas = function(success, error) {
    	$http.get('api/koodisto/maakunta').success(success).error(error || _commonErrorHandler);
    };

    this.listKuntas = function(success, error) {
    	$http.get('api/koodisto/kunta').success(success).error(error || _commonErrorHandler);
    };

    this.listOppilaitostyyppis = function(success, error) {
    	$http.get('api/koodisto/oppilaitostyyppi').success(success).error(error || _commonErrorHandler);
    };

    this.listOmistajatyyppis = function(success, error) {
    	$http.get('api/koodisto/omistajatyyppi').success(success).error(error || _commonErrorHandler);
    };

    this.listVuosiluokkas = function(success, error) {
    	$http.get('api/koodisto/vuosiluokka').success(success).error(error || _commonErrorHandler);
    };

    this.listKoultuksenjarjestajas = function(success, error) {
    	$http.get('api/koodisto/koulutuksenjarjestaja').success(success).error(error || _commonErrorHandler);
    };
});