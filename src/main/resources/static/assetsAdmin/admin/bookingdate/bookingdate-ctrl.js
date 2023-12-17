app.controller("bookingdate-ctrl", function($scope, $http) {
	/*alert("Quản lý order")*/
	$scope.items = [];
	$scope.cates = [];
	$scope.items2 = [];
	$scope.form = {};
	$scope.products = [];
	$scope.selectedItem = null;
	$scope.totalPrice = 0;

	$scope.initialize = function() {

		$http.get("/rest/tourdates").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.tourdates = new Date(item.tourdates)
			})
		});



	}

	// Khởi đầu
	$scope.initialize();

	$scope.calculateTotalBookingPrice = function() {
		return $scope.form.tourdatebooking.reduce(function(sum, booking) {
			return sum + booking.booking.totalprice;
		}, 0);
	};


	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.selectedItem = item;
		$scope.form = angular.copy(item);
		/*$(".nav-tabs a:eq(0)").tab('show');*/
	}
	//Tính tổng
	$scope.calculateTotal = function(item) {
		return (item.booking.adultticketnumber * item.booking.tour.pricings.adultprice) + (item.booking.childticketnumber * item.booking.tour.pricings.childprice);
	}


	$scope.sortType = 'desc';
	$scope.sortKey = 'tourdate';

	$scope.sortBy = function(key) {
		$scope.sortType = ($scope.sortKey === key) ? ($scope.sortType === 'asc' ? 'desc' : 'asc') : 'asc';
		$scope.sortKey = key;
	};




	$scope.selectedStatus = "0";

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

	/*	// tìm kiếm
		$scope.loadData = function() {
			var apiUrl = '/rest/tourdatebookings/searchkeywordtourdatebooking';
	
			// Kiểm tra xem có từ khóa tìm kiếm không
			if ($scope.searchText) {
				apiUrl += '?keyword=' + $scope.searchText;
			}
	
			$http.get(apiUrl)
				.then(function(response) {
					// Cập nhật dữ liệu trong scope
					$scope.items = response.data; // Cập nhật items để phản ánh dữ liệu mới
					$scope.pager.page = 0; // Đặt lại trang về 0 khi có dữ liệu mới
				})
				.catch(function(error) {
					console.error('Lỗi khi tải dữ liệu:', error);
				});
		};
	
		$scope.loadData1 = function() {
			var apiUrl = '/rest/tourdatebookings/filtertourdate';
	
			// Lấy giá trị ngày từ input
			var selectedDate = $scope.selectedDate;
	
			// Kiểm tra xem có ngày được chọn không
			if (selectedDate) {
				// Format ngày thành chuỗi YYYY-MM-DD để truyền vào URL với múi giờ Việt Nam
				var formattedDate = moment(selectedDate).format('YYYY-MM-DD');
	
				// Thêm thông tin ngày vào URL
				apiUrl += ($scope.searchText ? '&' : '?') + 'date=' + formattedDate;
			} else {
				// Nếu không có ngày được chọn, loại bỏ thông tin ngày từ URL
				apiUrl = apiUrl.replace(/&?date=[^&], '');
			}
	
			$http.get(apiUrl)
				.then(function(response) {
					// Cập nhật dữ liệu trong scope
					$scope.items = response.data; // Cập nhật items để phản ánh dữ liệu mới
					$scope.pager.page = 0; // Đặt lại trang về 0 khi có dữ liệu mới
				})
				.catch(function(error) {
					console.error('Lỗi khi tải dữ liệu:', error);
				});
		};
	
		// Thêm nút xóa dữ liệu
		$scope.clearData = function() {
			$scope.items = []; // Xóa dữ liệu
			$scope.searchText = ''; // Xóa từ khóa tìm kiếm
			$scope.selectedDate = null; // Xóa giá trị ngày
			$scope.loadData(); // Gọi lại hàm loadData để tải dữ liệu mới
		};*/


	// tìm kiếm
	$scope.loadData1 = function() {
		var apiUrl = '/rest/tourdates/searchkeywordtourdate';

		// Kiểm tra xem có từ khóa tìm kiếm không
		if ($scope.searchText) {
			apiUrl += '?keyword=' + $scope.searchText;
		}

		$http.get(apiUrl)
			.then(function(response) {
				// Cập nhật dữ liệu trong scope
				$scope.items = response.data; // Cập nhật items để phản ánh dữ liệu mới
				$scope.pager.page = 0; // Đặt lại trang về 0 khi có dữ liệu mới
			})
			.catch(function(error) {
				console.error('Lỗi khi tải dữ liệu:', error);
			});
	};

	$scope.loadData = function() {
		var apiUrl = '/rest/tourdates/filtertourdate';

		// Lấy giá trị ngày từ input
		var selectedDate = $scope.selectedDate;

		// Kiểm tra xem có ngày được chọn không
		if (selectedDate) {
			// Format ngày thành chuỗi YYYY-MM-DD để truyền vào URL với múi giờ Việt Nam
			var formattedDate = moment(selectedDate).format('YYYY-MM-DD');

			// Thêm thông tin ngày vào URL
			apiUrl += ($scope.searchText ? '&' : '?') + 'date=' + formattedDate;
		} else {
			// Nếu không có ngày được chọn, loại bỏ thông tin ngày từ URL
			apiUrl = apiUrl.replace(/&?date=[^&]*/, '');
		}

		$http.get(apiUrl)
			.then(function(response) {
				// Cập nhật dữ liệu trong scope
				$scope.items = response.data; // Cập nhật items để phản ánh dữ liệu mới
				$scope.pager.page = 0; // Đặt lại trang về 0 khi có dữ liệu mới
			})
			.catch(function(error) {
				console.error('Lỗi khi tải dữ liệu:', error);
			});
	};
	// Thêm nút xóa dữ liệu
	$scope.clearData = function() {
		$scope.items = []; // Xóa dữ liệu
		$scope.searchText = ''; // Xóa từ khóa tìm kiếm
		$scope.selectedDate = null; // Xóa giá trị ngày
		$scope.loadData(); // Gọi lại hàm loadData để tải dữ liệu mới
	};

	// tìm kiếm
	$scope.loadData2 = function() {
		var apiUrl = '/rest/tourdates/searchkeywordtourdate';

		// Check if there is a selected departure day
		if ($scope.selectedDepartureDay) {
			apiUrl += '?keyword=' + $scope.selectedDepartureDay;
		}


		$http.get(apiUrl)
			.then(function(response) {
				// Cập nhật dữ liệu trong scope
				$scope.items = response.data; // Cập nhật items để phản ánh dữ liệu mới
				$scope.pager.page = 0; // Đặt lại trang về 0 khi có dữ liệu mới
			})
			.catch(function(error) {
				console.error('Lỗi khi tải dữ liệu:', error);
			});
	};


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




	$scope.formatPrice = function(price) {
		var priceString = price.toString();
		priceString = priceString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		return priceString + " đ";
	}

	// Trong AngularJS controller hoặc service
	$scope.exportExcel = function() {
		$http.get('/excel-tourDateBooking', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);

				link.download = 'exportTourBookingDate.xlsx';

				link.download = 'tourdatebookingdata.xlsx';

				link.click();
			})
			.catch(function(error) {
				console.error('Error exporting Excel:', error);
			});
	};

	$scope.exportPdf = function() {
		$http.get('/pdf-tourDateBooking', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/pdf' });
				var objectUrl = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = objectUrl;
				a.download = 'exportUser.pdf';
				a.click();
				URL.revokeObjectURL(objectUrl);
			})
			.catch(function(error) {
				console.error('Error exporting PDF:', error);
			});
	};


});

