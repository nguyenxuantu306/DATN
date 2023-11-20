app.controller("product-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.deletedItems = [];
	$scope.cates = [];
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	var deletedProductList = [];

	$scope.initialize = function() {
		// Load products
		$http.get("/rest/products").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.publication_date = new Date(item.publication_date)
			})
		});

		// Load deleted products
		$http.get("/rest/products/deleted").then(resp => {
			$scope.deletedItems = resp.data;
		});

		// Load categories
		$http.get("/rest/categories").then(resp => {
			$scope.cates = resp.data;

		});



	}

	//		$scope.getDeletedList = function() {
	//			$http.get("/rest/products/deleted").then(function(resp) {
	//				$scope.deletedItems = resp.data;
	//			});
	//		};



	// Ban đầu, searchTerm rỗng
	$scope.searchTerm = '';

	// Hàm lọc sản phẩm
	$scope.filterProducts = function(product) {
		// Kiểm tra nếu searchTerm là chuỗi con của tên sản phẩm
		return product.productname.toLowerCase().includes($scope.searchTerm.toLowerCase());
	};

	$scope.formatCurrency = function(event) {
		// get input value
		var input = event.target;
		var value = input.value;
		value = value.replace(/[^0-9]/g, '');
		value = value.replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
		input.value = value;
	}

	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}

	// Khởi đầu
	$scope.initialize();

	// Xóa form
	$scope.reset = function() {
		$scope.error = ['err'];
		$scope.form = {
			image: 'https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png',
		};
	}

	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
	}

	// Hiện thị lên for
	$scope.editthemsp = function() {
		$scope.form = angular.copy();
		$scope.form = {
			image: 'https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png',
		};
	}

	// Thêm sản phẩm mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/products`, item).then(resp => {
			resp.data.publication_date = new Date(resp.data.publication_date)
			$scope.items.push(resp.data);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Thêm sản phẩm thành công!',
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
				text: 'Lỗi thêm sản phẩm',
			});
			console.log("Error", error);
		});
	}


	// Cập nhật sản phẩm
	$scope.update = function() {
		var item = angular.copy($scope.form);
		$http.put(`/rest/products/${item.productid}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.productid == item.productid);
			$scope.items[index] = item;
			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Cập nhật sản phẩm thành công!',
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
					text: 'Lỗi cập nhật sản phẩm',
				});
				console.log("Error", error);
			});
	}


	$scope.restore = function(productid) {
		try {
			axios.put(`/rest/products/${productid}/restore`)
				.then(response => {
					// Xử lý phản hồi thành công
					Swal.fire({
						icon: 'success',
						title: 'Thành công!',
						text: 'Khôi phục sản phẩm thành công!',
					});
					// Load lại trang
					location.reload();
				})
				.catch(error => {
					// Xử lý lỗi
					Swal.fire({
						icon: 'error',
						title: 'Lỗi!',
						text: 'Lỗi khôi phục sản phẩm',
					});
					console.log("Error", error);
				});
		} catch (error) {
			// Xử lý lỗi ngoại lệ
			Swal.fire({
				icon: 'error',
				title: 'Lỗi!',
				text: 'Lỗi khôi phục sản phẩmmmm',
			});
			console.log("Exception", error);
		}
	};




	// Xóa sản phẩm 
	$scope.delete = function(item) {
		$http.delete(`/rest/products/${item.productid}`)
			.then(resp => {
				// Xóa sản phẩm khỏi danh sách hiển thị
				var index = $scope.items.findIndex(p => p.productid == item.productid);
				$scope.items.splice(index, 1);

				// Hiển thị thông báo thành công
				Swal.fire({
					icon: 'success',
					title: 'Thành công!',
					text: 'Xóa sản phẩm thành công!',
				});
				// Load lại trang
				//	location.reload();
			})
			.catch(error => {
				// Hiển thị thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Lỗi xóa sản phẩm',
				});
				console.log("Error", error);
			});
	};



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
		$http.get('/excel-product', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				link.download = 'product.xlsx';
				link.click();
			})
			.catch(function(error) {
				console.error('Error exporting Excel:', error);
			});
	};
	// PDF

	$scope.exportPdf = function() {
		$http.get('/pdf-product', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/pdf' });
				var objectUrl = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = objectUrl;
				a.download = 'product.pdf';
				a.click();
				URL.revokeObjectURL(objectUrl);
			})
			.catch(function(error) {
				console.error('Error exporting PDF:', error);
			});
	};

	// Hàm định dạng giá tiền
	$scope.formatPrice = function(price) {
		var priceString = price.toString();
		priceString = priceString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		return priceString + " đ";
	}


	/*// Sử dụng AngularJS để lắng nghe sự kiện khi người dùng chọn tệp
	$scope.selectImage = function() {
		var fileInput = document.getElementById('imageUpload');
		fileInput.click();
	};
	
	// Xử lý sự kiện khi người dùng chọn tệp
	document.getElementById('imageUpload').addEventListener('change', function(event) {
		var imagePreview = document.getElementById('imagePreview');
		var file = event.target.files[0];
	
		if (file) {
			var reader = new FileReader();
	
			reader.onload = function(e) {
				imagePreview.src = e.target.result;
				imagePreview.style.display = 'block';
			};
	
			reader.readAsDataURL(file);
		} else {
			// Nếu người dùng không chọn tệp, ẩn hình ảnh
			imagePreview.src = '';
			imagePreview.style.display = 'none';
		}
	});
	
	// Xử lý sự kiện khi trường nhập URL mất focus (ng-blur)
	$scope.updateImageFromUrl = function() {
		var imageUrl = $scope.form.imageUrl;
		var imagePreview = document.getElementById('imagePreview');
	
		if (imageUrl) {
			imagePreview.src = imageUrl;
			imagePreview.style.display = 'block';
		} else {
			// Nếu URL trống, ẩn hình ảnh
			imagePreview.src = '';
			imagePreview.style.display = 'none';
		}
	};*/

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
