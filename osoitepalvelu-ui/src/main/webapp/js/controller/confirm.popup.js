/**
 * Created by ratamaa on 12/12/13.
 */
var ConfirmPopupController = function ($scope, $modalInstance, $modal, $timeout, LocalisationService, options) {
    var msg = function( key, params ) {
        return LocalisationService.t(key, params);
    };
    $scope.msg = msg;

    $scope.title = options.title;
    $scope.yesText = options.yesText || msg('confirm_yes');
    $scope.noText = options.noText || msg('confirm_no');
    $scope.yesClass = options.yesClass || "btn-primary";
    $scope.noClass = options.noClass || "btn-lg";

    $scope.yes = function() {
        $modalInstance.close();
    };

    $scope.no = function() {
        $modalInstance.dismiss('cancel');
    };

    $timeout(function() {
        $scope.$broadcast("dialogOpened");
    });
};
