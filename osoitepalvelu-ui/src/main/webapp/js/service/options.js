/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('OptionsService', function($log, $http, TutkintotoimikuntaRoolis) {

    this.listTutkintotoimikuntas = function(success) {
        // TODO:
        success( [] );
    };

    this.listTutkintotoimikuntaRoolis = function(success) {
        success( TutkintotoimikuntaRoolis );
    };

    this.listKoulutaRoolis = function(success) {
        // TODO:
        success( [] );
    };

    this.listAipalRoolis = function(success) {
        // TODO:
        success( [] );
    };

    this.listOrganisaationKielis = function(success) {
    	 $http.get('api/koodisto/opetuskieli').success(success);
    };

    this.listAvis = function(success) {
    	$http.get('api/koodisto/avi').success(success);
    };

    this.listMaakuntas = function(success) {
    	$http.get('api/koodisto/maakunta').success(success);
    };

    this.listKuntas = function(success) {
    	$http.get('api/koodisto/kunta').success(success);
    };

    this.listOppilaitostyyppis = function(success) {
    	$http.get('api/koodisto/oppilaitostyyppi').success(success);
    };

    this.listOmistajatyyppis = function(success) {
    	$http.get('api/koodisto/omistajatyyppi').success(success);
    };

    this.listVuosiluokkas = function(success) {
    	$http.get('api/koodisto/vuosiluokka').success(success);
    };

    this.listKoultuksenjarjestajas = function(success) {
    	$http.get('api/koodisto/koulutuksenjarjestaja').success(success);
    };
});