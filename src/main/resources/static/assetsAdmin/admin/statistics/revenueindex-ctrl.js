app.controller('revenueindex-ctrl', function($scope, $http) {
	$scope.form = {};
	$scope.itemsThongKeSTstatus = [];
	$scope.itemsThongKestats = [];
	
	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}
	
	$scope.initialize = function() {
		
		$http.get('/rest/orders/slstatus').then(response => {
			$scope.itemsThongKeSTstatus = response.data;
			console.log($scope.itemsThongKeSTstatus);
			
		})	
		
		$http.get('/rest/rating/stats').then(response => {
			$scope.itemsThongKestats = response.data;
			console.log($scope.itemsThongKestats);
			
		})	
		
	}
	
	$scope.calculateTotalCount = function() {
    let totalCount = 0;
    for (let i = 0; i < $scope.itemsThongKeSTstatus.length; i++) {
        totalCount += $scope.itemsThongKeSTstatus[i].count;
    }
    return totalCount;
	};
	
	$scope.calculateTotalCountstats = function() {
    let totalCount = 0;
    for (let i = 0; i < $scope.itemsThongKestats.length; i++) {
        totalCount += $scope.itemsThongKestats[i].count;
    }
    return totalCount;
	};
			
	$scope.initialize();
})