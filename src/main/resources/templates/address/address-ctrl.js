app.controller("address-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.cates = [];
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	
	$scope.initialize = function(){
		// Load genres
		$http.get("/rest/address").then(resp =>{
			$scope.items = resp.data;
			
		});
		
		
			
	}

});



