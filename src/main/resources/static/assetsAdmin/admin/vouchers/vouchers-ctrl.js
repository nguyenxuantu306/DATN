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
	}

	// Khởi đầu
	$scope.initialize();

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

