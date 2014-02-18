/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('SearchService', function($log, $filter, $http, $location, FilterHelper,
                                                      AddressFields, ReceiverTypes, EmptyTerms,
                                                      SearchResultProvider, SaveConverter) {
    var _terms = angular.copy(EmptyTerms),
        _targetGroups = [],
        _searchType = null,
        _addressFields = angular.copy(AddressFields),
        _receiverFields = angular.copy(ReceiverTypes),
        _deletedIds = [],
        _selectedSearch = null;

    this.getTerms = function() {return _terms;};
    this.getTargetGroups = function() {return _targetGroups;};
    this.getSearchType = function() {return _searchType;};
    this.getAddressFields = function() {return _addressFields;};
    this.hasAddressField = function(fieldType) {
        for(var i in _addressFields) {
            var field = _addressFields[i];
            if( field.type == fieldType ) {
                return field.selected;
            }
        }
        return false;
    };
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
            searchTerms: SaveConverter.toDomain({
                searchType: _searchType,
                terms: _terms,
                addressFields: _addressFields,
                receiverFields: _receiverFields,
                targetGroups: _targetGroups
            }),
            nonIncludedOrganisaatioOids: _deletedIds,
            callback: function(data) {
                success( {
                    presentation: data.presentation,
                    rows: $filter('filter')(data.rows, FilterHelper.extractedFieldNotInArray(_deletedIds, "organisaatioOid") )
                } );
            },
            errorCallback: function(e) {
                $log.error(e);
            }
        });
    };

    this.downloadExcel = function() {
        $log.info(_searchType);
        $log.info(_addressFields);
        $log.info(_targetGroups);
        $log.info(_terms);
        $log.info(_deletedIds);
        $http.post('api/search/prepare.excel.do', {
                  searchTerms: SaveConverter.toDomain({
                      searchType: _searchType,
                      terms: _terms,
                      addressFields: _addressFields,
                      receiverFields: _receiverFields,
                      targetGroups: _targetGroups
                  }),
                  nonIncludedOrganisaatioOids: _deletedIds
            })
            .success(function(key) {
                window.location = 'api/search/excel.do?downloadId='+key;
            })
            .error(function(e) {
               $log.error(e);
            });
    };

    this.updateSelectedSearch = function(search) {
        _selectedSearch = search;
    };

    this.getSelectedSearch = function() {
        return _selectedSearch;
    };
});
