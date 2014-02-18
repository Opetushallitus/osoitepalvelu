/**
 * Created by ratamaa on 12/9/13.
 */
var ResultsController = function($scope, i18n, $log, $location, $filter, $timeout, SearchService, ArrayHelper) {
    $scope.msg = i18n;
    $scope.results = [];
    $scope.searchDone = false;
    $scope.customColumnDefs = [];

    var columnVisibilityMapping = {
        'nimi':                         'organisaationNimiIncluded',
        'organisaatioOid':              [],//'organisaatiotunnisteIncluded',
        'yhteyshenkilonNimi':           'yhteyshenkiloIncluded',
        'email':                        'yhteyshenkiloEmailIncluded',
        'postiosoite':                  'positosoiteIncluded',
        'katuPostinumero':              ['katuosoiteIncluded', 'postinumeroIncluded'],
        'plPostinumero':                ['pLIncluded', 'postinumeroIncluded'],
        'puhelinnumero':                'puhelinnumeroIncluded',
        'faksinumero':                  'faksinumeroIncluded',
        'wwwOsoite':                    'wwwOsoiteIncluded',
        'virnaomaistiedotuksenEmail':   'viranomaistiedotuksenSahkopostiosoiteIncluded',
        'koulutusneuvonnanEmail':       'koulutusneuvonnanSahkopostiosoiteIncluded',
        'kriisitiedotuksenEmail':       'kriisitiedotuksenSahkopostiosoiteIncluded',
        'kotikunta':                    'organisaationSijaintikuntaIncluded'
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

    var addAggrationColumns = function(v) {
        v.yhteyshenkilonNimi = v.etunimi + " " + v.sukunimi;
        v.postiosoite = v.osoite +(v.osoite ? "\n":"") + v.extraRivi + (v.extraRivi ? "\n":"")
                + v.postinumero + " " + v.postitoimipaikka;
        v.katuPostinumero = v.osoite +(v.osoite ? ", ":"")
                + v.postinumero + " " + v.postitoimipaikka;
        v.plPostinumero = v.postilokero +(v.postilokero ? ", ":"")
                + v.postinumero + " " + v.postitoimipaikka;
    };

    var updateResults = function() {
        SearchService.search(function(data) {
            $log.info("Got data as results.");
            angular.forEach(data.rows, function(row) {
                addAggrationColumns(row);
            });
            $log.info(data);
            angular.forEach($scope.customColumnDefs, function(colDef) {
                colDef.visible = isColumnVisible(colDef.field, data.presentation);
                //$log.info("Column " + colDef.field + " visibility="+colDef.visible);
            });
            $scope.results = data.rows;
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
        columnDefs: "customColumnDefs",
        enableRowSelection: true,
        enableColumnResize: true,
        enableCellEdit: false,
        showSelectionCheckbox: true,
        afterSelectionChange: function( rowItem, event ) {
            $log.info(rowItem); // TODO
        }
    };
    $log.info("SHOWING GRID.");
    var colOverrides = {
        'organisaatioOid': {width: '20px'}
    };

    angular.forEach(columns, function(c) {
        $scope.customColumnDefs.push( angular.extend( {
            field: c,
            displayName: i18n['column_'+c]
        }, (colOverrides[c] || {}) ) );
    });
};
