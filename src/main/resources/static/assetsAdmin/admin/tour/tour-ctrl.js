app.controller("tour-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.form = {
		tourImage: [{ imageurl: '' }]
	};
	$scope.field = [];
	$scope.error = ['err'];
	$scope.item2 = [];
	$scope.deletedItems = [];

	console.log($scope.form.tourImage);


	$scope.formatPrice = function(price) {
		var priceString = price.toString();
		priceString = priceString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		return priceString + " đ";
	}

	$scope.initialize = function() {
		// Load tours
		$http.get("/rest/tours").then(resp => {
			$scope.items = resp.data;
		});
		$http.get("/rest/tours/images").then(resp => {
			$scope.item2 = resp.data;
		});

		// Load deleted tours
		$http.get("/rest/tours/deleted").then(resp => {
			$scope.deletedItems = resp.data;
		});
	}


	// Khởi đầu
	$scope.initialize();

	$scope.idi = {};

	/*//Thêm cái image khác
	$scope.addImage = function() {
		$scope.form.tourImage.push({
			imageurl: ''
		});
	};
	// Hiện thị lên form
	$scope.edit = function(item) {
		for (var i = 0; i < item.tourImage.length; i++) {
			item.tourImage[i].delete = false;
		}
		$scope.form = angular.copy(item);
		$scope.idi = item.tourid;
	}
	//Loại bỏ các image
	$scope.removeImage = function(index) {
		// Loại bỏ TourImage khỏi form
		$scope.form.tourImage.splice(index, 1);
	};
*/
	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		var imageUrls = item.tourImage.map(function(tourImage) {
			return tourImage.imageurl;
		});

		$scope.showEditImages(imageUrls);

	}

	//Hiển thị các hình ảnh sản phẩm khác - productImage
	$scope.showEditImages = function(imageUrls) {
		var editImagePreviewContainer = document.getElementById('edit-image-preview-container');
		editImagePreviewContainer.innerHTML = '';

		for (var i = 0; i < imageUrls.length; i++) {
			var img = document.createElement('img');
			img.src = imageUrls[i];
			img.style.maxWidth = '50px';
			img.style.height = '50px';
			editImagePreviewContainer.appendChild(img);
		}
	};


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
			$scope.form = {}; // Hoặc thực hiện các bước cần thiết để reset form
			$scope.frmvalidateadd.$setPristine();
			$scope.frmvalidateadd.$setUntouched();
			$scope.frmvalidateadd.$submitted = false;
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
				$scope.form = {}; // Hoặc thực hiện các bước cần thiết để reset form
				$scope.frmvalidateupdate.$setPristine();
				$scope.frmvalidateupdate.$setUntouched();
				$scope.frmvalidateupdate.$submitted = false;
				$scope.edit(item)

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

	$scope.restore = function(tourid) {
    // Hiển thị cửa sổ xác nhận trước khi khôi phục
    Swal.fire({
        title: 'Xác nhận khôi phục',
        text: 'Bạn có chắc chắn muốn khôi phục địa điểm này?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Hủy bỏ'
    }).then((result) => {
        // Kiểm tra xem người dùng đã bấm nút "Đồng ý" hay không
        if (result.isConfirmed) {
            // Nếu đã bấm "Đồng ý", thực hiện khôi phục
            try {
                axios.put(`/rest/tours/${tourid}/restore`)
                    .then(response => {
                        // Xử lý phản hồi thành công
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công!',
                            text: 'Khôi phục địa điểm thành công!',
                        }).then((result) => {
                            // Kiểm tra xem người dùng đã bấm nút "OK" hay chưa
                            if (result.isConfirmed) {
                                // Nếu đã bấm, thực hiện reload trang
                                location.reload();
                            }
                        });
                    })
                    .catch(error => {
                        // Xử lý lỗi
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: 'Lỗi khôi phục địa điểm!',
                        });
                        console.log("Error", error);
                    });
            } catch (error) {
                // Xử lý lỗi ngoại lệ
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Lỗi khôi phục địa điểm',
                });
                console.log("Exception", error);
            }
        }
    });
};


	$scope.delete = function(item) {
		// Hiển thị cửa sổ xác nhận trước khi xóa
		Swal.fire({
			title: 'Xác nhận xóa',
			text: 'Bạn có chắc chắn muốn xóa tour này?',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: 'Đồng ý',
			cancelButtonText: 'Hủy bỏ'
		}).then((result) => {
			// Kiểm tra xem người dùng đã bấm nút "Đồng ý" hay không
			if (result.isConfirmed) {
				// Nếu đã bấm "Đồng ý", thực hiện xóa
				$http.delete(`/rest/tours/${item.tourid}`).then(resp => {
					var index = $scope.items.findIndex(p => p.tourid == item.tourid);
					$scope.items.splice(index, 1);
					$scope.reset();

					// Hiển thị thông báo thành công
					Swal.fire({
						icon: 'success',
						title: 'Thành công!',
						text: 'Xóa tour thành công!',
					}).then((result) => {
						// Kiểm tra xem người dùng đã bấm nút "OK" hay chưa
						if (result.isConfirmed) {
							// Nếu đã bấm, thực hiện reload trang
							location.reload();
						}
					});
				}).catch(error => {
					// Hiển thị thông báo lỗi
					Swal.fire({
						icon: 'error',
						title: 'Lỗi!',
						text: 'Lỗi xóa tour!',
					});
					console.log("Error", error);
				});
			}
		});
	};

	function refreshPage() {
		location.reload();
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


	// Trong AngularJS controller hoặc service
	$scope.exportExcel = function() {
		$http.get('/excel-tour', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				link.download = 'tour.xlsx';
				link.click();
			})
			.catch(function(error) {
				console.error('Error exporting Excel:', error);
			});
	};

	// PDF

	$scope.exportPdf = function() {
		$http.get('/pdf-tour', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/pdf' });
				var objectUrl = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = objectUrl;
				a.download = 'exportTour.pdf';
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

	$scope.imagesChanged = function(event) {
		var files = event.target.files;
		if (files && files.length > 0) {
			var data = new FormData();
			var imagePreviewContainer = document.getElementById('image-preview-container');
			imagePreviewContainer.innerHTML = '';

			for (var i = 0; i < files.length; i++) {
				data.append('files', files[i]);
				var file = files[i];
				var reader = new FileReader();

				reader.onload = function(e) {
					var img = document.createElement('img');
					img.src = e.target.result;
					img.style.maxWidth = '50px';
					img.style.height = '50px';
					imagePreviewContainer.appendChild(img);
				};
				reader.readAsDataURL(file);
			}

			$http.post('/api/images/upload/multipartfile', data, {
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined }
			}).then(resp => {
				console.log('Upload success. Image URLs:', resp.data.imageUrls);

				// Assume resp.data.imageUrls is an array of image URLs
				$scope.form.tourImage = resp.data.imageUrls;
			}).catch(error => {
				alert("Lỗi upload hình ảnh");
				console.log("Error", error);
			});
		}
	};


});