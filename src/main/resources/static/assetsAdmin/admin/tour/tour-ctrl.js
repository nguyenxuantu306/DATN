app.controller("tour-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.form = {};
	
	$scope.initialize = function(){
		// Load tours
		$http.get("/rest/tours").then(resp =>{
			$scope.items = resp.data;
			$scope.items.forEach(item =>{
				item.startdate = new Date(item.startdate),
				item.enddate = new Date(item.enddate)
			})
		});
	
	}
	// Khởi đầu
	$scope.initialize();
	
	// Hiện thị lên form
	$scope.edit = function(item){
		$scope.form = angular.copy(item);		
	}
});

	
