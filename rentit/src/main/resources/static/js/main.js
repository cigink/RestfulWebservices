var app = angular.module('project', ['ngRoute']);

app.config(function($routeProvider) {
	$routeProvider
	    .when('/main', {
	      templateUrl:'views/plants/main.html'
	    })
	    .when('/plants', {
	      controller:'PlantsController',
	      templateUrl:'views/plants/list.html'
	    })
	    .when('/pos', {
	      controller:'PoController',
	      templateUrl:'views/plants/polist.html'
	    })
	    .when('/plants/create', {
	      controller: 'CreatePlantsController',
	      templateUrl: 'views/plants/create.html'
	    })
	    .when('/plants/update', {
	      controller: 'CreatePlantsController',
	      templateUrl: 'views/plants/update.html'
	    })
	    .otherwise({
	      redirectTo:'/main'
	    });
	});
