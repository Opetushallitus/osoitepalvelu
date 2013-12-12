/**
 * Created by ratamaa on 12/4/13.
 */

angular.module('Helpers', [])
.service('ArrayHelper', function() {
    this.extract = function(arr, field) {
        var results = [];
        angular.forEach(arr, function(v) {
            results.push( v[field] );
        });
        return results;
    };
})
.service('FilterHelper', function() {
    this.notInArray = function(arr) {
        return function(val) {
            if( !arr ) return true;
            return arr.indexOf(val) == -1;
        }
    };
    this.extractedFieldNotInArray = function(arr, field) {
        return function(val) {
            if( !arr ) return true;
            return arr.indexOf(val[field]) == -1;
        }
    };
})
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
}]);
