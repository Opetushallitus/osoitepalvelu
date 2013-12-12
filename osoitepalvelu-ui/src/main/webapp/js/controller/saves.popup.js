/**
 * Created by ratamaa on 12/4/13.
 */
var SavesPopupController = function ($scope, $modalInstance, $modal, $filter, saves, i18n, SavesService) {
    $scope.msg = i18n;

    $scope.saves = saves;

    $scope.close = function() {
        $modalInstance.close();
        // $modalInstance.dismiss('cancel');
    };

    $scope.deleteSave = function(id) {
        var modalInstance = $modal.open({
            templateUrl: 'partials/confirmPopup.html',
            controller: ConfirmPopupController,
            resolve: {options: function() {
                return {
                    title: i18n.format('saves_popup_delete_confirm_title', $filter('filter')($scope.saves, {id: id})[0].name ),
                    yesText: i18n.saves_popup_delete_confirm_yes,
                    noText: i18n.saves_popup_delete_confirm_no,
                    yesClass: 'btn-danger'
                };
            }}
        });
        modalInstance.result.then(function () {
            SavesService.deleteSearch(id, function() {
                SavesService.list(function(data) {
                    $scope.saves = data;
                });
            })
        }, function () {});
    };
};

