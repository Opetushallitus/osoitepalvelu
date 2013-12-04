/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('SearchService', function($log) {
    var _terms = {};

    this.updateTerms = function(terms) {
        _terms = terms;
    };

    this.search = function(success) {
        /// TODO:
        success( [] );
    };
});
