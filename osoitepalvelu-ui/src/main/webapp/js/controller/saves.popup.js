/**
 * Created by ratamaa on 12/4/13.
 */
var SavesPopupController = function ($scope, $modalInstance, saves, i18n, SavesService) {
    $scope.msg = i18n;

    $scope.saves = saves;

    $scope.close = function() {
        $modalInstance.close();
        // $modalInstance.dismiss('cancel');
    };

    $scope.deleteSave = function(id) {
        SavesService.deleteSave(id, function() {
            SavesService.list(function(data) {
                $scope.saves = data;
            });
        })
    };
};

