/**
 * Created by ratamaa on 12/9/13.
 */
var ResultsController = function($scope, i18n, $log, $location, $filter, $timeout, SearchService, ArrayHelper) {
    $scope.msg = i18n;
    $scope.results = [];
    $scope.searchDone = false;

    var updateResults = function() {
        SearchService.search(function(data) {
            $log.info("Got data as results.");
            $log.info(data);
            $scope.results = data;
            $scope.searchDone = true;
            $scope.$broadcast('resultsloaded');
        });
    };
    $timeout(function() {
        updateResults();
    }, 0, false);

    $scope.back = function() {
        $location.path("/");
    };

    $scope.deleteSelected = function() {
        $log.info($scope.resultGridOptions.selectedItems);
        SearchService.addDeleted( ArrayHelper.extract($scope.resultGridOptions.selectedItems, "identifier") );
        updateResults();
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
        'identifier': {width: '10%'}
    };
    angular.forEach([
        'identifier',
        'targetGroup'
    ], function(c) {
        $scope.resultGridOptions.columnDefs.push( angular.extend( {
            field: c,
            displayName: i18n['column_'+c]
        }, (colOverrides[c] || {}) ) );
    });
};
