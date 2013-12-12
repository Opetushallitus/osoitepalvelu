/**
 * Created by ratamaa on 12/4/13.
 */

OsoiteKoostepalvelu.service('SavesService', function($log, $http) {
    this.list = function(success, error) {
        $http.get('api/saves/').success(success).error(error);
    };

    this.saveSearch = function(save, success, error) {
        $http.put('api/saves/', {data:save}).success(success).error(error);
    };

    this.updateSearch = function(save, success, error) {
        $http.post('api/saves/', {data:save}).success(success).error(error);
    };

    this.getSearch = function(id, success, error) {
        $http.get("/api/saves/"+id).success(success).error(error);
    };

    this.deleteSearch = function(id, success, error) {
        $http.delete("/api/saves/", {params: {id:id}}).success(success).error(error);
    };
});