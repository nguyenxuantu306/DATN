app.controller("category-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.cates = [];
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	$scope.deletedItems = [];


	$scope.searchCategory = '';

	$scope.initialize = function() {
		// Load products
		$http.get("/rest/categories").then(resp => {
			$scope.items = resp.data;
		});

		// Load deleted categories
		$http.get("/rest/categories/deleted").then(resp => {
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
		$('#btn-create').removeAttr('disabled');
		$('#btn-update').attr('disabled', 'disabled');
		$('#btn-delete').attr('disabled', 'disabled');
	}

	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
	}

	// Hiện thị lên for
	$scope.editthemsp = function() {
		$scope.form = angular.copy();
	}


	// Thêm loại sản phẩm mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/categories`, item).then(resp => {
			resp.data.publication_date = new Date(resp.data.publication_date)
			$scope.items.push(resp.data);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Thêm danh mục sản phẩm thành công!',
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
				text: 'Lỗi thêm danh mục sản phẩm',
			});
			console.log("Error", error);
		});
	}


	// cập loại nhật sản phẩm
	$scope.update = function() {
		var item = angular.copy($scope.form);
		$http.put(`/rest/categories/${item.categoryid}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.categoryid == item.categoryid);
			$scope.items[index] = item;
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Cập nhật danh mục thành công!',
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
					text: 'Lỗi Cập nhật danh mục sản phẩm',
				});
				console.log("Error", error);
			});
	}


	$scope.restore = function(categoryid) {
    // Hiển thị cửa sổ xác nhận trước khi khôi phục
    Swal.fire({
        title: 'Xác nhận khôi phục',
        text: 'Bạn có chắc chắn muốn khôi phục danh mục này?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Hủy bỏ'
    }).then((result) => {
        // Kiểm tra xem người dùng đã bấm nút "Đồng ý" hay không
        if (result.isConfirmed) {
            // Nếu đã bấm "Đồng ý", thực hiện khôi phục
            try {
                axios.put(`/rest/categories/${categoryid}/restore`)
                    .then(response => {
                        // Xử lý phản hồi thành công
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công!',
                            text: 'Khôi phục danh mục thành công!',
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
                            text: 'Lỗi khôi phục danh mục!',
                        });
                        console.log("Error", error);
                    });
            } catch (error) {
                // Xử lý lỗi ngoại lệ
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Lỗi khôi phục danh mục',
                });
                console.log("Exception", error);
            }
        }
    });
};


	
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
				$http.delete(`/rest/categories/${item.categoryid}`).then(resp => {
					var index = $scope.items.findIndex(p => p.categoryid == item.categoryid);
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





	$scope.pager = {
		page: 0,
		size: 5,
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

