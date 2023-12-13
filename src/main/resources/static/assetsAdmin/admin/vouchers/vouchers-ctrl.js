app.controller("vouchers-ctrl", function($scope, $http, $window) {
	$scope.items = [];
	$scope.cates = [];
	$scope.items2 = [];
	$scope.form = {};
	$scope.form2 = {};
	$scope.products = [];
	$scope.selectedItem = null;
	$scope.totalPrice = 0;
	$scope.deletedItems = [];

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

		// Load deleted vouchers
		$http.get("/rest/vouchers/deleted").then(resp => {
			$scope.deletedItems = resp.data;
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

	// Hiện thị lên form
	$scope.edit2 = function(item2) {
		$scope.form2 = angular.copy(item2);
	}

	// Thêm voucher
	$scope.create = function() {
		var item = angular.copy($scope.form);

		// Đặt giá trị mặc định cho expirationdate là thời gian hiện tại
		item.expirationdate = new Date();

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


	// cập voucher
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
			text: 'Bạn có chắc chắn muốn xóa mã giảm giá này?',
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


	// Voucherusser
	// Cấp mã
	$scope.createuser = function() {
		var item2 = angular.copy($scope.form2);

		// Check if voucher with the same userid already exists
		var existingVoucher = $scope.items2.find(function(voucher) {
			return voucher.userid === item2.user.userid;
		});

		if (existingVoucher) {
			// If voucher with the same userid exists, show an error message and return
			Swal.fire({
				icon: 'error',
				title: 'Lỗi!',
				text: 'Voucher cho UserID này đã tồn tại.',
			});
			return;
		}
		$http.post(`/rest/vouchers/user`, item2).then(resp => {
			resp.data.expirationdate = new Date(resp.data.expirationdate)
			$scope.items2.push(resp.data);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Cấp mã giảm giá thành công!',
			}).then((result) => {
				// Kiểm tra xem người dùng đã bấm nút "OK" hay chưa
				if (result.isConfirmed) {
					// Nếu đã bấm, thực hiện reload trang
					location.reload();
				}
			});
			$scope.form = {}; // Hoặc thực hiện các bước cần thiết để reset form
			$scope.frmvalidateuser.$setPristine();
			$scope.frmvalidateuser.$setUntouched();
			$scope.frmvalidateuser.$submitted = false;
		}).catch(error => {
			// Sử dụng SweetAlert2 cho thông báo lỗi
			Swal.fire({
				icon: 'error',
				title: 'Lỗi!',
				text: 'Lỗi cấp mã giảm giá',
			});
			console.log("Error", error);
		});
	}

	// cập nhật mã user
	$scope.updateuser = function() {
		var item2 = angular.copy($scope.form2);
		$http.put(`/rest/vouchers/user/${item2.voucheruserid}`, item2).then(resp => {
			var index = $scope.items2.findIndex(p => p.voucheruserid == item2.voucheruserid);
			$scope.items2[index] = item2;
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Cập nhật mã thành công!',
			});
			location.reload();
			$scope.form = {}; // Hoặc thực hiện các bước cần thiết để reset form
			$scope.frmvalidateupdateuser.$setPristine();
			$scope.frmvalidateupdateuser.$setUntouched();
			$scope.frmvalidateupdateuser.$submitted = false;
			$scope.edit2(item2);
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

	$scope.deleteuser = function(item2) {
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
				$http.delete(`/rest/vouchers/user/${item2.voucheruserid}`).then(resp => {
					var index = $scope.items2.findIndex(p => p.voucheruserid == item2.voucheruserid);
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

	$scope.restore = function(voucherid) {
		// Hiển thị cửa sổ xác nhận trước khi khôi phục
		Swal.fire({
			title: 'Xác nhận khôi phục',
			text: 'Bạn có chắc chắn muốn khôi phục voucher này?',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: 'Đồng ý',
			cancelButtonText: 'Hủy bỏ'
		}).then((result) => {
			// Kiểm tra xem người dùng đã bấm nút "Đồng ý" hay không
			if (result.isConfirmed) {
				// Nếu đã bấm "Đồng ý", thực hiện khôi phục
				try {
					axios.put(`/rest/vouchers/${voucherid}/restore`)
						.then(response => {
							console.log("API Response:", response.data);
							// Xử lý phản hồi thành công
							Swal.fire({
								icon: 'success',
								title: 'Thành công!',
								text: 'Khôi phục voucher thành công!',
							}).then((result) => {
								// Kiểm tra xem người dùng đã bấm nút "OK" hay chưa
								if (result.isConfirmed) {
									// Nếu đã bấm, thực hiện reload trang
									location.reload();
								}
							});
						})
						.catch(error => {
							// Xử lý lỗi
							Swal.fire({
								icon: 'error',
								title: 'Lỗi!',
								text: 'Lỗi khôi phục voucher!',
							});
							console.log("Error", error);
						});
				} catch (error) {
					// Xử lý lỗi ngoại lệ
					Swal.fire({
						icon: 'error',
						title: 'Lỗi!',
						text: 'Lỗi khôi phục voucher',
					});
					console.log("Exception", error);
				}
			}
		});
	};

	// tìm kiếm
	$scope.loadData = function() {
		var apiUrl = '/rest/vouchers/searchkeywordvoucher';

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

	// tìm kiếm
	$scope.loadData1 = function() {
		var apiUrl = '/rest/vouchers/user/searchkeywordvoucheruser';

		// Kiểm tra xem có từ khóa tìm kiếm không
		if ($scope.searchText) {
			apiUrl += '?keyword=' + $scope.searchText;
		}

		$http.get(apiUrl)
			.then(function(response) {
				// Cập nhật dữ liệu trong scope
				$scope.items2 = response.data; // Cập nhật items để phản ánh dữ liệu mới
				$scope.pager1.page = 0; // Đặt lại trang về 0 khi có dữ liệu mới
			})
			.catch(function(error) {
				console.error('Lỗi khi tải dữ liệu:', error);
			});
	};

	$scope.pager = {
		page: 0,
		size: 5,
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

