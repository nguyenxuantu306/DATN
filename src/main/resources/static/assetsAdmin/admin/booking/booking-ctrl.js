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
				item.bookingdate = new Date(item.bookingdate)
			})
		});
		// Load statusBooking
		$http.get("/rest/statusbooking").then(resp => {
			$scope.cates = resp.data;
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
	//Tính tổng
	$scope.calculateTotal = function(item) {
		return (item.adultticketnumber * item.tour.pricings.adultprice) + (item.childticketnumber * item.tour.pricings.childprice);
	}


	// cập nhật trạng thái
	$scope.update = function() {
		var item = angular.copy($scope.form);

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

	$scope.filterBookings = function() {
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

	$scope.filterOrders = function() {
		var startDateTime = $scope.startDateTime;
		var endDateTime = $scope.endDateTime;

		if (startDateTime && endDateTime) {
			var formattedStartDateTime = moment(startDateTime, "DD-MM-YYYY hh:mm A").toDate(); // Chuyển đổi sang đối tượng Date
			var formattedEndDateTime = moment(endDateTime, "DD-MM-YYYY hh:mm A").toDate(); // Chuyển đổi sang đối tượng Date

			console.log("Start Date:", formattedStartDateTime);
			console.log("End Date:", formattedEndDateTime);

			$http.get("/rest/bookings/search", {
				params: {
					startDateTime: formattedStartDateTime.toISOString(),
					endDateTime: formattedEndDateTime.toISOString()
				}
			})
				.then(resp => {
					$scope.items = resp.data;

					$scope.items.forEach(function(item) {
						var formattedOrderDate = moment(item.bookingdate, "YYYY-MM-DDTHH:mm:ss").toDate();
						var formattedOrderDateStr = moment(formattedOrderDate).format('DD-MM-YYYY hh:mm A');
						item.bookingdate = formattedOrderDateStr;
					});

					var searchDates = resp.data.map(item => {
						return item.bookingdate;
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
		$http.get("/rest/bookings").then(resp => {
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
		$http.get('/excel-booking', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				link.download = 'booking.xlsx';
				link.click();
			})
			.catch(function(error) {
				console.error('Error exporting Excel:', error);
			});
	};
	// PDF
	$scope.exportPdf = function() {
		$http.get('/pdf-booking', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/pdf' });
				var objectUrl = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = objectUrl;
				a.download = 'booking.pdf';
				a.click();
				URL.revokeObjectURL(objectUrl);
			})
			.catch(function(error) {
				console.error('Error exporting PDF:', error);
			});
	}

	$scope.formatPrice = function(price) {
		var priceString = price.toString();
		priceString = priceString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		return priceString + " đ";
	}


});

