var commentapp = angular.module('commentApp', ['ngRoute']);

commentapp.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
  $routeProvider
    .when('/tour/detail/:tourid', {
      
      controller: 'comment-ctrl'
    });

  
  $locationProvider.html5Mode({
  enabled: true,
  requireBase: false
});
}]);


commentapp.controller("comment-ctrl", function($scope, $http, $routeParams) {
	$scope.items = [];
	$scope.listrecm = [];
	$scope.users = {};
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	$scope.tour = itemData;
	$scope.loggedInUser = uslog;
	 
	 // $scope.tour = null;

	/* $scope.tourId;*/
	// Lấy thông tin người dùng đăng nhập
    //var loggedInUser = 'email@example.com'; // Thay thế bằng email của người dùng đăng nhập
    
    // Kiểm tra nếu người dùng đang xem là người dùng đăng nhập
    $scope.isUserLoggedIn = function (email) {
        return email === uslog;
    };
	 
	$scope.initialize = function() {
	
	
		console.log("tourId: " + $scope.users.email); 
		console.log("tourId: " + $scope.tour.tourid); 
		console.log("tourId: " + $scope.loggedInUser); 
		
		// Load comment
		$http.get("/rest/comment/tour/"+ + $scope.tour.tourid).then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				/*recomment*/
				$http.get("/rest/recomment/comment/"+ +item.commentid).then(resp => {
			$scope.listrecm = resp.data;
			$scope.listrecm.forEach(item => {
				console.log("recm id: " + item.recommentid); 
				console.log("recm id: " + item.recommenttext); 
				item.recommentdate = new Date(item.recommentdate)
			})
		});
				console.log("cmid: " + item.commentid); 
				console.log("cmid: " + item.commenttext); 
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
			
			};
	};

	// Hiện thị lên for
	$scope.editthemsp = function(){	
		$scope.form = angular.copy();
		$scope.form = {			
			image:'https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png',			
		};			
	}
	
$http.get("/rest/users/email/" + $scope.loggedInUser).then(resp => {
			$scope.users = resp.data;
			
		});

// Thêm comment  mới
	$scope.create = function() {
		
		/*console.log("cmt user " +$scope.loggedInUser); 
		console.log("cmt user check" +$scope.users.userid); */
		$scope.form.tour = $scope.tour;
		$scope.form.user = $scope.users ;
		$scope.form.commenttext = $scope.form.commenttext;
		console.log("cmt " +$scope.form.commenttext); 
		$scope.form.commentdate = new Date();

		var item = {};
		 item = angular.copy($scope.form);
			
			console.log("cmt tour id " +$scope.tour.tourid); 
			item.user = $scope.users ;
		$http.post(`/rest/comment`, $scope.form).then(resp => {
			
			resp.data.commentdate = new Date(resp.data.commentdate);
			console.log("cmt đang hành công " ); 
			$scope.items.push(resp.data);
			$scope.reset();
			
			// Sử dụng SweetAlert2 cho thông báo thành công
			/*Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Thêm tour thành công!',
			});*/
			console.log("cmthành công " ); 
		}).catch(error => {
			// Sử dụng SweetAlert2 cho thông báo lỗi
			/*Swal.fire({
				icon: 'error',
				title: 'Lỗi!',
				text: 'Lỗi thêm mới tour!',
			});*/
			console.log("cmt lỗi " ); 
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
