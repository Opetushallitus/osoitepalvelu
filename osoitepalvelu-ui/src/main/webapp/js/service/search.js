/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('SearchService', function($log, $filter, FilterHelper,
                                                      AddressFields, ReceiverTypes, EmptyTerms,
                                                      SearchResultProvider) {
    var _terms = angular.copy(EmptyTerms),
        _targetGroups = [],
        _searchType = null,
        _addressFields = angular.copy(AddressFields),
        _receiverFields = angular.copy(ReceiverTypes),
        _deletedIds = [];

    this.getTerms = function() {return _terms;};
    this.getTargetGroups = function() {return _targetGroups;};
    this.getSearchType = function() {return _searchType;};
    this.getAddressFields = function() {return _addressFields;};
    this.getReceiverFields = function() {return _receiverFields;};

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

    this.updateSearchType = function(type, addressFields, receiverFields) {
        $log.info("Update search type: " + type);
        $log.info(addressFields);
        $log.info(receiverFields);
        _searchType = type;
        _addressFields = angular.copy(addressFields);
        _receiverFields = angular.copy(receiverFields);
    };

    this.updateTargetGroups = function(targetGroups) {
        $log.info("Update target groups");
        $log.info(targetGroups);
        _targetGroups = angular.copy(targetGroups);
    };

    this.updateTerms = function(terms) {
        $log.info("Update terms");
        $log.info(terms);
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
            receiverFields: _receiverFields,
            targetGroups: _targetGroups,
            callback: function(data) {
                success( $filter('filter')(data, FilterHelper.extractedFieldNotInArray(_deletedIds, "identifier") )  );
            }
        });
    };
});
