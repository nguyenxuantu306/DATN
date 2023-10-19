app.controller('tytestatistics-ctrl', function($scope, $http) {
	$scope.form = {};
	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}
	$scope.itemsThongKeLoai = [];
	$scope.initialize = function() {
		$http.get('/rest/products/thongke/loai').then(response => {
			$scope.itemsThongKeLoai = response.data;
			console.log($scope.itemsThongKeLoai);
		}).catch(err => {
			console.log(err);
		})
	}
	$scope.pager = {
		page: 0,
		size: 5,
		get itemsThongKeLoai() {
			var start = this.page * this.size;
			return $scope.itemsThongKeLoai.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.itemsThongKeLoai.length
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