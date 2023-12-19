app.controller("message-ctrl", function($scope, $http, $window) {
	$scope.items = [];
	$scope.cates = [];
	$scope.items2 = [];
	$scope.form = {};
	$scope.products = [];
	$scope.selectedItem = null;
	$scope.totalPrice = 0;
	$scope.loggedInUser = [];


	$scope.initialize = function() {
		// Load products
		$http.get("/rest/comment").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.commentdate = new Date(item.commentdate)
			})
		});
	}

	// Khởi đầu
	$scope.initialize();

	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		$scope.form.commentid = item.commentid;
	}

	$scope.checkAndReply = function () {
        // Kiểm tra xem nội dung bình luận có rỗng hay không
        if (!$scope.form.recommenttext) {
            // Hiển thị thông báo cảnh báo nếu rỗng
            Swal.fire({
                icon: 'warning',
                title: 'Vui lòng nhập nội dung trả lời trước khi gửi.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: 'OK'
            });

            // Dừng việc thực hiện tiếp
            return;
        }

        // Nếu bình luận không rỗng, tiếp tục gọi hàm update()
        $scope.update();
    };

	//Thêm recomments
	$scope.update = function() {
		$http.get("/rest/users/email").then(resp => {
			$scope.loggedInUser = resp.data;

			// Create a new object with the required data
			var requestData = {
				recommenttext: $scope.form.recommenttext,
				user: $scope.loggedInUser,
				comment: $scope.form,
				recommentdate: new Date()
			};

			console.log("Data to be sent:", requestData);

			$http.post(`/rest/recomment`, requestData).then(resp => {
				resp.data.recommentdate = new Date(resp.data.recommentdate);
				
				
				// Sử dụng SweetAlert2 cho thông báo thành công
				Swal.fire({
					icon: 'success',
					title: 'Thành công!',
					text: 'Trả lời bình luận thành công!',
				});
				location.reload();
			})
				.catch(error => {
					// Sử dụng SweetAlert2 cho thông báo lỗi
					Swal.fire({
						icon: 'error',
						title: 'Lỗi!',
						text: 'Trả lời bình luận thất bại!',
					});
					console.log("Error", error);
				});
		});
	}



	//Xóa comment
	$scope.delete = function(item) {
		// Hiển thị cửa sổ xác nhận trước khi xóa
		Swal.fire({
			title: 'Xác nhận xóa',
			text: 'Bạn có chắc chắn muốn xóa bình luận này?',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: 'Đồng ý',
			cancelButtonText: 'Hủy bỏ'
		}).then((result) => {
			// Kiểm tra xem người dùng đã bấm nút "Đồng ý" hay không
			if (result.isConfirmed) {
				// Nếu đã bấm "Đồng ý", thực hiện xóa
				$http.delete(`/rest/comment/${item.commentid}`).then(resp => {
					var index = $scope.items.findIndex(p => p.commentid == item.commentid);
					$scope.items.splice(index, 1);

					// Hiển thị thông báo thành công
					Swal.fire({
						icon: 'success',
						title: 'Thành công!',
						text: 'Xóa bình luận thành công!',
					});
				}).catch(error => {
					// Hiển thị thông báo lỗi
					Swal.fire({
						icon: 'error',
						title: 'Lỗi!',
						text: 'Lỗi xóa bình luận!',
					});
					console.log("Error", error);
				});
			}
		});
	};


	$scope.gotoComment = function(item) {
		console.log(item.commentid);
		var url = '/tour/detail/' + item.tour.tourid + '#comment-' + item.commentid;
		$window.open(url, '_blank');
	}

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

});

