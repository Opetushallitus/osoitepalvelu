
angular.module('OsoiteKoostepalvelu', []
//    ,
//    ['ngRoute']
    )
//	.config(function($routeProvider) {
//		$routeProvider
//		.when('/', {
//			controller:'SearchController',
//			templateUrl:'index.html'
//		})
//		.otherwise({
//			redirectTo:'/'
//		});
//	})
	.factory('Results', function() {
		return [
			{firstName: "Dummy", lastName: "Result"}
		];
	});

