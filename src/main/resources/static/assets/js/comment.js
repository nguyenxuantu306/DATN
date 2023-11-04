var commentapp = angular.module('commentApp', ['ngRoute']);

commentapp.config(function($routeProvider) {
  $routeProvider
    .when("/tour/detail/:tourid", {
      controller: 'comment-ctrl'
    });
});
commentapp.controller("comment-ctrl", function($scope, $http, $routeParams) {
	$scope.items = [];
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	$scope.tourId =  $routeParams.tourid;
	
	


	/* $scope.tourId;*/
	
	 
	$scope.initialize = function() {
	
		console.log("tourId: " + $scope.tourId); 
	
		
		
		
		// Load comment
		$http.get("/rest/comment/tour/1").then(resp => {
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
	}

	$scope.reset = function() {
		$scope.error = ['err'];
		$scope.form = {
			image: 'https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png',
		};
	};

	// Hiện thị lên for
	$scope.editthemsp = function(){	
		$scope.form = angular.copy();
		$scope.form = {			
			image:'https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png',			
		};			
	}

	// Thêm tour phẩm mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/comment`, item).then(resp => {
			
			resp.data.commentdate = new Date(resp.data.commentdate);
			$scope.items.push(resp.data);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Thêm tour thành công!',
			});
		}).catch(error => {
			// Sử dụng SweetAlert2 cho thông báo lỗi
			Swal.fire({
				icon: 'error',
				title: 'Lỗi!',
				text: 'Lỗi thêm mới tour!',
			});
			console.log("Error", error);
		});
	};


	// Cập nhật tour
	$scope.update = function() {
		var item = angular.copy($scope.form);
		if (item.commentid == null) {
			return $scope.create();
		} else if (item.commentid != null) {
			$http.put(`/rest/comment/${item.commentid}`, item).then(resp => {
				var index = $scope.items.findIndex(p => p.commentid == item.commentid);
				$scope.items[index] = item;
				$scope.reset();
				// Sử dụng SweetAlert2 cho thông báo thành công
				Swal.fire({
					icon: 'success',
					title: 'Thành công!',
					text: 'Cập nhật tour thành công!',
				});
			}).catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi cập nhật tour!',
				});
				console.log("Error", error);
			});
		} else {
			alert("Tour id không tồn tại");
		}
	}

	$scope.delete = function(item) {
		$http.delete(`/rest/comment/${item.commentid}`).then(resp => {
			var index = $scope.items.findIndex(p => p.commentid == item.commentid);
			$scope.items.splice(index, 1);
			//$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Xóa tour thành công!',
			});

		})
			.catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi xóa tour!',
				});
				console.log("Error", error);
			});
	}

	/*	// Upload hình	
		$scope.imageChanged = function(files) {
			var data = new FormData();
			data.append('file', files[0]);
			$http.post('/rest/upload/images', data, {
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined }
			}).then(resp => {
				$scope.form.tourimage = resp.data.name;
			}).catch(error => {
				alert("Lỗi upload hình ảnh");
				console.log("Error", error);
			})
		}*/
	$scope.pager = {
		page: 0,
		size: 7,
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


