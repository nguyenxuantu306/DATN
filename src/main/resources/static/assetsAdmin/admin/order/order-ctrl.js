app.controller("order-ctrl", function($scope, $http) {
	/*alert("Quản lý order")*/
	$scope.items = [];
	$scope.cates = [];
	$scope.items2 = [];
	$scope.form = {};
	$scope.products = [];
	$scope.selectedItem = null;
	$scope.totalPrice = 0;
	
	//Tính tổng đơn hàng
	$scope.calculateTotal = function(item) {
		var total = 0;
		if (item.orderDetail && item.orderDetail.length > 0) {
			for (var i = 0; i < item.orderDetail.length; i++) {
				total += item.orderDetail[i].totalprice;
			}
		}
		return total;
	};


	$scope.initialize = function() {
		// Load products
		$http.get("/rest/orders").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.orderDate = new Date(item.orderDate)
			})
		});

		// Load orderdetails
		$http.get("/rest/orderdetails").then(resp => {
			$scope.items2 = resp.data;

			$scope.totalPrice = $scope.items2.reduce((sum, item) => sum + item.totalprice, 0);
			console.log($scope.totalPrice);
		});

		// Load states
		$http.get("/rest/statusorder").then(resp => {
			$scope.cates = resp.data;
		});

		$http.get("/rest/products").then(resp => {
			$scope.products = resp.data;
			$scope.products.forEach(item => {
				item.publication_date = new Date(item.publication_date)
			})
		});

	}

	// Khởi đầu
	$scope.initialize();

	// Xóa form
	/*$scope.reset = function(){
		$scope.form = {
			publication_date:new Date(),
			image:'cloud-upload.jpg',
			available:true,
		};
	}
	*/
	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.selectedItem = item;
		$scope.form = angular.copy(item);
		/*$(".nav-tabs a:eq(0)").tab('show');*/
	}

	// cập nhật trạng thái
	$scope.update = function() {
		var item = angular.copy($scope.form);

		$http.put(`/rest/orders/${item.orderid}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.orderid == item.orderid);
			$scope.items[index] = item;
			// Sử dụng SweetAlert2 cho thông báo thành công
	        Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: 'Cập nhật trạng thái thành công !',
	        });
			refreshPage();
		})
			.catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
	        Swal.fire({
	            icon: 'error',
	            title: 'Lỗi!',
	            text: 'Cập nhật trạng thái thất bại !',
	        });
				console.log("Error", error);
			});
	}

	function refreshPage() {
		location.reload();
	}


	$scope.pager = {
		page: 0,
		size: 10,
		get items() {
			var start = this.page * this.size;
			return $scope.items.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.items.length / this.size);
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
			if (this.page > this.count) {
				this.first();
			}
		},
		last() {
			this.page = this.count - 1;
		}
	};
	
	

});

