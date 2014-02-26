/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('SavesService', function($log, $http, SaveConverter) {

    var _commonErrorHandler = function(e) {
            $log.error(e);
        };

    this.list = function(success, error) {
        $http.get('api/saves/').success(success).error(error);
    };

    this.saveSearch = function(save, success, error) {
        $log.info("Saving search");
        save = SaveConverter.toDomain(save);
        delete(save.id);
        $log.info(save);
        $http.put('api/saves/', save).success(success).error(error || _commonErrorHandler);
    };

    this.updateSearch = function(save, success, error) {
        $log.info("Updating search");
        save = SaveConverter.toDomain(save);
        $log.info(save);
        $http.post('api/saves/', save).success(success).error(error || _commonErrorHandler);
    };

    this.getSearch = function(id, success, error) {
        $http.get("api/saves/"+id).success(function(data) {
            success( SaveConverter.fromDomain(data) );
        }).error(error || _commonErrorHandler);
    };

    this.deleteSearch = function(id, success, error) {
        $log.info("Deleting search: " + id);
        $http['delete']("api/saves/"+id).success(success).error(error || _commonErrorHandler);
    };
});


OsoiteKoostepalvelu.service("SaveConverter", function($log, $filter, FilterHelper, ArrayHelper,
                        AddressFields, ReceiverTypes, TargetGroups) {
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

        domainSave.receiverFields = [];
        angular.forEach(save.receiverFields, function(field) {
            if( field.selected )  domainSave.receiverFields.push(field.type);
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

        save.receiverFields = angular.copy(ReceiverTypes);
        angular.forEach(domainSave.receiverFields, function(v) {
            angular.forEach($filter('filter')(save.receiverFields, {type:v}), function(v) {v.selected=true;});
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
});