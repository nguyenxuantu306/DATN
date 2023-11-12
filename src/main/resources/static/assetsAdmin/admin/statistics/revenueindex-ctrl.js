app.controller('revenueindex-ctrl', function($scope, $http) {
	$scope.form = {};
	$scope.itemsThongKeSTstatus = [];
	$scope.itemsThongKebookingstatus = [];
	$scope.itemsThongKestats = [];
	$scope.itemsThongKeDT = [];
	$scope.itemsThongKeDTTOUR = [];
	$scope.itemsThongKetotaluser = [];
	$scope.itemsThongKeBookingtotaluser = [];
	$scope.itemsThongKeCountcomment = [];
	$scope.latestYear = [];
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
		$http.get('/rest/bookings/slbookingstatus').then(response => {
			$scope.itemsThongKebookingstatus = response.data;
			console.log($scope.itemsThongKebookingstatus);

		})

		$http.get('/rest/rating/stats').then(response => {
			$scope.itemsThongKestats = response.data;
			console.log($scope.itemsThongKestats);

		})

		$http.get('/rest/orders/year-revenue').then(response => {
			$scope.itemsThongKeDT = response.data;
			console.log($scope.itemsThongKeDT);

		});
		$http.get('/rest/bookings/bookingyear-revenue').then(response => {
			$scope.itemsThongKeDTTOUR = response.data;
			console.log($scope.itemsThongKeDTTOUR);

		});

		$http.get("/rest/users").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.createddate = new Date(item.createddate);
				item.birthday = new Date(item.birthday);
			});

			// Sử dụng $scope.items.length để đếm số người đăng ký
			$scope.numberOfRegistrations = $scope.items.length;
		});

		$http.get('/rest/comment').then(response => {
			$scope.items = response.data;
			$scope.items.forEach(item => {
				item.commentdate = new Date(item.commentdate);
			});

			// Đếm số lượt comment
			$scope.numberOfComments = $scope.items.length;
		});


		// Thống kê tổng tiền mua hàng của từng user
		$http.get('/rest/users/total-purchase').then(response => {
			$scope.itemsThongKetotaluser = response.data;
			console.log($scope.itemsThongKetotaluser);

		});

		// Thống kê tổng tiền đặt vé của từng user
		$http.get('/rest/users/bookingtotal-purchase').then(response => {
			$scope.itemsThongKeBookingtotaluser = response.data;
			console.log($scope.itemsThongKeBookingtotaluser);

		});



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

	$scope.calculateTotalCount1 = function() {
		let totalCount = 0;
		for (let i = 0; i < $scope.itemsThongKebookingstatus.length; i++) {
			totalCount += $scope.itemsThongKebookingstatus[i].count;
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

	// Gọi API đầu tiên và lấy dữ liệu
	fetch('/rest/orders/year-revenue')
		.then(response => response.json())
		.then(data => {
			// Trích xuất dữ liệu từ API đầu tiên
			const labels = data.map(item => 'Năm ' + item.yearValue); // Thêm "Tháng" trước dữ liệu từ API
			const ordersRevenue = data.map(item => item.sum); // Sử dụng dữ liệu từ API đầu tiên

			// Gọi API thứ hai và lấy dữ liệu
			fetch('/rest/bookings/bookingyear-revenue')
				.then(response => response.json())
				.then(secondData => {
					// Trích xuất dữ liệu từ API thứ hai
					const bookingsRevenue = secondData.map(item => item.sum);

					// Tạo biểu đồ đường
					var ctx = document.getElementById('myChart2').getContext('2d');
					new Chart(ctx, {
						type: 'line',
						data: {
							labels: labels,
							datasets: [
								{
									label: 'Doanh thu đơn hàng',
									data: ordersRevenue,
									borderColor: '#ff0000', // Màu đường đầu tiên
									borderWidth: 2,
									fill: false,
								},
								{
									label: 'Doanh thu đặt tour',
									data: bookingsRevenue,
									borderColor: '#00ff00', // Màu đường thứ hai
									borderWidth: 2,
									fill: false,
								},
							],
						},
						options: {
							plugins: { // Thêm plugin chú thích
								legend: {
									display: true,
									position: 'nw',
								},
							},
							title: {
								display: true,
								text: 'Biểu đồ doanh thu theo năm', // Tiêu đề của biểu đồ
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
									max: Math.max(...ordersRevenue, ...bookingsRevenue), // Điều chỉnh tùy thuộc vào dữ liệu
								},
							},
						},
					});
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
		// Gọi API để lấy dữ liệu cho biểu đồ dựa trên selectedYear từ API 1
		fetch(`/rest/orders/findyearrevenue/${selectedYear}`)
			.then(response => response.json())
			.then(data => {
				const labels = data.map(item => 'Tháng ' + item.month);
				const newCust = data.map(item => item.sum);

				// Lấy dữ liệu từ API 2
				fetch(`/rest/bookings/findbookingyearrevenue/${selectedYear}`)
					.then(response => response.json())
					.then(data2 => {
						const newCust2 = data2.map(item => item.sum);

						// Làm mới biểu đồ
						var ctx = document.getElementById('myChart').getContext('2d');
						var myChart = new Chart(ctx, {
							type: 'line',
							data: {
								labels: labels,
								datasets: [
									{
										label: 'Tổng tiền các tháng (Shop)',
										data: newCust,
										borderColor: '#ff0000', // Đổi màu đường thành màu đỏ
										borderWidth: 2,
										fill: false,
									},
									{
										label: 'Tổng tiền các tháng (Tour)',
										data: newCust2,
										borderColor: '#00ff00', // Đổi màu đường thành màu xanh
										borderWidth: 2,
										fill: false,
									},
								],
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
										max: Math.max(...newCust, ...newCust2), // Giá trị tối đa của trục y, dựa trên dữ liệu từ cả hai API
									},
								},
							},
						});
					});
			});
	}

	$scope.pager = {
		page: 0,
		size: 5,
		get itemsThongKetotaluser() {
			var start = this.page * this.size;
			return $scope.itemsThongKetotaluser.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.itemsThongKetotaluser.length
				/ this.size);
		},
		get itemsThongKeBookingtotaluser() {
			var start = this.page * this.size;
			return $scope.itemsThongKeBookingtotaluser.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.itemsThongKeBookingtotaluser.length
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
	// Hợp nhất hai mảng thành một mảng tổng hợp
	$scope.combinedArray = $scope.pager.itemsThongKetotaluser.concat($scope.pager.itemsThongKeBookingtotaluser);


	$scope.initialize();
})