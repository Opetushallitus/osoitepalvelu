/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('SearchService', ["$log", "$filter", "$http", "$location", "FilterHelper",
                             "AddressFields", "EmptyTerms",
                             "SearchResultProvider", "SaveConverter",
                             "LocalisationService", "commonErrorHandler",
        function($log, $filter, $http, $location, FilterHelper,
                      AddressFields, EmptyTerms,
                      SearchResultProvider, SaveConverter,
                      LocalisationService, commonErrorHandler) {
    var _terms = angular.copy(EmptyTerms),
        _targetGroups = [],
        _searchType = null,
        _addressFields = angular.copy(AddressFields),
        _deletedIds = [],
        _selectedSearch = null,
        _resultData = null,
        _searchReady=false,
        _kieli=LocalisationService.getLocale();

    var _osoite = function(osoite) {
        if (!osoite) {
            return "";
        }
        return (osoite.osoite ? osoite.osoite : "") +(osoite.extraRivi ? "\n":"")
               + ( osoite.extraRivi ? osoite.extraRivi : "" );
    };

    var _addColumnData = function(v) {
        if( v.henkiloOid ) {
            v.oidTyyppi = 'henkilo';
            v.oid = v.henkiloOid;
        } else {
            v.oidTyyppi = 'organisaatio';
            v.oid = v.organisaatioOid;
        }
        v.organisaatioTunniste = v.oppilaitosKoodi;
        v.yhteyshenkilonNimi = v.yhteystietoNimi;
        v.postiosoitePostinumero = v.postiosoite ? (v.postiosoite.postinumero || "") : "";
        v.postiosoitePostitoimipaikka = v.postiosoite ? (v.postiosoite.postitoimipaikka || "") : "";
        v.postiosoite = _osoite(v.postiosoite);
        v.kayntiosoitePostinumero = v.kayntiosoite ? (v.kayntiosoite.postinumero || "") : "";
        v.kayntiosoitePostitoimipaikka = v.kayntiosoite ? (v.kayntiosoite.postitoimipaikka || "") : "";
        v.kayntiosoite = _osoite(v.kayntiosoite);
    };

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

    /**
     * @param oidAndTyyppis {
     *      oid: <string>,
     *      oidTyyppi: 'organisaatio' | 'henkilo'
     * }
     */
    this.addDeleted = function(oidAndTyyppis) {
        angular.forEach( $filter('filter')(oidAndTyyppis, FilterHelper.notInArray(_deletedIds) ),
            function(oidAndTyyppi) {
                if(oidAndTyyppi) _deletedIds.push(oidAndTyyppi);
            }
        );
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
        _kieli=LocalisationService.getLocale();
    };

    this.updateSearchType = function(type, addressFields) {
        $log.info("Update search type: " + type);
        $log.info(addressFields);
        _searchType = type;
        _addressFields = angular.copy(addressFields);
    };

    this.updateTargetGroups = function(targetGroups) {
        $log.info("Update target groups");
        $log.info(targetGroups);
        _targetGroups = angular.copy(targetGroups);
        angular.forEach(_targetGroups, function(tg) {
            delete(tg.hideOptions);
            delete(tg._uiSelectChoiceDisabled);
            delete(tg.options.hide);
        });
    };

    this.updateTerms = function(terms) {
        $log.info("Update terms");
        $log.info(terms);
        _terms = angular.copy(terms);
    };

    this.getKieli = function() {
        return _kieli;
    };

    this.updateKieli = function(kieli) {
        var kieliToUse = kieli || LocalisationService.getLocale();
        $log.info("Kieli: " + kieli + " -> " + kieliToUse);
        _kieli = kieliToUse;
    };

    var _transformResults = function(data, success) {
        angular.forEach(data.rows, function(row) {
            _addColumnData(row);
        });
        var filteredRows = $filter('filter')(data.rows,
                FilterHelper.extractedFieldNotInArray(_deletedIds, ["oid", "oidTyyppi", "rivinumero"]) );
        success( {
            presentation: data.presentation,
            rows: filteredRows,
            sourceRegisters: data.sourceRegisters
        } );
    };

    var _performSearch = function(success, error) {
        $log.info(_searchType);
        $log.info(_addressFields);
        $log.info(_targetGroups);
        $log.info(_terms);
        SearchResultProvider({
            data: {
                searchTerms: SaveConverter.toDomain({
                    searchType: _searchType,
                    terms: _terms,
                    addressFields: _addressFields,
                    targetGroups: _targetGroups
                }),
                nonIncludedOrganisaatioOids: _deletedIds
            },
            lang: _kieli,
            callback: function(data) {
                _transformResults(data, success);
                _resultData = data;
                _deletedIds = [];
            },
            errorCallback: function(data, status, headers, config) {
                commonErrorHandler(data, status, headers, config);
                if (error) {
                    error(data, status, headers, config);
                }
            }
        });
    };

    this.results = function(success, error) {
        if(_resultData===null) {
            _performSearch(success, error);
        } else {
            var filteredRows = $filter('filter')(_resultData.rows,
                    FilterHelper.extractedFieldNotInArray(_deletedIds, ["oid", "oidTyyppi", "rivinumero"]) );
            success( {
                presentation: _resultData.presentation,
                rows: filteredRows
            } );
        }
    };

    this.search = function(success, error) {
        _deletedIds = [];
        _performSearch(success, error);
    };

    this.downloadExcel = function(success, error) {
        $log.info(_searchType);
        $log.info(_addressFields);
        $log.info(_targetGroups);
        $log.info(_terms);
        $log.info(_deletedIds);
        $http.post(window.urls().noEncode().url('osoitepalvelu-service.search', 'prepare.excel.do'), {
                  searchTerms: SaveConverter.toDomain({
                      searchType: _searchType,
                      terms: _terms,
                      addressFields: _addressFields,
                      targetGroups: _targetGroups
                  }),
                  nonIncludedOrganisaatioOids: _deletedIds
            } )
        .success(function(key) {
            success(window.urls().noEncode().url('osoitepalvelu-service.search',
                'excel.do?downloadId='+key+'&lang='+_kieli));
        })
        .error( error || commonErrorHandler );
    };

    this.updateSelectedSearch = function(search) {
        _selectedSearch = search;
    };

    this.getSelectedSearch = function() {
        return _selectedSearch;
    };

    this.setSearchReady = function() {
        _searchReady = true;
    };

    this.isSearchReady = function() {
        return _searchReady;
    };
}]);
