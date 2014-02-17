/**
 * Created by ratamaa on 12/4/13.
 */
var OsoiteKoostepalvelu = angular.module('OsoiteKoostepalvelu',
    ['ngRoute', 'Helpers', 'I18n', 'ui.bootstrap', 'ui.select2', 'ngGrid']);

OsoiteKoostepalvelu.factory('SearchResultProvider', function($http) {
    return function(details) {
        $http.post('api/search/list.json', details.searchTerms)
            .success(details.callback)
            .error(details.errorCallback);
    }
});
