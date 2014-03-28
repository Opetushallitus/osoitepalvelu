/*
 * Copyright (c) 2013 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 */

// Apinoitu valintaperusteet / auth.js


/**
 * Authentication module.
 * NOTE: data (pre)loaded at server startup in index.hrml to Config.env["cas.userinfo"]
 */

var auth = angular.module("auth", ['ngResource', 'config']);

var USER = "USER_";
var READ = "_READ";
var UPDATE = "_READ_UPDATE";
var CRUD = "_CRUD";
var OPH_ORG = "xxx";

auth.factory('MyRolesModel', ["$http", "$log", "Config", function($http, $log, Config) {

    //console.log("MyRolesModel()");
    OPH_ORG = Config.env["root.organisaatio.oid"];

    var factory = (function() {

        var instance = {};
        instance.organisaatiot=[];

        var defaultUserInfo = {lang:"fi", groups:[]};

        instance.userinfo = Config.env.cas!==undefined ? Config.env.cas.userinfo || defaultUserInfo:defaultUserInfo;
        instance.myroles = instance.userinfo.groups;

        /**
         * prosessoi roolilistan läpi ja poimii tietoja, esim organisaatiot
         */
        var processRoleList=function(roolit) {
        	if(roolit!==undefined) {
        		for(var i=0;i<roolit.length;i++) {
        			var oidList = roolit[i].match(/_[0-9\.]+$/g);
        			if(oidList && oidList.length>0) {
        				//poimi tarjonta roolit
        				if(roolit[i].indexOf("APP_TARJONTA")==0 && roolit[i].indexOf("KK")==-1) {
        					var org = oidList[0].substring(1);

                                                if(roolit[i].indexOf('CRUD')!=-1 || roolit[i].indexOf('UPDATE')!=-1) {
                                                  console.log("adding org:", org, roolit[i]);
                                                  instance.organisaatiot.push(org);
                                                }
        				}
        			}
        		}
        	}
        };

//        console.log("myroles:", instance.myroles);

      	processRoleList(instance.myroles);

        return instance;
    })();

    return factory;
}]);

auth.factory('AuthService', ["$q", "$http", "$timeout", "$log", "MyRolesModel", "Config",
        function($q, $http, $timeout, $log, MyRolesModel, Config) {

	var ORGANISAATIO_URL_BASE;

	if(undefined!==Config.env){
		ORGANISAATIO_URL_BASE=Config.env["organisaatio.api.rest.url"];
	}

	//console.log("prefix:", ORGANISAATIO_URL_BASE);

    // CRUD ||UPDATE || READ
    var readAccess = function(service, org) {
        //$log.info("readAccess()", service, org);
    	return MyRolesModel.myroles.indexOf(service + READ + "_" + org) > -1 ||
               MyRolesModel.myroles.indexOf(service + UPDATE + "_" + org) > -1 ||
               MyRolesModel.myroles.indexOf(service + CRUD + "_" + org) > -1;
    };

    // CRUD ||UPDATE
    var updateAccess = function(service, org) {
        //$log.info("updateAccess()", service, org, MyRolesModel);
        return MyRolesModel.myroles.indexOf(service + UPDATE + "_" + org) > -1 ||
                MyRolesModel.myroles.indexOf(service + CRUD + "_" + org) > -1;
    };

    // CRUD
    var crudAccess = function(service, org) {
//        $log.info("crudAccess()", service, org);
        return MyRolesModel.myroles.indexOf(service + CRUD + "_" + org) > -1;
    };

    //async call, returns promise!
    var accessCheck = function(service, orgOid, accessFunction) {
//        $log.info("accessCheck(), service,org,fn:", service, orgOid, accessFunction);

        if(orgOid===undefined || (orgOid.length && orgOid.length==0)) {
        	throw "missing org oid!";
        }
        var deferred = $q.defer();
//        console.log("accessCheck().check()", service, orgOid, accessFunction);
      	var url = ORGANISAATIO_URL_BASE + "organisaatio/" + orgOid + "/parentoids";
//       	console.log("getting url:", url);

      	$http.get(url,{cache:true}).then(function(result) {
//        console.log("got:", result);

        var ooids = result.data.split("/");

        for(var i=0;i<ooids.length;i++) {
            if (accessFunction(service, ooids[i])) {
                deferred.resolve(true);
                return;
            }
        }
        deferred.resolve(false);
        }, function(){ //failure funktio
//           	console.log("could not get url:", url);
            deferred.resolve(false);
        });

        return deferred.promise;
    };

    return {
        getUsername: function() {
        	return Config.env.cas.userinfo.uid;
        },
        isLoggedIn: function() {
        	return Config.env.cas.userinfo.uid!==undefined;
        },
        /**
         * onko käyttäjällä lukuoikeus, palauttaa promisen
         * @param service
         * @param orgOid
         * @returns
         */
        readOrg: function(orgOid, service) {
            return accessCheck(service||'APP_TARJONTA', orgOid, readAccess);
        },
        /**
         * onko käyttäjällä päivitysoikeus, palauttaa promisen
         * @param service
         * @param orgOid
         * @returns
         */
        updateOrg: function(orgOid, service) {
            return accessCheck(service||'APP_TARJONTA', orgOid, updateAccess);
        },
        /**
         * onko käyttäjällä crud oikeus, palauttaa promisen
         * @param orgOid
         * @param service
         * @returns
         */
        crudOrg: function(orgOid, service) {
//        	console.log("crudorg", orgOid, service);
            return accessCheck(service||'APP_TARJONTA', orgOid, crudAccess);
        },
        /**
         * Palauttaa käyttäjän kielen
         */
        getLanguage: function(){
        	return MyRolesModel.userinfo.lang;
        },
        /**
         * Palauttaa käyttäjän oidin
         */
        getUserOid: function(){
        	return MyRolesModel.userinfo.oid;
        },

        /**
         * Palauttaa käyttäjän etunimen
         */
        getFirstName: function(){
        	return MyRolesModel.userinfo.firstName;
        },

        /**
         * Palauttaa käyttäjän etunimen
         */
        getLastName: function(){
        	return MyRolesModel.userinfo.lastName;
        },

        isUserOph : function() {

            var ophUser = false;

            angular.forEach(MyRolesModel.organisaatiot,function(orgOid){

                if (orgOid === Config.env['root.organisaatio.oid']) {
                    ophUser = true;
                }
            });

            return ophUser;

        },

        /**
         * Palauttaa käyttäjän organisaatiot ('TARJONTA-APP') joihin muokkaus/luontioikeudet
         */
        getOrganisations: function(){
        	//TODO palauta kopio?
        	return MyRolesModel.organisaatiot;
        }

    };
}]);

