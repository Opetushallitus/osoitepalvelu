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
OsoiteKoostepalvelu.controller('NewSavePopupController', ["$scope", "$modalInstance", "$modal", "$timeout",
    "save", "onSaveNew", "LocalisationService", "SavesService",
    function ($scope, $modalInstance, $modal, $timeout,
            save, onSaveNew, LocalisationService, SavesService) {
    var msg = function( key, params ) {
        return LocalisationService.t(key, params);
    };
    $scope.msg = msg;

    $scope.save = save;

    $scope.saveAction = function() {
      if( save.id ) {
          SavesService.updateSearch($scope.save, function() {
            $modalInstance.close();
          });
      } else {
         SavesService.saveSearch($scope.save, function(id) {
            $modalInstance.close();
            if (onSaveNew) {
                onSaveNew(id);
            }
         });
      }
    };

    $scope.saveAs = function() {
      var modalInstance = $modal.open({
          templateUrl: 'partials/newSavePopup.html',
          controller: 'NewSavePopupController',
          resolve: {
              save: function() {
                  return angular.extend({}, $scope.save, {
                      id: null,
                      name: $scope.save.name + msg('save_name_copy_ending')
                  });
              },
              onSaveNew: function() {
                  return onSaveNew;
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
}]);


