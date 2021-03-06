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

angular.module('Helpers', [])
.service('EqualsHelper', function() {
    var _eq = function(a, b) {
        if (!(((a instanceof Object) || (a instanceof Array))
                || ((b instanceof Object) || (b instanceof Array)))) {
            return a === b;
        }
        if ((a == null && b != null) || (a != null && b == null)) {
            return false;
        }
        for( var p in a ) {
            var va = a[p],
                vb = b[p];
            if( !_eq(va, vb) ) return false;
        }
        for( var p in b ) {
            var va = a[p],
                vb = b[p];
            if( !_eq(va, vb) ) return false;
        }
        return true;
    };
    this.equal = _eq;
})
.service('ExtractHelper', function() {
    var extractFn = function(val, fields) {
        if (val instanceof Array) {
            var result = [];
            angular.forEach(val, function(v) {
                result.push(extractFn(v, fields));
            })
            return result;
        }
        if( fields instanceof Array ) {
            var extracted = {};
            angular.forEach(fields, function(field) {
                extracted[field] = val[field];
            });
            return extracted;
        } else {
            return val[fields];
        }
    };
    this.extract = extractFn;
})
.service('ArrayHelper', ["ExtractHelper", "EqualsHelper", function(ExtractHelper,EqualsHelper) {
    var extractFn = function(arr, fn) {
        var results = [];
        angular.forEach(arr, function(v) {
            results.push( fn(v) );
        });
        return results;
    };
    this.extract = function(arr, fields) {
        if (fields instanceof Function) {
            return extractFn(arr, fields);
        }
        return extractFn(arr, function(v) { return ExtractHelper.extract(v, fields); });
    };
    var containsEqual = function(arr, value) {
        for( var i in arr ) {
            if( EqualsHelper.equal(arr[i], value) ) {
                return true;
            }
        }
        return false;
    };
    this.containsEqual = containsEqual;
    var ensureArray = function(arr) {
        if( !(arr instanceof Array) ) {
            return [arr];
        }
        return arr;
    };
    this.ensureArray = ensureArray;
    this.forAll = function(arr, callback) {
        var conditionsMet = true;
        angular.forEach( arr, function(item, key) {
            if( !callback(item, key) ) {
                conditionsMet = false;
            }
        });
        return conditionsMet;
    };
    var forAny = function(arr, callback) {
        for (var k in arr) {
            var v = arr[k];
            if (callback(v, k)) {
                return true;
            }
        }
        return false;
    };
    this.forAny = forAny;
    this.containsAny = function(arr, toContain) {
        toContain = ensureArray(toContain);
        return forAny(ensureArray(arr), function(v,k) { return containsEqual(toContain, v); });
    };
}])
.service('FilterHelper', ["ExtractHelper", "ArrayHelper", function(ExtractHelper, ArrayHelper) {
    this.notInArray = function(arr) {
        return function(val) {
            if( !arr ) return true;
            return arr.indexOf(val) == -1;
        }
    };
    this.extractedFieldNotInArray = function(arr, fields) {
        return function(val) {
            if( !arr ) return true;
            if( !(fields instanceof Array) ) {
                return arr.indexOf(ExtractHelper.extract(val, fields)) == -1;
            } else {
                return !ArrayHelper.containsEqual( arr, ExtractHelper.extract(val, fields) );
            }
        }
    };
    this.extractedFieldInArray = function(arr, fields) {
        return function(val) {
            if( !arr ) return true;
            if( !(fields instanceof Array) ) {
                return arr.indexOf(ExtractHelper.extract(val, fields)) != -1;
            } else {
                return ArrayHelper.containsEqual( arr, ExtractHelper.extract(val, fields) );
            }
        }
    };
}])
.service('KoodiHelper', [function() {
    this.koodiValue = function(v) {
        var _Pos = v.indexOf("_");
        if (_Pos > 0) {
            v = v.substring(_Pos+1);
        }
        var hashPos = v.indexOf("#");
        if (hashPos > 0) {
            v = v.substring(0,hashPos);
        }
        return v;
    };
}])
.directive('fullSizeGrid', ['$timeout', '$log', function ($timeout, $log) {
    $log.info("Directive called.");
    return {
        link: function ($scope, element, attrs) {
            $log.info("Link called in scope: ");
            $log.info($scope);
            var resizeAction = function (resizeParent) {
                $log.info("Results loaded directive.");
                $timeout(function () {
                    $log.info(element);
                    var $el = $(element),
                        widthPadding = $el.attr("data-width-padding") || 0,
                        heightPadding = $el.attr("data-height-padding") || 0;
                    $el.css("width", ($( window ).width()-widthPadding)+"px" )
                        .css("height", ($( window ).height()-heightPadding)+"px");
                    if( resizeParent ) {
                        $(element).parent().resize();
                    }
                }, 0, false);
            };
            $scope.$on('resultsloaded', resizeAction);
            $(window).resize( function() { return resizeAction(false); });
        }
    };
}])
.directive('focusOn', ['$timeout', function($timeout) {
    return function(scope, elem, attr) {
        scope.$on(attr.focusOn, function(e) {
            $timeout(function() {
                elem[0].focus();
            });
        });
    };
}])
.directive('selectAllOn', ['$timeout', function($timeout) {
    return function(scope, elem, attr) {
        scope.$on(attr.selectAllOn, function(e) {
            $timeout(function() {
                elem[0].select();
            });
        });
    };
}]);
