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
					
					
					//					var searchDates = resp.data.map(item => {
					//						var formattedOrderDate = moment(item.orderdate, "YYYY,MM,DD,hh,mm");
					//						return formattedOrderDate.format('DD-MM-YYYY hh:mm A');
					//					});
					//					console.log("Các ngày tìm kiếm được:", searchDates);
					//					$scope.searchDates = searchDates;

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
		$http.get('/excel-order', { responseType: 'arraybuffer' })
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

