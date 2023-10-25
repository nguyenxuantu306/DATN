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

	// Thêm sản phẩm mới
	/*$scope.create = function(){
		var item = angular.copy($scope.form);
		$http.post(`/rest/products`,item).then(resp =>{
			resp.data.publication_date = new Date(resp.data.publication_date)
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm sản phẩm thành công!");
		}).catch(error =>{
			alert("Lỗi thêm mới sản phẩm");
			console.log("Error",error);
		});
	}*/

	// cập nhật trạng thái
	$scope.update = function() {
		var item = angular.copy($scope.form);

		$http.put(`/rest/orders/${item.orderid}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.orderid == item.orderid);
			$scope.items[index] = item;
			alert("Cập nhật trạng thái thành công!");
			refreshPage();
		})
			.catch(error => {
				alert("Lỗi cập nhật trạng thái!");
				console.log("Error", error);
			});
	}

	function refreshPage() {
		location.reload();
	}


	// Xóa sản phẩm 
	/*$scope.delete = function(item){
		$http.delete(`/rest/orders/${item.id}`).then(resp =>{
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items.splice(index,1);
			$scope.reset();				
			alert("Xóa thành công!");
		})
		.catch(error =>{
			alert("Lỗi xóa");
			console.log("Error",error);
		});
	}
	*/
	// Upload hình
	/*$scope.imageChanged = function(files){
		var data = new FormData();
		data.append('file',files[0]);
		$http.post('/rest/upload/images',data,{
			transformRequest:angular.identity,
			headers:{'Content-Type':undefined}
		}).then(resp =>{
			$scope.form.image = resp.data.name;
		}).catch(error =>{
			alert("Lỗi upload hình ảnh");
			console.log("Error",error);
		})
	}*/

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

