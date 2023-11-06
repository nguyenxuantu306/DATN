app.controller('revenueindex-ctrl', function($scope, $http) {
	$scope.form = {};
	$scope.itemsThongKeSTstatus = [];
	$scope.itemsThongKestats = [];
	$scope.itemsThongKeDT = [];

		$scope.selectedYear = 2023; // Đặt giá trị mặc định là năm 2023

		$scope.itemsThongKeFindDT = [];

	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}

	$scope.initialize = function() {

		$http.get('/rest/orders/slstatus').then(response => {
			$scope.itemsThongKeSTstatus = response.data;
			console.log($scope.itemsThongKeSTstatus);

		})

		$http.get('/rest/rating/stats').then(response => {
			$scope.itemsThongKestats = response.data;
			console.log($scope.itemsThongKestats);

		})

		$http.get('/rest/orders/year-revenue').then(response => {
			$scope.itemsThongKeDT = response.data;
			console.log($scope.itemsThongKeDT);

		})

	

		$http.get('/rest/orders/findyearrevenue/' + $scope.selectedYear)
			.then(function(response) {
				$scope.itemsThongKeFindDT = response.data;
				console.log($scope.itemsThongKeFindDT);
			})
			.catch(function(error) {
				console.error("Lỗi trong quá trình gọi API: ", error);
			});

	}

	$scope.calculateTotalCount = function() {
		let totalCount = 0;
		for (let i = 0; i < $scope.itemsThongKeSTstatus.length; i++) {
			totalCount += $scope.itemsThongKeSTstatus[i].count;
		}
		return totalCount;
	};

	$scope.calculateTotalCountstats = function() {
		let totalCount = 0;
		for (let i = 0; i < $scope.itemsThongKestats.length; i++) {
			totalCount += $scope.itemsThongKestats[i].count;
		}
		return totalCount;
	};



	// Biểu đồ 

	// Gọi API để lấy dữ liệu từ "/rest/orders/year-revenue"
	fetch('/rest/orders/year-revenue')
		.then(response => response.json())
		.then(data => {
			// Trích xuất dữ liệu từ API
			const labels = data.map(item => 'Năm ' + item.yearValue); // Thêm "Tháng" trước dữ liệu từ API
			const newCust = data.map(item => item.sum); // Sử dụng dữ liệu từ API

			// Tạo biểu đồ đường
			var ctx = document.getElementById('myChart2').getContext('2d');
			new Chart(ctx, {
				type: 'line',
				data: {
					labels: labels,
					datasets: [{
						label: 'Tổng tiền năm',
						data: newCust,
						borderColor: '#ff0000', // Đổi màu đường thành màu đỏ
						borderWidth: 2,
						fill: false,
					}],
				},
				options: {
					plugins: { // Thêm plugin chú thích
						legend: {
							display: true,
							position: 'nw',
						},
						title: {
							display: true,
							text: 'Biểu đồ doanh thu theo năm', // Tiêu đề của biểu đồ
						},
					},
					scales: {
						x: {
							display: true,
							title: {
								display: true,
								text: 'Năm', // Thay đổi tiêu đề của trục x
							},
						},
						y: {
							display: true,
							title: {
								display: true,
								text: 'Số lượng',
							},
							min: 0,
							max: Math.max(...newCust),
						},
					},
				},
			});
		});


	// Mặc định, hiển thị biểu đồ cho năm 2023
	createChart(2023);

	// Lắng nghe sự kiện khi một năm mới được chọn từ danh sách
	const listItems = document.getElementById('listItems');
	listItems.addEventListener('click', (event) => {
		if (event.target.tagName === 'A') {
			event.preventDefault(); // Ngăn chuyển hướng trang khi nhấp vào liên kết
			const selectedYear = event.target.getAttribute('data-year'); // Lấy năm từ thuộc tính data-year
			createChart(selectedYear);
		}
	});

	// Hàm tạo biểu đồ dựa trên năm được chọn
	function createChart(selectedYear) {
		// Gọi API để lấy dữ liệu cho biểu đồ dựa trên selectedYear
		fetch(`/rest/orders/findyearrevenue/${selectedYear}`)
			.then(response => response.json())
			.then(data => {
				const labels = data.map(item => 'Tháng ' + item.month);
				const newCust = data.map(item => item.sum);

				// Làm mới biểu đồ
				var ctx = document.getElementById('myChart').getContext('2d');
				var myChart = new Chart(ctx, {
					type: 'line',
					data: {
						labels: labels,
						datasets: [{
							label: 'Tổng tiền các tháng trong năm',
							data: newCust,
							borderColor: '#ff0000', // Đổi màu đường thành màu đỏ
							borderWidth: 2,
							fill: false,
						}],
					},
					options: {
						legend: {
							display: true, // Hiển thị chú thích
							align: 'start', // Vị trí của chú thích (start, center, end)
							anchor: 'start', // Vị trí neo của chú thích (start, center, end)
						},
						scales: {
							x: {
								display: true, // Hiển thị trục x
								title: {
									display: true,
									text: 'Tháng', // Tiêu đề của trục x
								},
							},
							y: {
								beginAtZero: true, // Để bắt đầu từ 0
								title: {
									display: true,
									text: 'Số lượng', // Tiêu đề của trục y
								},
								min: 0, // Giá trị tối thiểu của trục y
								max: Math.max(...newCust), // Giá trị tối đa của trục y, dựa trên dữ liệu
							},
						},
					},
				});
			});
	}




	$scope.initialize();
})