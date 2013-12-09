/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('SearchService', function($log, $filter, FilterHelper, AddressFields, EmptyTerms, SearchResultProvider) {
    var _terms = angular.copy(EmptyTerms),
        _targetGroups = [],
        _searchType = null,
        _addressFields = angular.copy(AddressFields),
        _deletedIds = [];

    this.getTerms = function() {return _terms;};
    this.getTargetGroups = function() {return _targetGroups;};
    this.getSearchType = function() {return _searchType;};
    this.getAddressFields = function() {return _addressFields;};

    this.addDeleted = function(ids) {
        angular.forEach( $filter('filter')(ids, FilterHelper.notInArray(_deletedIds) ), function(id) { if(id) _deletedIds.push(id);});
    };

    this.getDeletedIds = function() {
        return _deletedIds;
    };

    this.clear = function() {
        _terms = angular.copy(EmptyTerms);
        _targetGroups = [];
        _searchType = null;
        _addressFields = angular.copy(AddressFields);
        _deletedIds = [];
    };

    this.updateSearchType = function(type, addressFields) {
        _searchType = type;
        _addressFields = angular.copy(addressFields);
    };

    this.updateTargetGroups = function(targetGroups) {
        _targetGroups = angular.copy(targetGroups);
    };

    this.updateTerms = function(terms) {
        _terms = angular.copy(terms);
    };

    this.search = function(success) {
        $log.info(_searchType);
        $log.info(_addressFields);
        $log.info(_targetGroups);
        $log.info(_terms);
        SearchResultProvider({
            type: _searchType,
            terms: _terms,
            addressFields: _addressFields,
            targetGroups: _targetGroups,
            callback: function(data) {
                success( $filter('filter')(data, FilterHelper.extractedFieldNotInArray(_deletedIds, "identifier") )  );
            }
        });
    };
});
