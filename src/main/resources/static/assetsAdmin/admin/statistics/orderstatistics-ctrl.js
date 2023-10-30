app.controller('orderstatistics-ctrl', function($scope, $http) {
	$scope.form = {};
	$scope.itemsThongKeOrder = [];
	$scope.itemsThongKeSTstatus = [];
	$scope.form = {};
	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}
	
	$scope.initialize = function() {
		$http.get('/rest/orderdetails/thongke').then(response => {
			$scope.itemsThongKeOrder = response.data;
			console.log($scope.itemsThongKeOrder);
			
		}).catch(err => {
			
			console.log(err);
		})
		
		$http.get('/rest/orders/slstatus').then(response => {
			$scope.itemsThongKeSTstatus = response.data;
			console.log($scope.itemsThongKeSTstatus);
			
		})	
		
	}
	

	
	$scope.calculateTotalCount = function() {
    let totalCount = 0;
    for (let i = 0; i < $scope.itemsThongKeSTstatus.length; i++) {
        totalCount += $scope.itemsThongKeSTstatus[i].count;
    }
    return totalCount;
	};
	
	
	
	$scope.pager = {
		page: 0,
		size: 5,
		get itemsThongKeOrder() {
			var start = this.page * this.size;
			return $scope.itemsThongKeOrder.slice(start, start + this.size);
		},
		/*get sumsum(){
			var sum1 =
			
			$scope.itemsThongKeOrder.sum
			return this.items
				.reduce((total, sum1) => total += sum1, 0);
			},*/
		get count() {
			return Math.ceil(1.0 * $scope.itemsThongKeOrder.length
				/ this.size);
		},
		first() {
			this.page = 0;

		},
		prev() {
			this.page--;
			if (this.page < 0) {
				this.last();
			}
		},
		next() {
			this.page++;
			if (this.page >= this.count) {
				this.first();
			}
		},
		last() {
			this.page = this.count - 1;
		},
		
		
	}
	$scope.initialize();
})