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


window.CONFIG = window.CONFIG || {};
window.CONFIG.app = {
    "userLanguages": ['kieli_fi', 'kieli_sv', 'kieli_en'], //default languages
    "ui.timeout.short": 10000,
    "ui.timeout.long": 60000
};

var OsoiteKoostepalvelu = angular.module('OsoiteKoostepalvelu',
        ['ngRoute', 'ngSanitize', 'ngCookies', 'Helpers', 'I18n', 'ui.bootstrap', 'ui.select', 'ngGrid', 'loading',
            'localisation', 'auth', 'angular-flash.service', 'angular-flash.flash-alert-directive', 'ng.shims.placeholder']);

OsoiteKoostepalvelu.run(function($http, $cookies) {
    $http.defaults.headers.common['clientSubSystemCode'] = "osoitekoostepalvelu.osoitepalvelu-ui.frontend";
    if($cookies['CSRF']) {
        $http.defaults.headers.common['CSRF'] = $cookies['CSRF'];
    }
});

OsoiteKoostepalvelu.factory('SearchResultProvider', ["$http", "LocalisationService",
        function($http, LocalisationService) {
    return function(details) {
        $http.post(window.url('osoitekoostepalvelu.search', 'list.json'), details.data, {
            params: {lang: details.lang}
        })
        .success(details.callback)
        .error(details.errorCallback);
    }
}]);

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
	}

	function logRequest(xhr, status) {
		 console.log("LOG "+status+": "+xhr.status+" "+xhr.statusText, xhr);
	}

    if (window.url('cas.me')) {
        init_counter++;
        jQuery.ajax(window.CONFIG.env.useCasMeUrl != "false" ? window.url('cas.me') : "cas_me_test.json", {
            dataType: "json",
            crossDomain:true,
            complete: logRequest,
            success: function(xhr, status) {
                window.CONFIG.env["me"] = xhr;
                initFunction("casMe", xhr, status);
            },
            error: function(xhr, status) {
                window.CONFIG.env["me"] = {};
                if (!xhr.status && xhr.responseJSON) {
                    // call to local JSON file:
                    initFunction("casMe", xhr.responseJSON, status);
                } else {
                    initFail("casMe", xhr, status);
                }
            }
        });
    }

    if ( !(window.CONFIG.mode && window.CONFIG.mode == 'dev-without-backend') ) {
        //
        // Preload application localisations for Osoitepalvelu
        //
        var localisationUrl = window.url('lokalisointi.localisation',
          {category: "osoitepalvelu", value: "cached"});
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

