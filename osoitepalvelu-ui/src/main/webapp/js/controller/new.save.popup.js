/**
 * Created by ratamaa on 12/9/13.
 */
var NewSavePopupController = function ($scope, $modalInstance, $modal, $timeout, save, i18n, SavesService) {
    $scope.msg = i18n;

    $scope.save = save;

    $scope.saveAction = function() {
      if( save.id ) {
          SavesService.updateSearch($scope.save, function() {
              $modalInstance.close();
          });
      } else {
         SavesService.saveSearch($scope.save, function() {
              $modalInstance.close();
        });
      }
    };

    $scope.saveAs = function() {
      var modalInstance = $modal.open({
          templateUrl: 'partials/newSavePopup.html',
          controller: NewSavePopupController,
          resolve: {
              save: function() {
                  return angular.extend({}, $scope.save, {
                      id: null,
                      name: $scope.save.name + i18n.save_name_copy_ending
                  });
              }
          }
      });
      modalInstance.result.then(function () {
          $modalInstance.close();
      }, function () {
      });
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

    $timeout(function() {
        $scope.$broadcast("dialogOpened");
    });
};
