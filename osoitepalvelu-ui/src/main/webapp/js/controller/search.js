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
 * Created by ratamaa on 12/3/13.
 */
var OsoiteKoostepalvelu = angular.module('OsoiteKoostepalvelu');
OsoiteKoostepalvelu.controller('SearchController', ["$scope", "$log", "$modal", "$location", "$filter", "SearchService",
    "SearchTypes", "TargetGroups", "EmptyTerms", "FilterHelper", "ArrayHelper", "ExtractHelper", "KoodiHelper",
    "SavesService", "OptionsService", "LocalisationService", "Osoitekielis",
    function($scope, $log, $modal, $location, $filter, SearchService,
    SearchTypes, TargetGroups, EmptyTerms, FilterHelper, ArrayHelper, ExtractHelper, KoodiHelper,
    SavesService, OptionsService, LocalisationService, Osoitekielis) {
        $scope.msg = function( key, params ) {
            return LocalisationService.t(key, params);
        };

        var updateSaves = function() {
            SavesService.listSearch(function(data) {
                $scope.saves = data;
                $scope.selectedSavedSearch = SearchService.getSelectedSearch();
            });
        };

        var getCurrentSaveDetails = function() {
            var selectedSave = $scope.selectedSavedSearch ? $filter('filter')($scope.saves,
                {id: $scope.selectedSavedSearch})[0] : null;
            return {
                id: selectedSave ? selectedSave.id : null,
                name: selectedSave ? selectedSave.name : null,
                terms: $scope.terms,
                targetGroups: $scope.visibleTargetGroups,
                searchType: $scope.searchType,
                addressFields: $scope.addressFields,
                lang: $scope.osoitekieli
            };
        };

        $scope.allowClearConfig = {
            allowClear : true
        };

        $scope.saves = [];
        $scope.selectedSavedSearch = null;
        // TODO: Find better way to do this
        // http://stackoverflow.com/questions/28960094/loading-data-at-startup-and-displaying-via-controller-in-angularjs
        updateSaves();

        $scope.osoitekielis = Osoitekielis;

        $scope.koulutuslupa = {"kylla": false, "ei": false};

        $scope.updateTerms = function(updateOptions) {
            if( updateOptions === undefined ) {
                updateOptions = true;
            }
            $log.info("Update search terms");

            $scope.searchTypes = SearchTypes;
            $scope.searchType = SearchService.getSearchType();

            $scope.addressFields = SearchService.getAddressFields();

            $scope.osoitekieli = SearchService.getKieli();

            $scope.targetGroups = TargetGroups;
            $scope.selectedTargetGroup = null;
            $scope.visibleTargetGroups = SearchService.getTargetGroups();
            $scope.selectedTargetGroupTypes = [];
            angular.forEach($scope.visibleTargetGroups, function(v) {
                $scope.selectedTargetGroupTypes.push(v.type);
            } );
            $scope.showExtraTerms = false;

            if( updateOptions ) {
                $scope.options = angular.copy(EmptyTerms);
                OptionsService.listKoulutaRoolis(function(data) { $scope.options.koulutaRoolis = data; });
                OptionsService.listAipalRoolis(function(data) { $scope.options.aipalRoolis = data; });
                OptionsService.listOrganisaationKielis(function(data) { $scope.options.organisaationKielis = data; });
                //OptionsService.listAvis(function(data) { $scope.options.avis = data; });
                OptionsService.listMaakuntas(function(data) { $scope.options.maakuntas = data; });
                OptionsService.listKuntas(function(data) { $scope.options.kuntas = data; });
                OptionsService.listOppilaitostyyppis(function(data) { $scope.options.oppilaitostyyppis = data; });
                //OptionsService.listOmistajatyyppis(function(data) { $scope.options.omistajatyyppis = data; });
                OptionsService.listVuosiluokkas(function(data) { $scope.options.vuosiluokkas = data; });
                OptionsService.listKoultuksenjarjestajas(function(data) { $scope.options.koultuksenjarjestajas = data; });
                OptionsService.listKoulutusalas(function(data) { $scope.options.koulutusalas = data; });
                //OptionsService.listOpintoalas(function(data) {$scope.options.opintoalas = data;});
                OptionsService.listKoulutusTyyppis(function(data) {$scope.options.koulutustyyppis = data;});
                OptionsService.listKoulutusLajis(function(data) {$scope.options.koulutuslajis = data;});
                OptionsService.listKielis(function(data) {$scope.options.kielis = data;});
                //OptionsService.listTutkintos(function(data) {$scope.options.tutkintos = data;});
                OptionsService.listKoulutus(function(data) {$scope.options.koulutus = data});
            }
            $scope.terms = SearchService.getTerms();
            if ($scope.terms.koulutuslupaExists.length > 0) {
                $scope.koulutuslupa["kylla"] = $scope.terms.koulutuslupaExists.indexOf("true") > -1;
                $scope.koulutuslupa["ei"] = $scope.terms.koulutuslupaExists.indexOf("false") > -1;
            }
            $scope.koulutusalasChanged();

            $log.info($scope.terms);
        };

        $scope.effectiveSelectedOpintoalas = function() {
            if ((!$scope.terms.opintoalas || $scope.terms.opintoalas.length < 1)
                    && $scope.terms.koulutusalas && $scope.terms.koulutusalas.length) {
                return ExtractHelper.extract($scope.options.opintoalas,"koodiUri");
            }
            return $scope.terms.opintoalas;
        };

        $scope.opintoalasChanged = function() {
            $log.info("Opintoalas changed.");
            $log.info($scope.terms.opintoalas);
            OptionsService.listKoulutusByOpintoalasOrTyyppis($scope.effectiveSelectedOpintoalas(),
                $scope.terms.koulutustyyppis, function(data) {$scope.options.koulutus = data;});
        };

        $scope.koulutusalasChanged = function() {
            $log.info("Koulutusalas changed.");
            $log.info($scope.terms.koulutusalas);
            var update = function(data) {
                $scope.options.opintoalas = data;
                $log.info("Updated opintoalas based on koulutusalas.");
                $log.info(data);
                $scope.opintoalasChanged();
            };
            if (!$scope.terms.koulutusalas || $scope.terms.koulutusalas.length < 1) {
                OptionsService.listOpintoalas(update);
            } else {
                OptionsService.listOpintoalasByKoulutusalas($scope.terms.koulutusalas, update);
            }
        };

        $scope.koulutusTyyppisChanged = function() {
            $log.info("Koulutustyyppis changed.");
            $log.info($scope.terms.koulutustyyppis);
            OptionsService.listKoulutusByOpintoalasOrTyyppis($scope.effectiveSelectedOpintoalas(),
                $scope.terms.koulutustyyppis, function(data) {$scope.options.koulutus = data;});
        };

        $scope.koulutuslupaKyllaChanged = function() {
            $scope.koulutuslupa["ei"] = false;
            if ($scope.koulutuslupa["kylla"]) {
                $scope.terms.koulutuslupaExists = ["true"];
            } else {
                $scope.terms.koulutuslupaExists = [];
            }
        };

        $scope.koulutuslupaEiChanged = function () {
            $scope.koulutuslupa["kylla"] = false;
            if ($scope.koulutuslupa["ei"]) {
                $scope.terms.koulutuslupaExists = ["false"];
            } else {
                $scope.terms.koulutuslupaExists = [];
            }
        }

        $scope.updateTerms();

        $scope.clear = function() {
            $log.info("CLEAR");
            SearchService.clear();
            $scope.updateTerms();
            $scope.selectedSavedSearch = null;
        };

        $scope.toggleShowMore = function() {
            $scope.showExtraTerms = !$scope.showExtraTerms;
        };

        $scope.isShowTutkintohierarkiaTerms = function() {
            return $scope.selectedTargetGroupTypes.indexOf('KOULUTUKSEN_TARJOAJAT') !== -1;
        };

        $scope.isShowKoulutusTyyppiTerm = function() {
            return $scope.selectedTargetGroupTypes.indexOf('KOULUTUKSEN_TARJOAJAT') !== -1;
        };

        $scope.isShowKoulutusTerms = function() {
            return $scope.selectedTargetGroupTypes.indexOf('KOULUTUKSEN_TARJOAJAT') !== -1;
        };

        // Koulutustoimija
        $scope.isShowKoulutustoimija = function() {
            return $scope.selectedTargetGroupTypes.indexOf('JARJESTAJAT_YLLAPITAJAT') !== -1;
        };

        // Oppilaitos or toimipiste
        $scope.isShowOppilaitoksetToimipisteet = function () {
            return $scope.selectedTargetGroupTypes.indexOf('OPPILAITOKSET') !== -1
            || $scope.selectedTargetGroupTypes.indexOf('OPETUSPISTEET') !== -1;
        };

        // Koulutustoimija or Oppisopimustoimisto
        $scope.isShowKoulutustoimijaOppisopimustoimisto = function() {
            return $scope.selectedTargetGroupTypes.indexOf('JARJESTAJAT_YLLAPITAJAT') !== -1
                || $scope.selectedTargetGroupTypes.indexOf('OPPISOPIMUSTOIMPISTEET') !== -1;
        };

        // Palveluiden käyttäjät
        $scope.isShowPalveluidenKayttajat = function() {
            return $scope.selectedTargetGroupTypes.indexOf('KOULUTA_KAYTTAJAT') !== -1;
        };

        // Työelämäjärjestö
        $scope.isShowTyoelamajarjesto = function() {
            return $scope.selectedTargetGroupTypes.indexOf('TYOELAMAPALVELUT') !== -1;
        };

        // Varhaiskasvatuksen toimipaikka
        $scope.isShowVarhaiskasvatuksentoimipaikka = function() {
            return $scope.selectedTargetGroupTypes.indexOf('VARHAISKASVATUKSEN_TOIMIPAIKKA') !== -1;
        };

        // Varhaiskasvatuksen jarjestaja
        $scope.isShowVarhaiskasvatuksenjarjestaja = function() {
            return $scope.selectedTargetGroupTypes.indexOf('VARHAISKASVATUKSEN_JARJESTAJA') !== -1;
        };

        // Vuosiluokka term which is shown when certain oppilaitostyyppi(s) are selected.
        $scope.isShowVuosiluokkaTerm = function() {
            if($scope.selectedTargetGroupTypes.indexOf('OPPISOPIMUSTOIMPISTEET') !== -1) {
                return false;
            }
            var oppilaitosTyyppisWithVuosiluokkaSetting = window.CONFIG.env["vuosiluokka.for.oppilaitostyyppis"];
            if (oppilaitosTyyppisWithVuosiluokkaSetting) {
                var oppilaitosTyyppisWithVuosiluokka = oppilaitosTyyppisWithVuosiluokkaSetting.split(",");
                return ArrayHelper.containsAny(ArrayHelper.extract($scope.terms.oppilaitostyyppis, KoodiHelper.koodiValue),
                    oppilaitosTyyppisWithVuosiluokka);
            }
            return true;
        };

        $scope.clearVuosiluokka = function($item, $model) {
            if(!$scope.isShowVuosiluokkaTerm()) {
                $scope.terms.vuosiluokkas = [];
            }
        };

        // Handle the disable logic caused by relations of searchTypes and targetGroups.
        $scope.searchtypesDisableLogic = function(toDisable) {
            if(($filter('filter')($scope.selectedTargetGroupTypes, 'KOULUTA_KAYTTAJAT')).length
            && (toDisable == 'LETTER' || toDisable == 'CONTACT')) {
                return true;
            }
            if(($filter('filter')($scope.selectedTargetGroupTypes, 'TYOELAMAPALVELUT')).length
                && (toDisable == 'CONTACT')) {
                return true;
            }
            return false;
        };

        $scope.handleSaveSelected = function() {
            if( $scope.selectedSavedSearch ) {
                var selected = angular.copy($scope.selectedSavedSearch);
                $log.info("Selected save: " + selected);
                SavesService.getSearch($scope.selectedSavedSearch, function(save) {
                    SearchService.updateSearchType(save.searchType, save.addressFields);
                    SearchService.updateTargetGroups(save.targetGroups);
                    SearchService.updateTerms(save.terms);
                    SearchService.updateKieli(save.lang);
                    $scope.updateTerms(false);
                    $scope.selectedSavedSearch = selected;
                });
            }
            SearchService.updateSelectedSearch($scope.selectedSavedSearch);
        };

        $scope.handleTargetGroupSelected = function() {
            //$scope.selectedTargetGroup = tg;
            $log.info("Target group selection changed to: " + $scope.selectedTargetGroup);
            if ($scope.selectedTargetGroup) {
                $scope.selectedTargetGroupTypes.push($scope.selectedTargetGroup);
                var newGroup = angular.copy(
                    $filter('filter')($scope.targetGroups, {type: $scope.selectedTargetGroup})[0]);
                if (newGroup.options.length === 1) {
                    newGroup.options[0].selected = true;
                }
                return false;
            }
        };

        // Handle targetGroups search logic.
        $scope.targetgroupsDisableLogic = function(typeToDisable) {
            // Don't allow these search types for Palveluiden käyttäjä targetgroup.
            if(($scope.searchType == 'LETTER' || $scope.searchType  == 'CONTACT')
                && typeToDisable == 'KOULUTA_KAYTTAJAT') {
                return true;
            }
            // Palveluiden käyttäjä can't be combined to other targetgroups.
            if(($filter('filter')($scope.selectedTargetGroupTypes, 'KOULUTA_KAYTTAJAT')).length
            || typeToDisable == 'KOULUTA_KAYTTAJAT' && $scope.selectedTargetGroupTypes.length) {
                return true;
            }
            // Koulutuksen tarjoajat can't be combined to other targetgroups.
            if(($filter('filter')($scope.selectedTargetGroupTypes, 'KOULUTUKSEN_TARJOAJAT')).length
                || typeToDisable == 'KOULUTUKSEN_TARJOAJAT' && $scope.selectedTargetGroupTypes.length) {
                return true;
            }
            // Työelämäpalvelut can't be combined to other targetgroups.
            if(($filter('filter')($scope.selectedTargetGroupTypes, 'TYOELAMAPALVELUT')).length
                || typeToDisable == 'TYOELAMAPALVELUT' && $scope.selectedTargetGroupTypes.length) {
                return true;
            }
            if($scope.searchType === 'CONTACT') {

                if(typeToDisable !== 'JARJESTAJAT_YLLAPITAJAT') {
                    for(var i in $scope.addressFields) {
                        var addressfield = $scope.addressFields[i];
                        // kriisitiedotuksen_sähköpostiosoite can only be used with Koulutustoimijas
                        if( (addressfield.type === 'KRIISITIEDOTUKSEN_EMAIL' || addressfield.type === 'VARHAISKASVATUKSEN_YHTEYSHENKILO') && addressfield.selected) {
                            return true;
                        }
                    }
                }

                if(typeToDisable !== 'OPPILAITOKSET') {
                    for(var i in $scope.addressFields) {
                        var addressfield = $scope.addressFields[i];
                        if( (addressfield.type === 'KOSKI_YHDYSHENKILO' || addressfield.type === 'MOVE_YHTEYSHENKILO') && addressfield.selected) {
                            return true;
                        }
                    }
                }

            }

            if($scope.searchType === '')

            return false;
        };

        // Logic when address fields are allowed to use.
        $scope.addressFieldDisableLogic = function(addressField) {
            if( (addressField.type === 'KRIISITIEDOTUKSEN_EMAIL' || addressField.type === 'VARHAISKASVATUKSEN_YHTEYSHENKILO')
                && !($filter('filter')($scope.selectedTargetGroupTypes, 'JARJESTAJAT_YLLAPITAJAT')).length
                && $scope.selectedTargetGroupTypes.length) {
                return true;
            }
            if( (addressField.type === 'KOSKI_YHDYSHENKILO' || addressField.type === 'MOVE_YHTEYSHENKILO')
                && !($filter('filter')($scope.selectedTargetGroupTypes, 'OPPILAITOKSET')).length
                && $scope.selectedTargetGroupTypes.length) {
                return true;
            }
            return false;
        };

        $scope.handleSaveSelected = function() {
            if( $scope.selectedSavedSearch ) {
                var selected = angular.copy($scope.selectedSavedSearch);
                $log.info("Selected save: " + selected);
                SavesService.getSearch($scope.selectedSavedSearch, function(save) {
                    SearchService.updateSearchType(save.searchType, save.addressFields);
                    SearchService.updateTargetGroups(save.targetGroups);
                    SearchService.updateTerms(save.terms);
                    SearchService.updateKieli(save.lang);
                    $scope.updateTerms(false);
                    $scope.selectedSavedSearch = selected;
                });
            }
            SearchService.updateSelectedSearch($scope.selectedSavedSearch);
        };

        $scope.handleTargetGroupSelected = function() {
            $log.info("Target gorup selection changed to: "+$scope.selectedTargetGroup);
            if( $scope.selectedTargetGroup ) {
                $scope.selectedTargetGroupTypes.push($scope.selectedTargetGroup);
                var newGroup = angular.copy(
                    $filter('filter')($scope.targetGroups, {type: $scope.selectedTargetGroup})[0]);
                if( newGroup.options.length === 1 ) {
                    newGroup.options[0].selected=true;
                }
                $scope.visibleTargetGroups.push( newGroup );
                $scope.selectedTargetGroup = "";
            }
        };
        $scope.removeTargetGroup = function(i) {
            var arr = [];
            angular.forEach($scope.selectedTargetGroupTypes, function(v,index) {
                if( index !== i && v ) arr.push(v);
            });
            $scope.selectedTargetGroupTypes = arr;
            arr = [];
            angular.forEach( $scope.visibleTargetGroups, function (v, index) {
                if( index !== i ) arr.push( angular.copy(v) );
            } );
            $scope.visibleTargetGroups = arr;
            $scope.selectedTargetGroup = "";
        };

        $scope.selectableTargetGroupsFilter = FilterHelper.extractedFieldNotInArray;

        $scope.isTermsShown = function() {
            return !!$scope.searchType;
        };

        $scope.isShowTargetGroup = function() {
            return $scope.isTermsShown();
        };

        $scope.isSearchActionsVisible = function() {
            return $scope.isTermsShown();
        };

        $scope.isOptionSelected = function(options) {
            var result = false;

            if (angular.isArray(options) === false) {
                $log.warn("Target group options array invalid! ", options);
                return false;
            }
            angular.forEach(options, function(option) {
                if(option.selected === true) {
                    result = true;
                    return;
                }
            });
            return result;
        };

        $scope.isSearchAllowed = function() {
            // Lets check that one option is checked in every visible target group
            for(var i=0; i < $scope.visibleTargetGroups.length; i++) {
                if ($scope.isOptionSelected($scope.visibleTargetGroups[i].options) === false) {
                    return false;
                }
            }

            return $scope.isTermsShown() && $scope.visibleTargetGroups.length > 0;
        };

        $scope.saveSearch = function() {
            $log.info("Show save popup.");

            var onSaveNew = function() { return function(newSaveId) {
                $log.info("New save with id: " + newSaveId);
                SearchService.updateSelectedSearch(newSaveId);
                updateSaves();
            }; };
            var modalInstance = null;
            if( $scope.selectedSavedSearch ) {
                modalInstance = $modal.open({
                    templateUrl: 'partials/overwriteSavePopup.html',
                    controller: 'NewSavePopupController',
                    resolve: {
                        save: getCurrentSaveDetails,
                        onSaveNew: onSaveNew
                    }
                });
            } else {
                modalInstance = $modal.open({
                    templateUrl: 'partials/newSavePopup.html',
                    controller: 'NewSavePopupController',
                    resolve: {
                        save: getCurrentSaveDetails,
                        onSaveNew: onSaveNew
                    }
                });
            }
            modalInstance.result.then(function () {
            }, function () {});
        };

        $scope.search = function() {
            SearchService.updateSearchType($scope.searchType, $scope.addressFields);
            SearchService.updateTargetGroups($scope.visibleTargetGroups);
            SearchService.updateTerms($scope.terms);
            SearchService.updateKieli($scope.osoitekieli);
            SearchService.setSearchReady();
            $location.path("/results");
        };

        $scope.showSaveSearchPopup = function() {
            $log.info("Show saved searches popup.");
            var modalInstance = $modal.open({
                templateUrl: 'partials/savesPopup.html',
                controller: 'SavesPopupController',
                resolve: {
                    saves: function () {
                        return $scope.saves;
                    }
                }
            });
            modalInstance.result.then(function () {
                $log.info("Closed.");
                updateSaves();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
                updateSaves();
            });
        };
    }
]);
