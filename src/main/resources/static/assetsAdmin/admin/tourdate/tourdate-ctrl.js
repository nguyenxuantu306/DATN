app.controller("tourdate-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.cates = [];
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	$scope.deletedItems = [];


	$scope.searchCategory = '';

	$scope.initialize = function() {
		// Load products
		$http.get("/rest/tourdates").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.tourdates = new Date(item.tourdates)
			})
		});


		// Load các tour
		$http.get("/rest/tours").then(resp => {
			$scope.cates = resp.data;
		});


	}

	// Khởi đầu
	$scope.initialize();

	// Xóa form
	$scope.reset = function() {
		/*$scope.error = ['err'];*/
		$scope.form = {
			availableslots: 45, // Đặt giá trị mặc định là 45 cho số chỗ
		};
	}

	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
	}


	// Thêm sự kiện mới
	$scope.create = function() {
		var item = angular.copy($scope.form);

		// Chuyển đổi ngày thành đối tượng Date
		item.tourdates = new Date(item.tourdates);

		// Kiểm tra xem ngày tham quan đã tồn tại trong danh sách hay chưa
		var isDuplicate = $scope.items.some(function(existingItem) {
			return new Date(existingItem.tourdates).toDateString() === item.tourdates.toDateString();
		});

		if (isDuplicate) {
			// Ngày tham quan đã tồn tại, hiển thị thông báo lỗi
			Swal.fire({
				icon: 'warning',
				title: 'Lỗi!',
				text: 'Ngày tham quan đã tồn tại.',
			});
		} else {
			$http.post(`/rest/tourdates`, item).then(resp => {
				resp.data.tourdates = new Date(resp.data.tourdates);
				$scope.items.push(resp.data);
				$scope.reset();
				// Sử dụng SweetAlert2 cho thông báo thành công
				Swal.fire({
					icon: 'success',
					title: 'Thành công!',
					text: 'Tổ chức ngày tham quan thành công!',
				}).then((result) => {
					// Kiểm tra xem người dùng đã bấm nút "OK" hay chưa
					if (result.isConfirmed) {
						// Nếu đã bấm, thực hiện reload trang
						location.reload();
					}
				});
				$scope.form = {}; // Hoặc thực hiện các bước cần thiết để reset form
				$scope.frmvalidate.$setPristine();
				$scope.frmvalidate.$setUntouched();
				$scope.frmvalidate.$submitted = false;
			}).catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi Tổ chức ngày tham quan',
				});
				console.log("Error", error);
			});
		}
	}

	// Cập nhật sự kiện
	$scope.update = function() {
		var item = angular.copy($scope.form);

		// Chuyển đổi ngày thành đối tượng Date
		item.tourdates = new Date(item.tourdates);

		// Kiểm tra xem ngày tham quan đã tồn tại trong danh sách (trừ sự kiện đang cập nhật) hay chưa
		var isDuplicate = $scope.items.some(function(existingItem) {
			return existingItem.tourdateid !== item.tourdateid &&
				new Date(existingItem.tourdates).toDateString() === item.tourdates.toDateString();
		});

		if (isDuplicate) {
			// Ngày tham quan đã tồn tại, hiển thị thông báo lỗi
			Swal.fire({
				icon: 'warning',
				title: 'Lỗi!',
				text: 'Ngày tham quan đã tồn tại.',
			});
		} else {
			$http.put(`/rest/tourdates/${item.tourdateid}`, item).then(resp => {
				var index = $scope.items.findIndex(p => p.tourdateid == item.tourdateid);
				$scope.items[index] = item;
				// Sử dụng SweetAlert2 cho thông báo thành công
				Swal.fire({
					icon: 'success',
					title: 'Thành công!',
					text: 'Cập nhật ngày tổ chức thành công!',
				}).then((result) => {
					// Kiểm tra xem người dùng đã bấm nút "OK" hay chưa
					if (result.isConfirmed) {
						// Nếu đã bấm, thực hiện reload trang
						location.reload();
					}
				});
				$scope.form = {}; // Hoặc thực hiện các bước cần thiết để reset form
				$scope.frmvalidateupdate.$setPristine();
				$scope.frmvalidateupdate.$setUntouched();
				$scope.frmvalidateupdate.$submitted = false;
				$scope.edit(item);
			})
				.catch(error => {
					// Sử dụng SweetAlert2 cho thông báo lỗi
					Swal.fire({
						icon: 'error',
						title: 'Lỗi!',
						text: 'Lỗi Cập nhật ngày tổ chức',
					});
					console.log("Error", error);
				});
		}
	}



	$scope.delete = function(item) {
		// Hiển thị cửa sổ xác nhận trước khi xóa
		Swal.fire({
			title: 'Xác nhận xóa',
			text: 'Bạn có chắc chắn muốn xóa ngày tổ chức này?',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: 'Đồng ý',
			cancelButtonText: 'Hủy bỏ'
		}).then((result) => {
			// Kiểm tra xem người dùng đã bấm nút "Đồng ý" hay không
			if (result.isConfirmed) {
				// Nếu đã bấm "Đồng ý", thực hiện xóa
				$http.delete(`/rest/tourdates/${item.tourdateid}`).then(resp => {
					var index = $scope.items.findIndex(p => p.tourdateid == item.tourdateid);
					$scope.items.splice(index, 1);
					$scope.reset();

					// Sử dụng SweetAlert2 cho thông báo thành công
					Swal.fire({
						icon: 'success',
						title: 'Thành công!',
						text: 'Xóa danh mục thành công!',
					}).then((result) => {
						// Kiểm tra xem người dùng đã bấm nút "OK" hay chưa
						if (result.isConfirmed) {
							// Nếu đã bấm, thực hiện reload trang
							location.reload();
						}
					});

				}).catch(error => {
					// Sử dụng SweetAlert2 cho thông báo lỗi
					Swal.fire({
						icon: 'error',
						title: 'Lỗi!',
						text: 'Lỗi danh mục sản phẩm',
					});
					console.log("Error", error);
				});
			}
		});
	};





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

