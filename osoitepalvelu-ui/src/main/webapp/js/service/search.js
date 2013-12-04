/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('SearchService', function($log) {
    var _terms = {},
        _targetGroups = [],
        _searchType = null,
        _addressFields = [];

    this.updateSearchType = function(type, addressFields) {
        _searchType = type;
        _addressFields = addressFields;
    };

    this.updateTargetGroups = function(targetGroups) {
        _targetGroups = targetGroups;
    };

    this.updateTerms = function(terms) {
        _terms = terms;
    };

    this.search = function(success) {
        $log.info(_searchType);
        $log.info(_addressFields);
        $log.info(_targetGroups);
        $log.info(_terms);
        /// TODO:
        success( [] );
    };
});
