var app = angular.module('project', ['ngRoute']);

app.config(function($routeProvider) {
	$routeProvider
	    .when('/plants', {
	      controller:'PlantsController',
	      templateUrl:'views/plants/list.html'
	    })
	    .when('/phr', {
	      controller: 'PHRController',
	      templateUrl: 'views/phrs/list.html'
	    })
	    .when('/phr/create', {
	      controller: 'CreatePHRController',
	      templateUrl: 'views/phrs/create.html'
	    })
    	.when('/phr/update', {
    	controller: 'CreatePHRController',
    	templateUrl: 'views/phrs/update.html'
	    })
	    .otherwise({
	      redirectTo:'/phr'
	    });
	});
