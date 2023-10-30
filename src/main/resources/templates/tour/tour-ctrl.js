app.controller("tour-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];

	$scope.initialize = function() {
		// Load tours
		$http.get("/rest/tours").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.startdate = new Date(item.startdate),
					item.enddate = new Date(item.enddate)
			})
		});

	}
	// Khởi đầu
	$scope.initialize();



	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
	};

	$scope.reset = function() {
		$scope.error = ['err'];
		$scope.form = {
			publication_date: new Date(),
			image: 'cloud-upload.jpg',
			available: true,
		};
		$('#id').attr('readonly', false);
		$('#btn-create').removeAttr('disabled');
		$('#btn-update').attr('disabled', 'disabled');
		$('#btn-delete').attr('disabled', 'disabled');
	};


	// Thêm tour phẩm mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/tours`, item).then(resp => {
			resp.data.startdate = new Date(resp.data.startdate);
			resp.data.enddate = new Date(resp.data.enddate);
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm tour thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới tour!");
			console.log("Error", error);
		});
	};


	// Cập nhật tour
	$scope.update = function() {
		var item = angular.copy($scope.form);
		if (item.tourid == null) {
			return $scope.create();
		} else if (item.tourid != null) {
			$http.put(`/rest/tours/${item.tourid}`, item).then(resp => {
				var index = $scope.items.findIndex(p => p.tourid == item.tourid);
				$scope.items[index] = item;
				$scope.reset();
				alert("Cập nhật tour thành công!");
			}).catch(error => {
				alert("Lỗi cập nhật tour!");
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
			alert("Xóa tour thành công!");
		})
			.catch(error => {
				alert("Lỗi xóa tour!");
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


});


