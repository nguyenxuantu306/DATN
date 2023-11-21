app.controller("user-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.cates = [];
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	$scope.deletedItems = [];


	$scope.initialize = function() {
		// Load products
		$http.get("/rest/users").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.createddate = new Date(item.createddate);
				item.birthday = new Date(item.birthday);
			})
		});

		$http.get("/rest/users/deleted").then(resp => {
			$scope.deletedItems = resp.data;
		});

	}
	// Khởi đầu
	$scope.initialize();



	// validation ngày tạo
	$scope.isDateBeforeCreatedate = function() {
		var selectedDate = new Date($scope.form.createddate);
		var currentDate = new Date();
		return selectedDate < currentDate;
	};

	// validation ngày sinh
	$scope.isDateBeforeToday = function() {
		var selectedDate = new Date($scope.form.birthday);
		var currentDate = new Date();
		return selectedDate < currentDate;
	};


	$scope.isEdit = false; // Mặc định không ở chế độ edit

	$scope.reset = function() {
		$scope.form = {
			createddate: new Date(),
			birthday: new Date(),
			image: 'cloud-upload.jpg',
			gender: true,
			password: '' // Trường password sẽ được đặt về rỗng khi tạo mới
		};
		$scope.isEdit = false; // Chuyển về chế độ tạo mới
		$('#id').attr('readonly', false);
	}

	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		$scope.isEdit = true; // Chuyển về chế độ edit
	}

	// Hiện thị lên for
	$scope.editthemsp = function() {
		$scope.isEdit = false; // Chuyển về chế độ tạo mới
		$scope.form = {
			image: 'https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png'
		}
	}

	// Thêm mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/users`, item).then(resp => {
			resp.data.createddate = new Date(resp.data.createddate);
			resp.data.birthday = new Date(resp.data.birthday);
			$scope.items.push(resp.data);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Thêm tài khoản thành công!',
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
				text: 'Lỗi thêm mới tài khoản',
			});
			console.log("Error", error);
		});
	}


	// cập loại nhật sản phẩm
	$scope.update = function() {
		var item = angular.copy($scope.form);
		$http.put(`/rest/users/${item.userid}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.userid == item.userid);
			$scope.items[index] = item;
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Cập nhật thành công!',
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
					text: 'Lỗi Cập nhật',
				});
				console.log("Error", error);
			});
	}



	$scope.restore = function(userid) {
		try {
			axios.put(`/rest/users/${userid}/restore`)
				.then(response => {
					// Xử lý phản hồi thành công
					Swal.fire({
						icon: 'success',
						title: 'Thành công!',
						text: 'Khôi phục tài khoản thành công!',
					});
					// Load lại trang
					location.reload();
				})
				.catch(error => {
					// Xử lý lỗi
					Swal.fire({
						icon: 'error',
						title: 'Lỗi!',
						text: 'Lỗi khôi phục tài khoản!',
					});
					console.log("Error", error);
				});
		} catch (error) {
			// Xử lý lỗi ngoại lệ
			Swal.fire({
				icon: 'error',
				title: 'Lỗi!',
				text: 'Lỗi khôi phục tài khoản',
			});
			console.log("Exception", error);
		}
	};


	// Xóa loại sản phẩm 
	$scope.delete = function(item) {
		$http.delete(`/rest/users/${item.userid}`).then(resp => {
			var index = $scope.items.findIndex(p => p.userid == item.userid);
			$scope.items.splice(index, 1);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Xóa thành công!',
			});
			// Load lại trang
			location.reload();
		})
			.catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi user',
				});
				console.log("Error", error);
			});
	}

	// Upload hình
	$scope.imageChanged = function(files) {
		var data = new FormData();
		data.append('file', files[0]);
		$http.post('/rest/upload/images', data, {
			transformRequest: angular.identity,
			headers: { 'Content-Type': undefined }
		}).then(resp => {
			$scope.form.image = resp.data.name;
		}).catch(error => {
			alert("Lỗi upload hình ảnh");
			console.log("Error", error);
		})
	}


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


	// Trong AngularJS controller hoặc service
	$scope.exportExcel = function() {
		$http.get('/print-to-excel', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);

				link.download = 'exportUser.xlsx';

				link.download = 'userdata.xlsx';

				link.click();
			})
			.catch(function(error) {
				console.error('Error exporting Excel:', error);
			});
	};

	// PDF

	$scope.exportPdf = function() {
		$http.get('/export-to-pdf', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/pdf' });
				var objectUrl = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = objectUrl;
				a.download = 'exportUser.pdf';
				a.click();
				URL.revokeObjectURL(objectUrl);
			})
			.catch(function(error) {
				console.error('Error exporting PDF:', error);
			});
	};

	$scope.imageChanged = function(event) {
		var file = event.target.files[0];
		if (file) {
			var data = new FormData();
			data.append('file', file);
			$http.post('/api/images/upload', data, {
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined },
				//responseType: 'text'
			}).then(resp => {
				console.log('Upload success. Image URL:', resp.data.imageUrl);
				$scope.form.image = resp.data.imageUrl;
			}).catch(error => {
				alert("Lỗi upload hình ảnh");
				console.log("Error", error);
			})
		}
	};
});



