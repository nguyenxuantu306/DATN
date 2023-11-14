app.controller("message-ctrl", function($scope, $http, $window) {
	$scope.items = [];
	$scope.cates = [];
	$scope.items2 = [];
	$scope.form = {};
	$scope.products = [];
	$scope.selectedItem = null;
	$scope.totalPrice = 0;


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

	// Xóa sản phẩm 
	$scope.delete = function(item) {
		$http.delete(`/rest/comment/${item.commentid}`).then(resp => {
			var index = $scope.items.findIndex(p => p.commentid == item.commentid);
			$scope.items.splice(index, 1);
			//$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Xóa thông báo thành công!',
			});
		})
			.catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi xóa thông báo!',
				});
				console.log("Error", error);
			});
	}

	$scope.gotoComment = function(item) {
		console.log(item.commentid);
		var url = '/tour/detail/' + item.tour.tourid + '#comment-' + item.commentid;
		$window.open(url, '_blank');
	}
	
	$scope.pager = {
		page:0,
		size:5,
		get items(){
			var start = this.page*this.size;
			 return $scope.items.slice(start,start + this.size);
		},
		get count(){
			return Math.ceil(1.0 * $scope.items.length / this.size);
		},
		first(){
			this.page = 0;
		},
		prev(){
			this.page--;
			if(this.page < 0){
				this.last();
			}
		},
		next(){
			this.page++;
			if(this.page > this.count){
				this.first();
			}
		},
		last(){
			this.page = this.count-1;
		}		
	}	

});

