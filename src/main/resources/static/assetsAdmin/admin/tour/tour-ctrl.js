app.controller("tour-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.form = {
		tourImage: [{ imageurl: '' }]
	};
	$scope.field = [];
	$scope.error = ['err'];
	$scope.item2 = [];

	console.log($scope.form.tourImage);
	//Thêm cái image khác
	$scope.addImage = function() {
		$scope.form.tourImage.push({
			imageurl: ''
		});
	};

	$scope.formatPrice = function(price) {
		var priceString = price.toString();
		priceString = priceString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		return priceString + " đ";
	}



	$scope.initialize = function() {
		// Load tours
		$http.get("/rest/tours").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.startdate = new Date(item.startdate),
					item.enddate = new Date(item.enddate)
			})
		});
		$http.get("/rest/tours/images").then(resp => {
			$scope.item2 = resp.data;
		});
	}
	// Khởi đầu
	$scope.initialize();

	$scope.idi = {};
	// Hiện thị lên form
	$scope.edit = function(item) {
		for (var i = 0; i < item.tourImage.length; i++) {
			item.tourImage[i].delete = false;
		}
		$scope.form = angular.copy(item);
		$scope.idi = item.tourid;
		//		$scope.form.tourImage = angular.copy(item.tourImage);
		console.log($scope.form);
	}


	// Reset form
	$scope.reset = function() {
		$scope.error = ['err'];
		$scope.form = {
			image: 'https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png',
		};
	};

	// Hiện thị lên for
	$scope.editthemsp = function() {
		$scope.error = ['err'];
		$scope.form = {
			image: 'https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png',
			tourImage: [{ imageurl: '' }]
		};
	}

	// Thêm tour mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/tours`, item).then(resp => {
			resp.data.startdate = new Date(resp.data.startdate);
			resp.data.enddate = new Date(resp.data.enddate);
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

	//Loại bỏ các image
	$scope.removeImage = function(index) {
		// Loại bỏ TourImage khỏi form
		$scope.form.tourImage.splice(index, 1);
	};

	// Cập nhật tour
	$scope.update = function() {
		var item = angular.copy($scope.form);
		if (item.tourid != null) {
			if (item.tourImage) {
				item.tourImage.forEach(imageurl => {
					delete imageurl.tourid;
				});
			}
			$http.put(`/rest/tours/${item.tourid}`, item).then(resp => {
				var index = $scope.items.findIndex(p => p.tourid == item.tourid);
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
		$http.delete(`/rest/tours/${item.tourid}`).then(resp => {
			var index = $scope.items.findIndex(p => p.tourid == item.tourid);
			$scope.items.splice(index, 1);
			$scope.reset();
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


	/*	$scope.formatCurrency = function(event) {
			// get input value
			var input = event.target;
			var value = input.value;
	
			// replace non-digit characters with empty string
			value = value.replace(/[^0-9]/g, '');
	
			// format the value using regex
			value = value.replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
	
			// update the input value
			input.value = value;
		}*/
});


