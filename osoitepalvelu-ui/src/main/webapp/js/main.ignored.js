/**
 * Created by ratamaa on 12/4/13.
 */

window.CONFIG = window.CONFIG || {};
window.CONFIG.app = {
    "userLanguages": ['kieli_fi', 'kieli_sv', 'kieli_en'], //default languages
    "ui.timeout.short": 10000,
    "ui.timeout.long": 60000
};

var OsoiteKoostepalvelu = angular.module('OsoiteKoostepalvelu',
        ['ngRoute', 'Helpers', 'I18n', 'ui.bootstrap', 'ui.select2', 'ngGrid', 'loading',
            'localisation', 'auth', 'flash']);

OsoiteKoostepalvelu.factory('SearchResultProvider', function($http, LocalisationService) {
    return function(details) {
        $http.post('api/search/list.json', details.data, {
            params: {lang: LocalisationService.getLocale()}
        })
        .success(details.callback)
        .error(details.errorCallback);
    }
});

if (!(window.console && console.log)) {
    console = {
        log: function() {
        },
        debug: function() {
        },
        info: function() {
        },
        warn: function() {
        },
        error: function() {
        }
    };
}


function osoitepalveluInit() {
    var loader = $("div#ajax-loader");

	var init_counter = 0;
	var fail = false;

	jQuery.support.cors = true;

	function initFail(id, xhr, status) {
	    fail = true;
	    console.log("Init failure: " + id + " -> "+status, xhr);
	    loader.toggleClass("fail", true);
	}

	function initFunction(id, xhr, status) {
	    init_counter--;

	    console.log("Got ready signal from: " + id + " -> "+status+" -> IC="+init_counter/*, xhr*/);

	    if (!fail && init_counter == 0) {
	        angular.element(document).ready(function() {
	            angular.module('myApp', ['OsoiteKoostepalvelu']);
	            angular.bootstrap(document, ['myApp']);
		    	loader.toggleClass("pre-init", false);
	        });
	    }
	};

	function logRequest(xhr, status) {
		 console.log("LOG "+status+": "+xhr.status+" "+xhr.statusText, xhr);
	}

    if ( !(window.CONFIG.mode && window.CONFIG.mode == 'dev-without-backend') ) {
        //
        // Preload application localisations for Osoitepalvelu
        //
        var localisationUrl = window.CONFIG.env.osoitepalveluLocalisationRestUrl + "?category=osoitepalvelu&value=cached";
        console.log("** Loading localisation info; from: ", localisationUrl);
        init_counter++;
        jQuery.ajax(localisationUrl, {
            dataType: "json",
            crossDomain:true,
            complete: logRequest,
            success: function(xhr, status) {
                window.CONFIG.env["osoitepalvelu.localisations"] = xhr;
                initFunction("localisations", xhr, status);
            },
            error: function(xhr, status) {
                window.CONFIG.env["osoitepalvelu.localisations"] = [];
                initFail("localisations", xhr, status);
            }
        });
    } else {
        init_counter++;
        initFunction('dev', {}, 200);
    }
}

