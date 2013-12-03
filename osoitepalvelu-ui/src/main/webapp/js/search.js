
var SearchController = function($scope, i18n, $log, $modal, $location, $filter, SearchService,
                                SearchTypes, TargetGroups, Saves) {
    $scope.msg = i18n;

    $scope.clear = function() {
        $scope.saves = Saves;
        $scope.selectedSavedSearch = null;

        $scope.searchTypes = SearchTypes;
        $scope.searchType = null;

        $scope.targetGroups = TargetGroups;
        $scope.selectedTargetGroup = null;
        $scope.selectedTargetGroupTypes = [];
        $scope.visibleTargetGroups = [];

        $scope.terms = {
            foo: "bar"
            /// TODO
        };
    };

    $scope.clear();

    $scope.handleSaveSelected = function() {
        $log.info("Selected save: " + $scope.selectedSavedSearch);
        // TODO: REST call an update search terms
    };

    $scope.nonSelectedByField = function(arr, field) {
        return function(val) {
            if( !arr ) return true;
            return arr.indexOf(val[field]) == -1;
        }
    };
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
        return $scope.searchType != null;
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
        SearchService.updateTerms($scope.terms);
        $location.path("/results");
    };

    $scope.showSaveSearchPopup = function() {
        $log.info("Show save search popup.");
        var modalInstance = $modal.open({
            templateUrl: 'partials/savesPopup.html',
            controller: SavesPopup,
            resolve: {
                saves: function () {
                    return $scope.saves;
                }
            }
        });
        modalInstance.result.then(function () {
            $log.info("Closed.");
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };
}

var SavesPopup = function ($scope, $modalInstance, saves) {
    $scope.saves = saves;
    $scope.ok = function() {
        $modalInstance.close();
        // $modalInstance.dismiss('cancel');
    };
};

