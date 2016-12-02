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
var OsoiteKoostepalvelu = angular.module('OsoiteKoostepalvelu');
OsoiteKoostepalvelu.service('SavesService', ["$log", "$http", "SaveConverter", "commonErrorHandler",
        function($log, $http, SaveConverter, commonErrorHandler) {

    this.listSearch = function(success, error) {
        $http.get(window.url('osoitekoostepalvelu.saves', '')).success(success).error(error ||commonErrorHandler);
    };

    this.saveSearch = function(save, success, error) {
        $log.info("Saving search");
        save = SaveConverter.toDomain(save);
        delete(save.id);
        $log.info(save);
        $http.post(window.url('osoitekoostepalvelu.saves', ''), save).success(success).error(error || commonErrorHandler);
    };

    this.updateSearch = function(save, success, error) {
        $log.info("Updating search");
        save = SaveConverter.toDomain(save);
        $log.info(save);
        $http.put(window.url('osoitekoostepalvelu.saves', ''), save).success(success).error(error || commonErrorHandler);
    };

    this.getSearch = function(id, success, error) {
        $http.get(window.url('osoitekoostepalvelu.saves', id)).success(function(data) {
            success( SaveConverter.fromDomain(data) );
        }).error(error || commonErrorHandler);
    };

    this.deleteSearch = function(id, success, error) {
        $log.info("Deleting search: " + id);
        $http.delete(window.url('osoitekoostepalvelu.saves', id)).success(success).error(error || commonErrorHandler);
    };
}]);

OsoiteKoostepalvelu.service("SaveConverter", ["$log", "$filter", "FilterHelper", "ArrayHelper",
                   "AddressFields", "TargetGroups",
        function($log, $filter, FilterHelper, ArrayHelper, AddressFields, TargetGroups) {
    this.toDomain = function(save) {
        var domainSave = angular.copy(save);

        domainSave.terms = [];
        angular.forEach(save.terms, function(v,k) {
            domainSave.terms.push({
                type: k,
                values: v
            });
        });
        angular.forEach(domainSave.targetGroups, function(group) {
            delete(group.name);
            delete(group.hideOptions);
            delete(group._uiSelectChoiceDisabled);
            var opts = [];
            angular.forEach ( $filter('filter')(group.options, {selected:true}), function(option) {
                opts.push(option.type);
            } );
            group.options = opts;
        });

        domainSave.addressFields= [];
        angular.forEach(save.addressFields, function(field) {
            if( field.selected ) domainSave.addressFields.push(field.type);
        });

        return domainSave;
    };
    this.fromDomain = function(domainSave) {
        $log.info("FROM DOMAIN:");
        $log.info(domainSave);
        var save = angular.copy(domainSave);

        save.terms = {};
        angular.forEach(domainSave.terms, function(v) {
            save.terms[v.type] = v.values;
        });

        save.addressFields = angular.copy(AddressFields);
        angular.forEach(domainSave.addressFields, function(v) {
            angular.forEach($filter('filter')(save.addressFields, {type:v}), function(v) {v.selected=true;});
        });

        save.targetGroups = $filter('filter')(angular.copy(TargetGroups),
            FilterHelper.extractedFieldInArray(ArrayHelper.extract(domainSave.targetGroups, "type"), "type"));
        angular.forEach(domainSave.targetGroups, function(domainTargetGroup) {
            var targetGroup = $filter('filter')(save.targetGroups, {type:domainTargetGroup.type})[0];
            if( targetGroup && domainTargetGroup.options ) {
                angular.forEach(targetGroup.options, function(option) {
                    option.selected = domainTargetGroup.options && domainTargetGroup.options.indexOf(option.type) != -1;
                });
            }
        });

        return save;
    };
}]);