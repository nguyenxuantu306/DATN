app.controller('tytestatistics-ctrl', function($scope, $http) {
	$scope.form = {};
	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}
	$scope.itemsThongKeLoai = [];
	$scope.selectedCategoryProducts = []; // Danh sách sản phẩm thuộc category được chọn


	$scope.initialize = function() {
		$http.get('/rest/products/thongke/loai').then(response => {
			$scope.itemsThongKeLoai = response.data;
			console.log($scope.itemsThongKeLoai);
		}).catch(err => {
			console.log(err);
		})
	}



	$scope.pager = {
		page: 0,
		size: 5,
		get itemsThongKeLoai() {
			var start = this.page * this.size;
			return $scope.itemsThongKeLoai.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.itemsThongKeLoai.length
				/ this.size);
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
			if (this.page >= this.count) {
				this.first();
			}
		},
		last() {
			this.page = this.count - 1;
		},
	}
//
	// Sửa phương thức để hiển thị danh sách sản phẩm khi nhấn "Xem"
	$scope.showCategoryProducts = function(category) {
		$http.get('/rest/products/getProductsByCategory/' + category.categoryid).then(response => {
			$scope.selectedCategoryProducts = response.data;
			console.log($scope.selectedCategoryProducts)
			$('#exampleModal').modal('show');
		}).catch(err => {
			console.log(err);
		});
	};
	$scope.initialize();



	// Trong AngularJS controller hoặc service
	$scope.exportExcel = function() {
		$http.get('/excel-categorystatistics', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				link.download = 'categorystatistics.xlsx';
				link.click();
			})
			.catch(function(error) {
				console.error('Error exporting Excel:', error);
			});
	};

	// PDF

	$scope.exportPdf = function() {
		$http.get('/pdf-categorytatistics', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/pdf' });
				var objectUrl = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = objectUrl;
				a.download = 'exportCategoryStatistics.pdf';
				a.click();
				URL.revokeObjectURL(objectUrl);
			})
			.catch(function(error) {
				console.error('Error exporting PDF:', error);
			});
	};
})