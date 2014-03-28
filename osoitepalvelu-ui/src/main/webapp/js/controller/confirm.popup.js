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
