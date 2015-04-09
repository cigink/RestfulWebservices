var app = angular.module('project');

app.controller('PlantsController', function($scope, $http, $route, $location) {
	$scope.plants = [];
	$http.get('/rest/plants').success(function(data, status, headers, config) {
		console.log(JSON.stringify(data));
		$scope.plants = data;
	});
	
	$scope.viewPlant = function () {
		
		 $location.path('/plants/create');
	};
	
    $scope.follow = function(link) {
        if (link.method == 'PUT') {   
            app.current_phr_link = link;
            $location.path('/plants/update'); 
        } 
        else{
    	 $http.delete(link.href).success(function(data, status, headers, config) {
             console.log(data);
             $route.reload();
         }); 
        }
    };
});
	

app.controller('CreatePlantsController', function($scope, $http, $location) {		

	
	$scope.createPlant = function () {
		plant = { name: $scope.name, description: $scope.description, price: $scope.price};
		if ($location.path() == "/plants/create"){
        $http.post('/rest/plants/', plant).success(function(data, status, headers, config) {
            console.log(status);     
            $location.path('/plants');
        });
		}
        else{
			
        	$http.put(app.current_phr_link.href, plant).success(function(data, status, headers, config) {
		        console.log(data);
				console.log(status);
		        app.current_phr_link = null;
		        $location.path('/plants');
            });
        }
        	
        };
            
});

app.controller('PoController', function($scope, $http, $route, $location) {
	$scope.pos = [];
	$http.get('/rest/pos').success(function(data, status, headers, config) {
		console.log(JSON.stringify(data));
		$scope.pos = data;
	});
	
	 $scope.followpo = function(link) {
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
	            $http.put(link.href).success(function(data, status, headers, config) {
	                console.log(data);
	                app.current_phr_link = link;
	                $location.path('/pos');
//	            	$location.path('/phr/update'); 
	            }); 
			}
		};
});