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
				total += item.orderDetail[i].totalPrice;
			}
		}
		return total;
	};

	// Hàm định dạng giá tiền
	$scope.formatPrice = function(price) {
		// Sử dụng hàm toLocaleString để định dạng giá tiền
		return price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
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



	$scope.selectedStatus = "1";

	$scope.filterOrders = function() {
		// Lặp qua tất cả các hàng của bảng đơn hàng
		var rows = document.querySelectorAll("#orderList .text-center");
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			var statusCell = row.querySelector("td:nth-child(5)"); // Lấy ô chứa trạng thái

			// Kiểm tra xem trạng thái của đơn hàng có khớp với trạng thái đã chọn hoặc là trạng thái 1 hay không
			if (statusCell.textContent.trim() === selectedStatus || selectedStatus === "1") {
				// Hiển thị đơn hàng nếu khớp hoặc nếu đã chọn "Tất cả"
				row.style.display = "table-row";
			} else {
				// Ẩn đơn hàng nếu không khớp
				row.style.display = "none";
			}
		}
	};

	$scope.ngayTaoFilter = ''; // Trường nhập kiểu text

	$scope.filterOrdersByNgayTao = function() {
		if ($scope.ngayTaoFilter === '') {
			// Nếu trường nhập kiểu text rỗng, hiển thị toàn bộ đơn hàng
			$scope.items = $scope.originalItems;
		} else {
			// Nếu trường nhập kiểu text có dữ liệu, lọc đơn hàng dựa trên ngày tạo
			var formattedInputDate = new Date($scope.ngayTaoFilter + "T00:00:00"); // Chuyển đổi chuỗi ngày thành đối tượng Date
			$scope.items = $scope.originalItems.filter(function(item) {
				var orderDate = new Date(item.orderdate.replace('T', ' ') + ":00"); // Chuyển đổi chuỗi ngày thành đối tượng Date
				var inputDate = formattedInputDate;

				// So sánh ngày tạo và ngày tìm kiếm
				return orderDate.toDateString() === inputDate.toDateString();
			});
		}
	};

	$scope.pager = {
		page: 0,
		size: 40,
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
	}

});

