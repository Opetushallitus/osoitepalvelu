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
var SearchController = function($scope, $log, $modal, $location, $filter, SearchService,
                                SearchTypes, TargetGroups, EmptyTerms,
                                FilterHelper, SavesService, OptionsService, LocalisationService) {
    $scope.msg = function( key, params ) {
        return LocalisationService.t(key, params);
    };

    var updateSaves = function() {
        SavesService.list(function(data) {
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
            receiverFields: $scope.receiverFields
        };
    };

    $scope.saves = [];
    $scope.selectedSavedSearch = null;
    updateSaves();

    $scope.updateTerms = function(updateOptions) {
        if( updateOptions === undefined ) {
            updateOptions = true;
        }
        $log.info("Update search terms");

        $scope.searchTypes = SearchTypes;
        $scope.searchType = SearchService.getSearchType();

        $scope.addressFields = SearchService.getAddressFields();

        $scope.receiverFields = SearchService.getReceiverFields();

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
            OptionsService.listTutkintotoimikuntas(function(data) { $scope.options.tutkintotoimikuntas = data; });
            OptionsService.listTutkintotoimikuntaRoolis(function(data) { $scope.options.tutkintotoimikuntaRoolis = data; });
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
        }

        $scope.terms = SearchService.getTerms();
        $log.info($scope.terms);
    };

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

    $scope.isShowTutkintotoimikuntaTerm = function() {
        return $scope.selectedTargetGroupTypes.indexOf('TUTKINTOTOIMIKUNNAT') != -1;
    };

    $scope.isShowTutkintotoimikuntaRooliTerm = function() {
        return $scope.selectedTargetGroupTypes.indexOf('TUTKINTOTOIMIKUNNAT') != -1;
    };

    $scope.isShowKoulutaTerm = function() {
        return $scope.selectedTargetGroupTypes.indexOf('KOULUTA_KAYTTAJAT') != -1;
    };

    $scope.isShowAipalTerm = function() {
        return $scope.selectedTargetGroupTypes.indexOf('AIPAL_KAYTTAJAT') != -1;
    };

    $scope.handleSaveSelected = function() {
        if( $scope.selectedSavedSearch ) {
            var selected = angular.copy($scope.selectedSavedSearch);
            $log.info("Selected save: " + selected);
            SavesService.getSearch($scope.selectedSavedSearch, function(save) {
                SearchService.updateSearchType(save.searchType, save.addressFields, save.receiverFields);
                SearchService.updateTargetGroups(save.targetGroups);
                SearchService.updateTerms(save.terms);
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
            if( newGroup.options.length == 1 ) {
                newGroup.options[0].selected=true;
            }
            $scope.visibleTargetGroups.push( newGroup );
            $scope.selectedTargetGroup = "";
        }
    };
    $scope.removeTargetGroup = function(i) {
        var arr = [];
        angular.forEach($scope.selectedTargetGroupTypes, function(v,index) {
            if( index != i && v ) arr.push(v);
        });
        $scope.selectedTargetGroupTypes = arr;
        arr = [];
        angular.forEach( $scope.visibleTargetGroups, function (v, index) {
            if( index != i ) arr.push( angular.copy(v) );
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

    $scope.isSearchAllowed = function() {
        return $scope.isTermsShown() && $scope.visibleTargetGroups.length > 0;
    };

    $scope.saveSearch = function() {
        $log.info("Show save popup.");

        var modalInstance = null;
        if( $scope.selectedSavedSearch ) {
            modalInstance = $modal.open({
                templateUrl: 'partials/overwriteSavePopup.html',
                controller: NewSavePopupController,
                resolve: {save: getCurrentSaveDetails}
            });
        } else {

            modalInstance = $modal.open({
                templateUrl: 'partials/newSavePopup.html',
                controller: NewSavePopupController,
                resolve: {save: getCurrentSaveDetails}
            });
        }
        modalInstance.result.then(function () {
            updateSaves();
        }, function () {});
    };

    $scope.search = function() {
        SearchService.updateSearchType($scope.searchType, $scope.addressFields, $scope.receiverFields);
        SearchService.updateTargetGroups($scope.visibleTargetGroups);
        SearchService.updateTerms($scope.terms);
        SearchService.setSearchReady();
        $location.path("/results");
    };

    $scope.showSaveSearchPopup = function() {
        $log.info("Show saved searches popup.");
        var modalInstance = $modal.open({
            templateUrl: 'partials/savesPopup.html',
            controller: SavesPopupController,
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

SearchController.$inject = ["$scope", "$log", "$modal", "$location", "$filter", "SearchService",
                     "SearchTypes", "TargetGroups", "EmptyTerms", "FilterHelper", "SavesService",
                     "OptionsService", "LocalisationService"];
