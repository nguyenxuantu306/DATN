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
	
	$scope.editrecm = function(item) {
		console.log(item);
		$scope.form.recommenttext = angular.copy(item.recommenttext);
		console.log('them id vao comment');	
		
		$scope.form.recommentid = angular.copy(item.recommentid);
		$scope.form.comment = angular.copy(item.comment);
		console.log('them id vao comment'+item.recommentid);	
	}
	
	$scope.reset = function() {
		$scope.error = ['err'];
		$scope.form = {
			
			};
	};
	
		
	$scope.resetrecm = function() {
		$scope.form.recommenttext = null;
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
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Thêm comment thành công!',
			});
			console.log("cmthành công " ); 
		}).catch(error => {
			// Sử dụng SweetAlert2 cho thông báo lỗi
			Swal.fire({
				icon: 'error',
				title: 'Lỗi!',
				text: 'Lỗi thêm mới comment!',
			});
			console.log("cmt lỗi " ); 
			console.log("Error", error);
		});
	};


	// Cập nhật comment tour
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
					text: 'Cập nhật comment thành công!',
				});
			}).catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi cập nhật tour!',
				});
				console.log("Error", error);
				console.error("Error", error);
			});
		} else {
			alert("comment  không tồn tại");
		}
	}

	$scope.delete = function(item) {
		$http.delete(`/rest/comment/${item.commentid}`).then(resp => {
			var index = $scope.items.findIndex(p => p.commentid == item.commentid);
			$scope.items.splice(index, 1);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Xóa comment thành công!',
			});

		})
			.catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi xóa comment!',
				});
				console.log("Error", error);
			});
	}
	
	
	// Thêm recomment  mới
	$scope.createrecomment = function(item) {
		
		/*console.log("cmt user " +$scope.loggedInUser); 
		console.log("cmt user check" +$scope.users.userid); */ 
		console.log("coi le day la comment :"+item);
		console.log("********* "); 
		$scope.form.tour = $scope.tour;
		console.log("cmtid tua dya nhe " + $scope.tour); 
		$scope.form.user = $scope.users ;
		
		$scope.form.comment = item;
		
		
		$scope.form.recommenttext = $scope.form.recommenttext;
		console.log("cmt " +$scope.form.recommenttext); 
		$scope.form.commentdate = new Date();

		var item = {};
		 item = angular.copy($scope.form);
			
			console.log("cmt tour id " +$scope.tour.tourid); 
			item.user = $scope.users ;
		$http.post(`/rest/recomment`, $scope.form).then(resp => {
			
			resp.data.recommentdate = new Date(resp.data.recommentdate);
			/*$scope.items.push(resp.data);*/
			$scope.items.forEach(function(item) {
  // Kiểm tra xem mảng con recomment có tồn tại không
  if (item.recomment) {
    // Thêm dữ liệu mới vào mảng recomment
    item.recomment.push(resp.data);
    $scope.initialize();
  } /*else {
    // Nếu mảng recomment chưa tồn tại, bạn có thể tạo nó
    item.recomment = [resp.data];
  }*/
});
			console.log("du lieu"+$scope.items);
			$scope.resetrecm();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Thêm comment thành công!',
			});
			console.log("cmthành công " ); 
		}).catch(error => {
			// Sử dụng SweetAlert2 cho thông báo lỗi
			Swal.fire({
				icon: 'error',
				title: 'Lỗi!',
				text: 'Lỗi thêm mới comment!',
			});
			console.log("comment lỗi " ); 
			console.log("day la data"+$scope.items);
			console.log("Error", error);
		});
	};


// Cập nhật comment tour
	$scope.updaterecm = function(item) {
			
		var item = angular.copy($scope.form);
		item.comment = $scope.form.comment;
			item.user =  $scope.users ;
		if (item.recommentid == null) {
			
			return $scope.createrecomment();
		} else if (item.recommentid != null) {
				console.log("day la recm id ow comment:"+item.recommentid, item );
				
			$http.put(`/rest/recomment/${item.recommentid}`, item).then(resp => {
				var index = $scope.items.findIndex(recm => recm.recommentid == item.recommentid);
				$scope.items[index] = item;
				$scope.reset();
				  $scope.initialize();
				// Sử dụng SweetAlert2 cho thông báo thành công
				Swal.fire({
					icon: 'success',
					title: 'Thành công!',
					text: 'Cập nhật recomment thành công!',
				});
			}).catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi cập nhật recomment!',
				});
				console.log("Error", error);
			});
		} else {
			alert("recomment không tồn tại");
		}
	}


	$scope.deleterecm = function(item) {
		$http.delete(`/rest/recomment/${item.recommentid}`).then(resp => {
			var index = $scope.items.findIndex(recm => recm.recommentid == item.recommentid);
			$scope.items.splice(index, 1);
			$scope.reset();
			$scope.initialize();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Xóa comment thành công!',
			});

		})
			.catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi xóa comment!',
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
		
    // Hàm tính toán khoảng thời gian
    $scope.formatTimeAgo = function (date) {
        const now = new Date();
        const diffInSeconds = Math.floor((now - date) / 1000);

        const intervals = {
            year: 31536000,
            month: 2592000,
            week: 604800,
            day: 86400,
            hour: 3600,
            minute: 60,
            second: 1
        };

        for (const [unit, secondsInUnit] of Object.entries(intervals)) {
            const interval = Math.floor(diffInSeconds / secondsInUnit);
            if (interval >= 1) {
                return interval + ' ' + (interval === 1 ? unit : unit + 's') + ' ago';
            }
        }

        return 'Vừa xong';
    };

		
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
