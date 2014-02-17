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

            angular.forEach(data, function(v) {
                v.yhteyshenkilonNimi = v.etunimi + " " + v.sukunimi;
                v.postiosoite = v.osoite +(v.osoite ? "\n":"") + v.extraRivi + (v.extraRivi ? "\n":"")
                        + v.postinumero + " " + v.postitoimipaikka;
                v.katuPostinumero = v.osoite +(v.osoite ? ", ":"")
                        + v.postinumero + " " + v.postitoimipaikka;
                v.plPostinumero = v.postilokero +(v.postilokero ? ", ":"")
                        + v.postinumero + " " + v.postitoimipaikka;
            });
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
        SearchService.addDeleted( ArrayHelper.extract($scope.resultGridOptions.selectedItems, "organisaatioOid") );
        updateResults();
    };

    $scope.downloadExcel = function() {
        SearchService.downloadExcel();
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
    var columns = [];
    var searchType = SearchService.getSearchType(),
        isContact = searchType == 'CONTACT',
        isEmail = searchType == 'EMAIL',
        isLetter = !isContact && !isEmail;
    $log.info("SHOWING GRID.");
    $log.info(searchType);
    var colOverrides = {
        'organisaatioOid': {width: '10%', visible: isContact && SearchService.hasAddressField('ORGANIAATIO_TUNNISTE') }
    };
    if( isEmail || (isContact && SearchService.hasAddressField('ORGANIAATIO_NIMI')) ) {
        columns.push('nimi');
    }
    columns.push('organisaatioOid');
    if( isEmail || (isContact && SearchService.hasAddressField('YHTEYSHENKILO')) ) {
        columns.push('yhteyshenkilonNimi');
    }
    if( isEmail || (isContact && SearchService.hasAddressField('YHTEYSHENKILO')) ) {
        columns.push('email');
    }
    if( isLetter || (isContact && SearchService.hasAddressField('POSTIOSOITE')) ) {
        columns.push('postiosoite');
    }
    if( isContact && SearchService.hasAddressField('KATU_POSTINUMERO') ) {
        columns.push('katuPostinumero');
    }
    if( isContact && SearchService.hasAddressField('PL_POSTINUMERO') ) {
        columns.push('plPostinumero');
    }
    if( isContact && SearchService.hasAddressField('PUHELINNUMERO') ) {
        columns.push('puhelinnumero');
    }
    if( isContact && SearchService.hasAddressField('FAXINUMERO') ) {
        columns.push('faksinumero');
    }
    if( isContact && SearchService.hasAddressField('INTERNET_OSOITE') ) {
        columns.push('wwwOsoite');
    }
    if( isContact && SearchService.hasAddressField('VIRANOMAISTIEDOTUS_EMAIL') ) {
        columns.push('virnaomaistiedotuksenEmail');
    }
    if( isContact && SearchService.hasAddressField('KRIISITIEDOTUKSEN_EMAIL') ) {
        columns.push('koulutusneuvonnanEmail');
    }
    if( isContact && SearchService.hasAddressField('KOULUTUSNEUVONNAN_EMAIL') ) {
        columns.push('kriisitiedotuksenEmail');
    }
    if( isContact && SearchService.hasAddressField('ORGANISAATIO_SIJAINTIKUNTA') ) {
        columns.push('kotikunta');
    }

    angular.forEach(columns, function(c) {
        $scope.resultGridOptions.columnDefs.push( angular.extend( {
            field: c,
            displayName: i18n['column_'+c]
        }, (colOverrides[c] || {}) ) );
    });
};
