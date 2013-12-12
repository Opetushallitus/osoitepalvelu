/**
 * Created by ratamaa on 12/12/13.
 */
var ConfirmPopupController = function ($scope, $modalInstance, $modal, $timeout, i18n, options) {
    $scope.msg = i18n;

    $scope.title = options.title;
    $scope.yesText = options.yesText || i18n.confirm_yes;
    $scope.noText = options.noText || i18n.confirm_no;
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
