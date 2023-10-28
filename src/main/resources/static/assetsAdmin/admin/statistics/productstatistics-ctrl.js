app.controller('productstatistics-ctrl', function($scope, $http) {

	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}
	$scope.form = {};
	$scope.itemsThongKeSp = [];

	$scope.initialize = function() {
		$http.get('/rest/products/thongke/sp').then(response => {
			$scope.itemsThongKeSp = response.data;
			console.log($scope.itemsThongKeSp);
		}).catch(err => {
			console.log(err);
		})
	}
	$scope.pager = {
		page: 0,
		size: 5,
		get itemsThongKeSp() {
			var start = this.page * this.size;
			return $scope.itemsThongKeSp.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.itemsThongKeSp.length
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
	$scope.initialize();
<<<<<<< Updated upstream
=======


>>>>>>> Stashed changes
// Trong AngularJS controller hoặc service
	$scope.exportExcel = function() {
		$http.get('/excel-productstatistics', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				link.download = 'productstatistics.xlsx';
				link.click();
			})
			.catch(function(error) {
				console.error('Error exporting Excel:', error);
			});
	};
	
	// PDF
		
		$scope.exportPdf = function() {
		$http.get('/pdf-productstatistics', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/pdf' });
				var objectUrl = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = objectUrl;
<<<<<<< Updated upstream
				a.download = 'exportProductstatistics.pdf';
=======
				a.download = 'exportUser.pdf';
>>>>>>> Stashed changes
				a.click();
				URL.revokeObjectURL(objectUrl);
			})
			.catch(function(error) {
				console.error('Error exporting PDF:', error);
			});
	};
});