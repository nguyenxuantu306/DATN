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

	//Tính tổng đơn hàng
	$scope.calculateTotalVoucher = function(item) {
		var totalVoucher = 0.0;
		var voucherDiscount = 0.0;
		var discountAmount = 0.0;
		// Tính tổng giá trị sản phẩm trong đơn hàng
		if (item.orderDetail && item.orderDetail.length > 0) {
			for (var i = 0; i < item.orderDetail.length; i++) {
				totalVoucher += item.orderDetail[i].totalPrice;
			}
		}


		// Áp dụng giảm giá từ voucher nếu có
		if (item.voucherorder && item.voucherorder.length > 0 && item.voucherorder[0].voucher && item.voucherorder[0].voucher.discount) {
			voucherDiscount = item.voucherorder[0].voucher.discount;
			discountAmount = totalVoucher * voucherDiscount;

			// Giảm giá từ voucher
			totalVoucher -= discountAmount;
		} else {
			console.log("Không có voucher hoặc thông tin voucher không đúng!");
			//console.log(item);
		}

		// Đảm bảo tổng không âm
		if (totalVoucher < 0) {
			totalVoucher = 0;
		}

		return totalVoucher;
	};


	$scope.formatPrice = function(price) {
		var priceString = price.toString();
		priceString = priceString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		return priceString + " đ";
	}


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

			/*	$scope.totalPrice = $scope.items2.reduce((sum, item) => sum + item.totalprice, 0);
				console.log($scope.totalPrice);*/
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

		var hasInsufficientQuantity = false; // biến boolean để kiểm tra số lượng sản phẩm
		// Kiểm tra và trừ số lượng sản phẩm trong giỏ hàng
		if (item.statusOrder.statusorderid == '2') {
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

				$http.put(`/rest/products/${orderDetail.product.productid}`, orderDetail.product)
					.then(function(response) {
						console.log("Update success", response);
					})
					.catch(function(error) {
						// Xử lý lỗi
						console.log("Error updating product: ", error);
					});
			});
		}
		if (hasInsufficientQuantity) { // Nếu sản phẩm không đủ số lượng, không cập nhật trạng thái đơn hàng
			return;
		}


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
		// Gửi yêu cầu HTTP đến API /byStatusName với tham số statusName là giá trị đã chọn
		$http.get('/rest/orders/byStatusName', { params: { statusName: $scope.selectedStatus } })
			.then(function(response) {
				// Xử lý dữ liệu trả về từ API
				$scope.items = response.data;
			})
			.catch(function(error) {
				console.error("Lỗi trong quá trình gửi yêu cầu: " + error);
			});
	};



	$scope.pager = {
		page: 0,
		size: 100,
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





	//	$scope.filterOrders = function() {
	//		// Lấy giá trị ngày bắt đầu và kết thúc từ các trường input
	//		var startDateTime = $scope.startDateTime;
	//		var endDateTime = $scope.endDateTime;
	//
	//		// Kiểm tra xem các giá trị có hợp lệ không, ví dụ: không được để trống
	//		if (startDateTime && endDateTime) {
	//			// Sử dụng thư viện moment.js để định dạng ngày thời gian
	//			var formattedStartDateTime = moment(startDateTime, "DD-MM-YYYY hh:mm A").format();
	//			var formattedEndDateTime = moment(endDateTime, "DD-MM-YYYY hh:mm A").format();
	//			// Ghi log ngày bắt đầu và kết thúc
	//			console.log("Start Date:", formattedStartDateTime);
	//			console.log("End Date:", formattedEndDateTime);
	//			// Gọi API để tìm kiếm danh sách đơn hàng bằng khoảng ngày sử dụng $http
	//			$http.get("/rest/orders/search", {
	//				params: {
	//					startDateTime: formattedStartDateTime,
	//					endDateTime: formattedEndDateTime
	//				}
	//			}).then(resp => {
	//				$scope.items = resp.data;
	//
	//				// Hiển thị các ngày tìm kiếm được
	//				var searchDates = resp.data.map(item => item.formattedOrderDate);
	//				console.log("Các ngày tìm kiếm được:", searchDates);
	//			}).catch(error => {
	//				console.log("Error status:", error.status);
	//				console.log("Error message:", error.data);
	//				console.log("Error headers:", error.headers);
	//				console.log("Error config:", error.config);
	//			});
	//		} else {
	//			// Xử lý lỗi nếu dữ liệu không hợp lệ
	//			console.log("Invalid input data");
	//		}
	//	};



	$scope.filterOrders = function() {
		var startDateTime = $scope.startDateTime;
		var endDateTime = $scope.endDateTime;

		if (startDateTime && endDateTime) {
			var formattedStartDateTime = moment(startDateTime, "DD-MM-YYYY hh:mm A").toDate(); // Chuyển đổi sang đối tượng Date
			var formattedEndDateTime = moment(endDateTime, "DD-MM-YYYY hh:mm A").toDate(); // Chuyển đổi sang đối tượng Date

			console.log("Start Date:", formattedStartDateTime);
			console.log("End Date:", formattedEndDateTime);

			$http.get("/rest/orders/search", {
				params: {
					startDateTime: formattedStartDateTime.toISOString(),
					endDateTime: formattedEndDateTime.toISOString()
				}
			})
				.then(resp => {
					$scope.items = resp.data;


					//$scope.exportExcel();

					$scope.items.forEach(function(item) {
						var formattedOrderDate = moment(item.orderdate, "YYYY-MM-DDTHH:mm:ss").toDate();
						var formattedOrderDateStr = moment(formattedOrderDate).format('DD-MM-YYYY hh:mm A');
						item.orderdate = formattedOrderDateStr;
					});

					var searchDates = resp.data.map(item => {
						return item.orderdate;
					});
					console.log("Các ngày tìm kiếm được:", searchDates);
					$scope.searchDates = searchDates;

				})
				.catch(error => {
					console.log("Error status:", error.status);
					console.log("Error message:", error.data);
					console.log("Error headers:", error.headers);
					console.log("Error config:", error.config);
				});

		} else {
			console.log("Invalid input data");
		}
	};

	$scope.resetFilter = function() {
		// Xóa giá trị của startDateTime và endDateTime
		$scope.startDateTime = null;
		$scope.endDateTime = null;

		loadOrders();
	};

	function loadOrders() {
		// Gọi API để tải danh sách đơn hàng với hoặc không có bộ lọc
		// Sử dụng $http.get hoặc phương thức tải lại tùy thuộc vào mã của bạn
		$http.get("/rest/orders").then(resp => {
			$scope.items = resp.data;
		}).catch(error => {
			console.log("Error status:", error.status);
			console.log("Error message:", error.data);
			console.log("Error headers:", error.headers);
			console.log("Error config:", error.config);
		});
	}


	// Trong AngularJS controller hoặc service
	$scope.exportExcel = function() {
		$http.get('/excel-order', {
			params: {
				startDateTime: $scope.startDateTime,
				endDateTime: $scope.endDateTime
			},
			responseType: 'arraybuffer'
		})
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				link.download = 'order.xlsx';
				link.click();
			})
			.catch(function(error) {
				console.error('Error exporting Excel:', error);
			});
	};
	// PDF

	$scope.exportPdf = function() {
		$http.get('/pdf-order', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/pdf' });
				var objectUrl = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = objectUrl;
				a.download = 'order.pdf';
				a.click();
				URL.revokeObjectURL(objectUrl);
			})
			.catch(function(error) {
				console.error('Error exporting PDF:', error);
			});
	};

	//	$scope.startDateTime = '';
	//	$scope.endDateTime = '';
	//	$scope.originalItems = [];
	//	$scope.items = [];
	//
	//	$scope.filterOrdersByDateRange = function() {
	//		$http.get("/rest/orders").then(function(resp) {
	//			$scope.originalItems = resp.data;
	//			$scope.items = $scope.originalItems.map(item => {
	//				return {
	//					...item,
	//					orderDate: new Date(item.orderDate)
	//				};
	//			});
	//
	//			const startDate = $scope.startDateTime ? new Date($scope.startDateTime) : null;
	//			const endDate = $scope.endDateTime ? new Date($scope.endDateTime) : null;
	//
	//			$scope.items = $scope.items.filter(item => {
	//				const orderDate = new Date(item.orderDate);
	//
	//				if (startDate && endDate) {
	//					return orderDate >= startDate && orderDate <= endDate;
	//				} else if (startDate) {
	//					return orderDate >= startDate;
	//				} else if (endDate) {
	//					return orderDate <= endDate;
	//				}
	//
	//				// If both startDate and endDate are not selected, don't apply date filter
	//				return true;
	//			});
	//
	//			// Display success message
	//			alert("Search successful!");
	//			console.log(startDate);
	//			console.log(endDate);
	//		}).catch(function(error) {
	//			// Display error message
	//			alert("Search failed: " + error);
	//		});
	//	};
	//
	//	$scope.resetDateFilters = function() {
	//		$scope.startDateTime = '';
	//		$scope.endDateTime = '';
	//		$scope.filterOrdersByDateRange();
	//	};
	//


});

