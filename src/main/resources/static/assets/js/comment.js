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
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	$scope.tour = itemData;
	 var tourid = $routeParams.tourid;
	 // $scope.tour = null;

	/* $scope.tourId;*/
	
	 
	$scope.initialize = function() {
	
	
		console.log("tourId: " + $scope.tour.tourid); 
		
		
		// Load comment
		$http.get("/rest/comment/tour/"+ + $scope.tour.tourid).then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.commentdate = new Date(item.commentdate)
			})
		});
		
		/*console.log("tourId2: " + tourid); 
		  
$http.get('/rest/tours/' + tourid)
        .then(function(response) {
            $scope.tour = response.data;
        })
        .catch(function(error) {
            console.log(error);
        });*/
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


// Thêm comment  mới
	$scope.create = function() {
		$scope.form.tour = $scope.tour;
		$scope.form.user = $scope.tour.user;
		$scope.form.commenttext = $scope.form.commenttext;
		console.log("cmt " +$scope.form.commenttext); 
		$scope.form.commentdate = new Date();

		var item = {};
		 item = angular.copy($scope.form);
			
			console.log("cmt tour id " +$scope.tour.tourid); 
			item.user = $scope.tour.user;
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
