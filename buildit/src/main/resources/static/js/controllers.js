var app = angular.module('project');

app.controller('PHRController', function($scope, $http, $route, $location) {
	$scope.phrs = [];
	$http.get('/rest/phr').success(function(data, status, headers, config) {
		console.log(data);
		$scope.phrs = data;
	});
	
    $scope.accept = function(accept){
    	$http.post('/rest/phr/{id}/accept',accept.id).success(function(data, status, headers, config){
    		console.log(accept);
    		$route.reload();
    	});
    };
    
    $scope.reject = function(reject){
    	$http.delete('/rest/phr/{id}/accept',reject.id).success(function(data, status, headers, config){
    		console.log(reject);
    		$route.reload();
    	});    	
    };
	
    $scope.cancel = function(close){
    	$http.post('/rest/phr/{id}/accept', close.id).success(function(data, status, headers, config){
    		console.log(close);
    		$route.reload();
    	});
    };
	
    $scope.follow = function(link) {
        console.log(link.method);
        if (link.method == 'POST') {
            $http.post(link.href).success(function(data, status, headers, config) {
                console.log(data);
                $route.reload();
            });
        } 
        else if (link.method == 'DELETE') {
            $http.delete(link.href).success(function(data, status, headers, config) {
                console.log(data);
                $route.reload();
            }); 
		}
        else if (link.method == 'PUT') {
//            $http.put(link.href).success(function(data, status, headers, config) {
//                console.log(data);
                app.current_phr_link = link;
//                $location.path('/phr');
            	$location.path('/phr/update'); 
//            }); 
		}
	};
});

app.controller('CreatePHRController', function($scope, $http, $location) {
	$scope.plants = [];
	$scope.name = '';
	$scope.startDate = new Date();
	$scope.endDate = new Date();
	
	
	console.log($location.path() == "/phr/create");
	$scope.catalogShown = false;
	
	$scope.execQuery = function () {
    	if ($scope.name == "") {
    		$http.get('/rest/plants').success(function(data, status, headers, config) {
    			$scope.plants = data;
                $scope.catalogShown = true;
            });
    	}
    	else {
	        $http.get('/rest/plants/query', {params: {name: $scope.name, startDate: $scope.startDate,
	        	endDate: $scope.endDate}}).success(function(data, status, headers, config) {
	            $scope.plants = data;
	            $scope.catalogShown = true;
	        });
    	};
    };
	
 
    $scope.setPlant = function (selectedPlant) {
	    phr = { plant: selectedPlant, startDate: $scope.startDate, endDate: $scope.endDate};
	
		if ($location.path() == "/phr/create"){
	        $http.post('/rest/phr', phr).success(function(data, status, headers, config) {
	            console.log(status);
//	            $scope.phr= data;
//	            $scope.catalogShown = true;
	            $location.path('/phr');
	        });
		}
		else {
			$http.put(app.current_phr_link.href, phr).success(function(data, status, headers, config) {
		        console.log(data);
				console.log(status);
		        app.current_phr_link = null;
		        $location.path('/phr');
		    });
		}
	};
	
//    $scope.viewPlant = function (){
//    	$scope.Show = true;
//    });
});

app.controller('PlantsController', function($scope, $http) {
	$scope.plants = [];
	$http.get('/rest/plants').success(function(data, status, headers, config) {
		console.log(JSON.stringify(data));
		$scope.plants = data;
	});
	
});

//app.controller('UpdatePHRController', function($scope, $http, $location){
//	$scope.phr = [];
//    $scope.name = '';
//    $scope.startDate = new Date();
//    $scope.endDate = new Date();
//    
//    $scope.id = app.current_phr_link.href.split("/")[5];
//	
//    $http.get('/rest/phr/'+$scope.id).success(function(data, status, headers, config) {
//        console.log(data);
//        console.log(status);
//        $scope.phr = data;
//        $scope.name = data.plant.name;
//        $scope.startDate = new Date(data.startDate);
//        $scope.endDate = new Date(data.endDate);
//    });
//    
//    $http.put(app.current_phr_link.href, phr).success(function(data, status, headers, config) {
//        console.log(data);
//    	console.log(status);        
//        $location.path('/phr');
//    });
//    
//	$scope.sendUpdate = function (selectedPlant) {  
//		console.log(selectedPlant);
//		console.log(app.current_phr_link);
//        phr = {endDate: $scope.endDate};   
//    });
//});