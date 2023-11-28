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
		size: 10,
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
				a.download = 'exportProductstatistics.pdf';
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
	
	// biểu đồ
	/*fetch('/rest/products/thongke/top10spbanchay') // Thay thế bằng đường dẫn của API
  .then(response => response.json())
  .then(data => {
    const labels = data.map(item => item.group.productname);
    const values = data.map(item => item.count);

    const backgroundColors = generateRandomColors(data.length);

    const chartData = {
      datasets: [{
        data: values,
        backgroundColor: backgroundColors,
        label: 'Sản phẩm theo tổng giá trị'
      }],
      labels: labels
    };

    var ctx = document.getElementById('myChart').getContext('2d');
    new Chart(ctx, {
      data: chartData,
      type: 'polarArea'
    });
  });

function generateRandomColors(count) {
  const colors = [];
  for (let i = 0; i < count; i++) {
    const randomColor = `rgba(${getRandomInt(0, 255)}, ${getRandomInt(0, 255)}, ${getRandomInt(0, 255)}, 0.6)`;
    colors.push(randomColor);
  }
  return colors;
}

function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}*/



});