/**
 * Created by ratamaa on 12/9/13.
 */
var NewSavePopupController = function ($scope, $modalInstance, save, i18n, SavesService, $log) {
  $scope.msg = i18n;

  $scope.name = null;
  $scope.save = save;

  $scope.save = function() {
      $log.info("Save");
      SavesService.saveSearch(angular.extend({
          name: name
      }, $scope.save), function() {
          $modalInstance.close();
      });
  };

  $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
  };

  $scope.deleteSave = function(id) {
      SavesService.deleteSave(id, function() {
          SavesService.list(function(data) {
              $scope.saves = data;
          });
      })
  };
};
