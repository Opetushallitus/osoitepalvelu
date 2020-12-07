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
 * Created by ratamaa on 12/9/13.
 */
OsoiteKoostepalvelu.controller('ResultsController', ["$scope", "$log", "$location", "$filter", "$timeout",
    "SearchService", "EmailService", "ArrayHelper", "LocalisationService",
    function($scope, $log, $location, $filter, $timeout,
                SearchService, EmailService, ArrayHelper, LocalisationService) {
    var msg = function( key, params ) {
        return LocalisationService.t(key, params);
    };
    $scope.msg = msg;
    $scope.results = [];
    $scope.searchDone = false;
    $scope.customColumnDefs = [];
    $scope.presentation = null;
    $scope.sourceRegisters = null;

    var columnVisibilityMapping = {
        'nimi':                         'organisaationNimiIncluded',
        'organisaatioTunniste':         'organisaatiotunnisteIncluded',
        'organisaatioOid':              'organisaatioOidIncluded',
        'ytunnus':                      'ytunnusIncluded',
        'yritysmuoto':                  'yritysmuotoIncluded',
        'opetuskieli':                  'opetuskieliIncluded',
        'yhteyshenkilonNimi':           'yhteyshenkiloIncluded',
        'henkiloEmail':                 'yhteyshenkiloEmailIncluded',
        'emailOsoite':                  'organisaatioEmailIncluded',
        'postiosoite':                  'positosoiteIncluded',
        'postiosoitePostinumero':       'positosoiteIncluded',
        'postiosoitePostitoimipaikka':  'positosoiteIncluded',
        'puhelinnumero':                'puhelinnumeroIncluded',
        'wwwOsoite':                    'wwwOsoiteIncluded',
        'viranomaistiedotuksenEmail':   'viranomaistiedotuksenSahkopostiosoiteIncluded',
        'koulutusneuvonnanEmail':       'koulutusneuvonnanSahkopostiosoiteIncluded',
        'kriisitiedotuksenEmail':       'kriisitiedotuksenSahkopostiosoiteIncluded',
        'kotikunta':                    'organisaationSijaintikuntaIncluded',
        'varhaiskasvatuksenYhteyshenkilo': 'varhaiskasvatuksenYhteyshenkiloIncluded',
        'varhaiskasvatuksenEmail':      'varhaiskasvatuksenEmailIncluded',
        'koskiYhdyshenkilo':            'koskiYhdyshenkiloIncluded',
        'moveYhteyshenkilo':            'moveYhteyshenkiloIncluded',
    };
    var columns = [];
    for( columnName in columnVisibilityMapping ) {
        columns.push(columnName);
    }

    var isColumnVisible = function(field, presentation) {
        return ArrayHelper.forAll( ArrayHelper.ensureArray(columnVisibilityMapping[field]),
            function(visibleDetail) {
                return presentation[ visibleDetail ];
            }
        );
    };

    var updateResults = function(update) {
        var f = update ? SearchService.search : SearchService.results;
        f(function(data) {
            $log.info("Got data as results.");
            $log.info(data);
            $scope.presentation = data.presentation;
            $scope.sourceRegisters = data.sourceRegisters;
            angular.forEach($scope.customColumnDefs, function(colDef) {
                colDef.visible = isColumnVisible(colDef.field, data.presentation);
                //$log.info("Column " + colDef.field + " visibility="+colDef.visible);
            });
            $scope.results = data.rows;
            $scope.searchDone = true;
            $scope.$broadcast('resultsloaded');
        }, function() {
            $location.path("/");
        });
    };
    $timeout(function() {
        updateResults(true);
    }, 1, false);

    $scope.back = function() {
        $location.path("/");
    };

    $scope.deleteSelected = function() {
        $log.info($scope.resultGridOptions.selectedItems);
        SearchService.addDeleted( ArrayHelper.extract($scope.resultGridOptions.selectedItems, ["oid", "oidTyyppi", "rivinumero"]) );
        updateResults();
    };

    $scope.downloadExcel = function() {
        SearchService.downloadExcel(function(downloadUrl) {
            window.location = downloadUrl;
        });
    };

    $scope.sendEmail = function() {
        EmailService.sendEmail($scope.results, $scope.presentation, $scope.sourceRegisters);
    };

    $scope.resultGridOptions = {
        data: 'results',
        enablePaging: true,
        selectedItems: [],
        columnDefs: "customColumnDefs",
        enableRowSelection: true,
        enableColumnResize: true,
        enableCellEdit: false,
        showSelectionCheckbox: true,
        afterSelectionChange: function( rowItem, event ) {
            $log.info(rowItem);
        }
    };
    $log.info("SHOWING GRID.");
    var colOverrides = {
        'organisaatioTunniste': {} /*..*/
    };

    angular.forEach(columns, function(c) {
        $scope.customColumnDefs.push( angular.extend( {
            field: c,
            displayName: msg('column_'+c)
        }, (colOverrides[c] || {}) ) );
    });

    if( !SearchService.isSearchReady() ) {
        $location.path("/");
        return;
    }
}]);


