/**
 * Created by ratamaa on 12/4/13.
 */

angular.module('Helpers', []).service('FilterHelper', function() {
    this.extractedFieldNotInArray = function(arr, field) {
        return function(val) {
            if( !arr ) return true;
            return arr.indexOf(val[field]) == -1;
        }
    };
});
