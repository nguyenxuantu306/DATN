app.controller("vouchers-ctrl", function($scope, $http, $window) {
	$scope.items = [];
	$scope.cates = [];
	$scope.items2 = [];
	$scope.form = {};
	$scope.products = [];
	$scope.selectedItem = null;
	$scope.totalPrice = 0;


	$scope.initialize = function() {
		// Load products
		$http.get("/rest/vouchers").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.expirationdate = new Date(item.expirationdate)
			})
		});
		
		$http.get("/rest/vouchers/user").then(resp => {
			$scope.items2 = resp.data;
		});
	}

	// Khởi đầu
	$scope.initialize();


	// Xóa form
	$scope.reset = function() {
		/*$scope.error = ['err'];*/
		$scope.form = {
			publication_date: new Date(),
			image: 'cloud-upload.jpg',
			available: true
		};
		$('#id').attr('readonly', false);
	}

	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
	}

	// Thêm loại sản phẩm mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/vouchers`, item).then(resp => {
			resp.data.expirationdate = new Date(resp.data.expirationdate)
			$scope.items.push(resp.data);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Thêm mã giảm giá thành công!',
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
				text: 'Lỗi thêm mã giảm giá sản phẩm',
			});
			console.log("Error", error);
		});
	}
	
	// cập loại nhật sản phẩm
	$scope.update = function() {
		var item = angular.copy($scope.form);
		$http.put(`/rest/vouchers/${item.voucherid}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.voucherid == item.voucherid);
			$scope.items[index] = item;
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Cập nhật mã thành công!',
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
					text: 'Lỗi Cập nhật mã',
				});
				console.log("Error", error);
			});
	}

	$scope.delete = function(item) {
		// Hiển thị cửa sổ xác nhận trước khi xóa
		Swal.fire({
			title: 'Xác nhận xóa',
			text: 'Bạn có chắc chắn muốn xóa danh mục này?',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: 'Đồng ý',
			cancelButtonText: 'Hủy bỏ'
		}).then((result) => {
			// Kiểm tra xem người dùng đã bấm nút "Đồng ý" hay không
			if (result.isConfirmed) {
				// Nếu đã bấm "Đồng ý", thực hiện xóa
				$http.delete(`/rest/vouchers/${item.voucherid}`).then(resp => {
					var index = $scope.items.findIndex(p => p.voucherid == item.voucherid);
					$scope.items.splice(index, 1);
					$scope.reset();

					// Sử dụng SweetAlert2 cho thông báo thành công
					Swal.fire({
						icon: 'success',
						title: 'Thành công!',
						text: 'Xóa mã thành công!',
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
						text: 'Lỗi mã giảm giá',
					});
					console.log("Error", error);
				});
			}
		});
	};
	
	// tìm kiếm
	$scope.loadData = function() {
		var apiUrl = '/rest/comment/searchkeywordcomment';

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
	$scope.pager1 = {
		page: 0,
		size: 10,
		get items2() {
			var start = this.page * this.size;
			return $scope.items2 ? $scope.items2.slice(start, start + this.size) : [];
		},
		get count() {
			return Math.ceil(1.0 * ($scope.items2 ? $scope.items2.length : 0) / this.size);
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
