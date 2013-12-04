/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.factory('DummySaves', function() {
    return [
        {id: 12345,     name: 'Ruotsinkielisten lukioiden opot'},
        {id: 12346,     name: 'Kaikkien oppilaitosten rehtorit paitsi ahvenanmaalta'},
        {id: 789148,    name: 'Varsinais-suomen ammattioppilaitokset'},
        {id: 885415,    name: 'AIPAL ja KOULUTA -vastuukäyttäjät'}
    ];
});

OsoiteKoostepalvelu.service('SavesService', function($log, DummySaves, $filter, FilterHelper) {
    var deletedIds = [];

    this.list = function(success) {
        success( $filter('filter')(DummySaves, FilterHelper.extractedFieldNotInArray(deletedIds, "id") ) );
    };

    this.deleteSave = function(id, success) {
        deletedIds.push(id);
        success();
    }
});


OsoiteKoostepalvelu.factory('DummyResults', function() {
    return [
        {firstName: "Dummy", lastName: "Result"}
    ];
});

OsoiteKoostepalvelu.service('SearchService', function($log, DummyResults) {
    var _terms = {};

    this.updateTerms = function(terms) {
        _terms = terms;
    };

    this.search = function() {
        $log.info("Search called. Terms:");
        $log.info(_terms);
        return DummyResults;
    };
});
