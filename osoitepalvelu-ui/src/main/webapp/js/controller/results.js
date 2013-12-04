/**
 * Created by ratamaa on 12/3/13.
 */
var ResultsController = function($scope, i18n, $log, $location, SearchService) {
    $scope.msg = i18n;
    $scope.results = [];
    var updateResults = function() {
        SearchService.search(function(data) {
            $scope.results = data;
        });
    };
    updateResults();

    $scope.hasResults = function() {
        return $scope.results.length > 0;
    };
}