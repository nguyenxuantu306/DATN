app.controller('productstatistics-ctrl', function($scope, $http) {

	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}
	$scope.form = {};
	$scope.itemsThongKeSp = [];

	$scope.initialize = function() {
		$http.get('/rest/products/thongke/sp').then(response => {
			$scope.itemsThongKeSp = response.data;
			console.log($scope.itemsThongKeSp);
		}).catch(err => {
			console.log(err);
		})
	}
	$scope.pager = {
		page: 0,
		size: 5,
		get itemsThongKeSp() {
			var start = this.page * this.size;
			return $scope.itemsThongKeSp.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.itemsThongKeSp.length
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

});