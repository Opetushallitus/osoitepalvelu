/**
 * Created by ratamaa on 12/3/13.
 */
var SearchController = function($scope, i18n, $log, $modal, $location, $filter, SearchService,
                                SearchTypes, AddressFields, TargetGroups,
                                FilterHelper, SavesService, OptionsService) {
    $scope.msg = i18n;

    var updateSaves = function() {
        SavesService.list(function(data) {
            $scope.saves = data;
        });
    };

    var getCurrentSaveDetails = function() {
        return {
            terms: $scope.terms,
            targetGroup: $scope.visibleTargetGroups,
            type: $scope.searchType,
            addressFields: $scope.addressFields
        };
    };

    $scope.updateTerms = function() {
        $log.info("CLEAR");
        $scope.saves = [];
        updateSaves();

        $scope.selectedSavedSearch = null;

        $scope.searchTypes = SearchTypes;
        $scope.searchType = SearchService.getSearchType();

        $scope.addressFields = SearchService.getAddressFields();

        $scope.targetGroups = TargetGroups;
        $scope.selectedTargetGroup = null;
        $scope.visibleTargetGroups = SearchService.getTargetGroups();
        $scope.selectedTargetGroupTypes = [];
        angular.forEach($scope.visibleTargetGroups, function(v) {
            $scope.selectedTargetGroupTypes.push(v.type);
        } );
        $scope.showExtraTerms = false;

        $scope.options = {
            avis: [],
            maakuntas : [],
            kuntas : [],
            oppilaitostyyppis: [],
            omistajatyyppis: [],
            vuosiluokkas: [],
            koultuksenjarjestajas: []
        };
        OptionsService.listAvis(function(data) { $scope.options.avis = data; });
        OptionsService.listMaakuntas(function(data) { $scope.options.maakuntas = data; });
        OptionsService.listKuntas(function(data) { $scope.options.kuntas = data; });
        OptionsService.listOppilaitostyyppis(function(data) { $scope.options.oppilaitostyyppis = data; });
        OptionsService.listOmistajatyyppis(function(data) { $scope.options.omistajatyyppis = data; });
        OptionsService.listVuosiluokkas(function(data) { $scope.options.vuosiluokkas = data; });
        OptionsService.listKoultuksenjarjestajas(function(data) { $scope.options.koultuksenjarjestajas = data; });

        $scope.terms = SearchService.getTerms();
        $log.info($scope.terms);
    };

    $scope.updateTerms();

    $scope.clear = function() {
        SearchService.clear();
        $scope.updateTerms();
    }

    $scope.toggleShowMore = function() {
        $scope.showExtraTerms = !$scope.showExtraTerms;
    };

    $scope.handleSaveSelected = function() {
        $log.info("Selected save: " + $scope.selectedSavedSearch);
        // TODO: REST call an update search terms
    };

    $scope.nonSelectedByField = FilterHelper.extractedFieldNotInArray;

    $scope.handleTargetGroupSelected = function() {
        $log.info("Target gorup selection changed to: "+$scope.selectedTargetGroup);
        if( $scope.selectedTargetGroup ) {
            $scope.selectedTargetGroupTypes.push($scope.selectedTargetGroup);
            $scope.visibleTargetGroups.push( angular.copy(
                $filter('filter')($scope.targetGroups, {type: $scope.selectedTargetGroup})[0]) );
        }
    };
    $scope.removeTargetGroup = function(i) {
        var arr = [];
        angular.forEach($scope.selectedTargetGroupTypes, function(v,index) {
            if( index != i ) arr.push(v);
        });
        $scope.selectedTargetGroupTypes = arr;
        arr = [];
        angular.forEach( $scope.visibleTargetGroups, function (v, index) {
            if( index != i ) arr.push( angular.copy(v) );
        } );
        $scope.visibleTargetGroups = arr;
        $scope.selectedTargetGroup = null;
    };

    $scope.isTermsShown = function() {
        return !!$scope.searchType;
    };

    $scope.isShowTargetGroup = function() {
        return $scope.isTermsShown();
    };

    $scope.isSearchAllowed = function() {
        return $scope.isTermsShown();
    };

    $scope.saveSearch = function() {
        $log.info("Show new save search popup.");
        var modalInstance = $modal.open({
            templateUrl: 'partials/newSavePopup.html',
            controller: NewSavePopupController,
            resolve: {
                save: getCurrentSaveDetails
            }
        });
        modalInstance.result.then(function () {
            updateSaves();
        }, function () {
        });
    };

    $scope.search = function() {
        SearchService.updateSearchType($scope.searchType, $scope.addressFields);
        SearchService.updateTargetGroups($scope.visibleTargetGroups);
        SearchService.updateTerms($scope.terms);
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

