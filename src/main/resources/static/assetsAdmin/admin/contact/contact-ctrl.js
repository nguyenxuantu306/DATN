app.controller("contact-ctrl", function($scope, $http, $window) {
	$scope.items = [];
	$scope.cates = [];
	$scope.items2 = [];
	$scope.form = {};
	$scope.products = [];
	$scope.selectedItem = null;
	$scope.totalPrice = 0;
	$scope.loggedInUser = [];


	$scope.initialize = function() {
		// Load products
		$http.get("/rest/contact").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.createddate = new Date(item.createddate)
			})
		});
	}

	// Khởi đầu
	$scope.initialize();

	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		$scope.form.commentid = item.commentid;
	}


	

	$scope.pager = {
		page: 0,
		size: 10,
		get items() {
			var start = this.page * this.size;
			return $scope.items ? $scope.items.slice(start, start + this.size) : [];
		},
		get count() {
			return Math.ceil(1.0 * ($scope.items ? $scope.items.length : 0) / this.size);
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
		}
	};

});

