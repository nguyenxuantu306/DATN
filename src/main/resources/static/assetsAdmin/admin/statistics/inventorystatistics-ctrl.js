app.controller("inventorystatistics-ctrl", function($scope, $http) {
	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}
	$scope.form = {};
	$scope.itemsThongKeSp = [];
	$scope.itemsThongKeTK = [];


	$scope.initialize = function() {
		$http.get('/rest/products/thongke/sp').then(response => {
			$scope.itemsThongKeSp = response.data;
			console.log($scope.itemsThongKeSp);
		}).catch(err => {
			console.log(err);
		})
		
		
		$http.get('/rest/products/thongke/top10tk').then(response => {
			$scope.itemsThongKeTK = response.data;
			console.log($scope.itemsThongKeTK);
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


 /*// biểu đồ 
let myChart = document.getElementById('myChart').getContext('2d');

// Global Options
Chart.defaults.global.defaultFontFamily = 'Lato';
Chart.defaults.global.defaultFontSize = 18;
Chart.defaults.global.defaultFontColor = '#777';

let massPopChart = new Chart(myChart, {
  type: 'bar', // bar, horizontalBar, pie, line, doughnut, radar, polarArea
  data: {
    labels: ['Boston', 'Worcester', 'Springfield', 'Lowell', 'Cambridge', 'New Bedford', 'City7', 'City8', 'City9', 'City10'],
    datasets: [{
      label: 'Population',
      data: [
        999999,
        617594,
        181045,
        153060,
        106519,
        105162,
        95072,
        90000, // Thêm giá trị cho City7
        85000, // Thêm giá trị cho City8
        80000  // Thêm giá trị cho City9
      ],
      backgroundColor: [
        'rgba(255, 99, 132, 0.6)',
        'rgba(54, 162, 235, 0.6)',
        'rgba(255, 206, 86, 0.6)',
        'rgba(75, 192, 192, 0.6)',
        'rgba(153, 102, 255, 0.6)',
        'rgba(255, 159, 64, 0.6)',
        'rgba(255, 99, 132, 0.6)',
        'rgba(54, 162, 235, 0.6)',
        'rgba(255, 206, 86, 0.6)',
        'rgba(75, 192, 192, 0.6)'
      ],
      borderWidth: 1,
      borderColor: '#777',
      hoverBorderWidth: 3,
      hoverBorderColor: '#000'
    }]
  },
  options: {
    title: {
      display: true,
      text: 'Top 10 Sản phẩm dư nhiều nhất',
      fontSize: 25
    },
    legend: {
      display: true,
      position: 'right',
      labels: {
        fontColor: '#000'
      }
    },
    layout: {
      padding: {
        left: 50,
        right: 0,
        bottom: 0,
        top: 0
      }
    },
    tooltips: {
      enabled: true
    }
  }
});*/

// Sử dụng JavaScript cơ bản hoặc Axios để lấy dữ liệu từ REST API
fetch('/rest/products/thongke/top10tk')
  .then(response => response.json())
  .then(data => {
    const labels = data.map(item => item.product.productname);
    const values = data.map(item => item.product.quantityavailable);

    // Vẽ biểu đồ bằng dữ liệu lấy được
    let myChart = document.getElementById('myChart').getContext('2d');

    // Cấu hình biểu đồ
    let massPopChart = new Chart(myChart, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Số lượng còn trong kho',
          data: values,
          backgroundColor: 'rgba(75, 192, 192, 0.6)',
          borderWidth: 1,
          borderColor: '#777',
          hoverBorderWidth: 3,
          hoverBorderColor: '#000'
        }]
      },
      options: {
        title: {
          display: true,
          text: 'Top 10 Sản phẩm dư nhiều nhất',
          fontSize: 25
        },
        legend: {
          display: true,
          position: 'right',
          labels: {
            fontColor: '#000'
          }
        },
        layout: {
          padding: {
            left: 50,
            right: 0,
            bottom: 0,
            top: 0
          }
        },
        tooltips: {
          enabled: true
        }
      }
    });
  });


});


