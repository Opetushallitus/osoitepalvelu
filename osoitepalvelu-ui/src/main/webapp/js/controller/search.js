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

    var updateResults = function() {
        SearchService.search(function(data) {
            $scope.results = data;
            $scope.searchDone = true;
        });
    };

    $scope.clear = function() {
        $scope.saves = [];
        updateSaves();

        $scope.searchDone = false;
        $scope.selectedSavedSearch = null;

        $scope.searchTypes = SearchTypes;
        $scope.searchType = null;

        $scope.addressFields = angular.copy(AddressFields);

        $scope.targetGroups = TargetGroups;
        $scope.selectedTargetGroup = null;
        $scope.selectedTargetGroupTypes = [];
        $scope.visibleTargetGroups = [];
        $scope.showExtraTerms = false;
        $scope.results = [];

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

        $scope.terms = {
            avis: [],
            maakuntas : [],
            kuntas : [],
            oppilaitostyyppis: [],
            omistajatyyppis: [],
            vuosiluokkas: [],
            koultuksenjarjestajas: []
        };
    };

    $scope.clear();

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
        /// TODO
    };

    $scope.search = function() {
        SearchService.updateSearchType($scope.searchType, $scope.addressFields);
        SearchService.updateTargetGroups($scope.visibleTargetGroups);
        SearchService.updateTerms($scope.terms);
        updateResults();
        //$location.path("/results");
    };

    $scope.showSaveSearchPopup = function() {
        $log.info("Show save search popup.");
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

    $scope.resultGridOptions = {
        data: 'results',
        enablePaging: true,
        selectedItems: [],
        columnDefs: [],
        enableRowSelection: true,
        enableColumnResize: true,
        enableCellEdit: false,
        showSelectionCheckbox: true,
        afterSelectionChange: function( rowItem, event ) {
            $log.info(rowItem); // TODO
        }
    };
    var colOverrides = {
    }
    angular.forEach([
            'identifier',
            'targetGroup'
    ], function(c) {
       $scope.resultGridOptions.columnDefs.push( angular.extend( {
           field: c,
           displayName: i18n['column_'+c]
       }, (colOverrides[c] || {}) ) );
    });
}

