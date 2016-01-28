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
 * Created by ratamaa on 12/4/13.
 */
OsoiteKoostepalvelu.controller('SavesPopupController', ["$scope", "$modalInstance", "$modal", "$filter", "saves",
    "LocalisationService", "SavesService",
    function ($scope, $modalInstance, $modal, $filter, saves,
            LocalisationService, SavesService) {
    var msg = function( key, params ) {
        return LocalisationService.t(key, params);
    };
    $scope.msg = msg;

    $scope.saves = saves;

    $scope.close = function() {
        $modalInstance.close();
        // $modalInstance.dismiss('cancel');
    };

    $scope.deleteSave = function(id) {
        var modalInstance = $modal.open({
            templateUrl: 'partials/confirmPopup.html',
            controller: 'ConfirmPopupController',
            resolve: {options: function() {
                return {
                    title: msg('saves_popup_delete_confirm_title', [$filter('filter')($scope.saves, {id: id})[0].name] ),
                    yesText: msg('saves_popup_delete_confirm_yes'),
                    noText: msg('saves_popup_delete_confirm_no'),
                    yesClass: 'btn-danger'
                };
            }}
        });
        modalInstance.result.then(function () {
            SavesService.deleteSearch(id, function() {
                SavesService.listSearch(function(data) {
                    $scope.saves = data;
                });
            })
        }, function () {});
    };
}]);
