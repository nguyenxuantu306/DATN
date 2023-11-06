app.controller("booking-ctrl", function($scope, $http) {
	/*alert("Quản lý order")*/
	$scope.items = [];
	$scope.cates = [];
	$scope.items2 = [];
	$scope.form = {};
	$scope.products = [];
	$scope.selectedItem = null;
	$scope.totalPrice = 0;


	$scope.initialize = function() {
		// Load products
		$http.get("/rest/bookings").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.orderDate = new Date(item.orderDate)
			})
		});
		
	}

	// Khởi đầu
	$scope.initialize();
	
	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.selectedItem = item;
		$scope.form = angular.copy(item);
		/*$(".nav-tabs a:eq(0)").tab('show');*/
	}

	// cập nhật trạng thái
    $scope.update = function() {
		var item = angular.copy($scope.form);

		var newStatus = item.StatusBooking.statusbookingid;
		var hasInsufficientQuantity = false; // biến boolean để kiểm tra số lượng sản phẩm
		// Kiểm tra và trừ số lượng sản phẩm trong giỏ hàng
		if (newStatus == '2') {
			angular.forEach($scope.form.orderDetail, function(orderDetail) {
				var product = orderDetail.product;
				var quantityAvailable = orderDetail.product.quantityavailable;

				// Kiểm tra số lượng sản phẩm trong kho
				if (quantityAvailable < orderDetail.quantityordered) {
					// Thông báo lỗi
					alert("Sản phẩm '" + product.productname + "' không đủ số lượng trong kho.");
					hasInsufficientQuantity = true; // Đặt biến này thành true nếu sản phẩm không đủ số lượng
					return false; //Thoát khỏi vòng lặp ngay lập tức
				}
				// Trừ số lượng sản phẩm khỏi kho
				orderDetail.product.quantityavailable -= orderDetail.quantityordered;
			});
		}

		if (hasInsufficientQuantity) { // Nếu sản phẩm không đủ số lượng, không cập nhật trạng thái đơn hàng
			return;
		}

		$http.put(`/rest/bookings/${item.bookingid}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.bookingid == item.bookingid);
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
			var formattedInputDate = new Date($scope.ngayTaoFilter + "- 00:00:00"); // Chuyển đổi chuỗi ngày thành đối tượng Date
			$scope.items = $scope.originalItems.filter(function(item) {
				var orderDate = new Date(item.orderdate.replace('- ', ' ') + ":00"); // Chuyển đổi chuỗi ngày thành đối tượng Date
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

