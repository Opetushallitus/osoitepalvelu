/**
 * Created by ratamaa on 12/9/13.
 */
var ResultsController = function($scope, i18n, $log, $location, $filter, $timeout, SearchService, EmailService, ArrayHelper) {
    $scope.msg = i18n;
    $scope.results = [];
    $scope.searchDone = false;
    $scope.customColumnDefs = [];

    var columnVisibilityMapping = {
        'nimi':                         'organisaationNimiIncluded',
        'organisaatioTunniste':         'organisaatiotunnisteIncluded',
        'yhteyshenkilonNimi':           'yhteyshenkiloIncluded',
        'henkiloEmail':                 'yhteyshenkiloEmailIncluded',
        'postiosoite':                  'positosoiteIncluded',
        'katuPostinumero':              ['katuosoiteIncluded', 'postinumeroIncluded'],
        'plPostinumero':                ['pLIncluded', 'postinumeroIncluded'],
        'puhelinnumero':                'puhelinnumeroIncluded',
        'faksinumero':                  'faksinumeroIncluded',
        'wwwOsoite':                    'wwwOsoiteIncluded',
        'viranomaistiedotuksenEmail':   'viranomaistiedotuksenSahkopostiosoiteIncluded',
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

    var updateResults = function() {
        SearchService.search(function(data) {
            $log.info("Got data as results.");
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
        SearchService.addDeleted( ArrayHelper.extract($scope.resultGridOptions.selectedItems, ["oid", "oidTyyppi"]) );
        updateResults();
    };

    $scope.downloadExcel = function() {
        SearchService.downloadExcel();
    };

    $scope.sendEmail = function() {
        EmailService.sendEmail($scope.results);
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
        'organisaatioTunniste': {} /*..*/
    };

    angular.forEach(columns, function(c) {
        $scope.customColumnDefs.push( angular.extend( {
            field: c,
            displayName: i18n['column_'+c]
        }, (colOverrides[c] || {}) ) );
    });
};
