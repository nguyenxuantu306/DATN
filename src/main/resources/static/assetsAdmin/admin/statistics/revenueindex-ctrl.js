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



	// Biểu đồ miền doanh thu các năm 
	// Gọi API và lấy dữ liệu
	fetch('/rest/orders/year-revenue')
		.then(response => response.json())
		.then(data => {
			// Trích xuất dữ liệu từ API
			const labels = data.map(item => 'Năm ' + item.yearValue);
			const ordersRevenue = data.map(item => item.sum);

			// Tạo biểu đồ đường
			var ctx = document.getElementById('myChart3').getContext('2d');
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
					tooltips: {
						callbacks: {
							label: function(tooltipItem, data) {
								var value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
								return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
							}
						}
					},
					scales: {
						yAxes: [{
							ticks: {
								callback: function(value, index, values) {
									return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
								}
							}
						}]
					}
				},
			});
		});
	// Gọi API và lấy dữ liệu
	fetch('/rest/bookings/bookingyear-revenue')
		.then(response => response.json())
		.then(data => {
			// Trích xuất dữ liệu từ API
			const labels = data.map(item => 'Năm ' + item.yearValue);
			const bookingsRevenue = data.map(item => item.sum);

			// Tạo biểu đồ đường
			var ctx = document.getElementById('myChart4').getContext('2d');
			new Chart(ctx, {
				type: 'line',
				data: {
					labels: labels,
					datasets: [
						{
							label: 'Doanh thu đặt tour',
							data: bookingsRevenue,
							borderColor: '#00ff00', // Màu đường đầu tiên
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
						text: 'Biểu đồ doanh thu đặt tour theo năm', // Tiêu đề của biểu đồ
					},
					tooltips: {
						callbacks: {
							label: function(tooltipItem, data) {
								var value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
								return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
							}
						}
					},
					scales: {
						yAxes: [{
							ticks: {
								callback: function(value, index, values) {
									return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
								}
							}
						}]
					}
				}

			});
		});

	// Biểu đồ miền doanh thu các năm 

	function getCurrentYear() {
		return new Date().getFullYear();
	}

	function createRecentYearsList() {
		const listItems = document.getElementById('listItems');
		const ulElement = document.createElement('ul');
		ulElement.style.fontWeight = 'bold';
		ulElement.style.listStyle = 'none';
		ulElement.style.padding = '0';
		ulElement.style.display = 'flex'; // hoặc 'inline-block'


		for (let year = getCurrentYear(); year > getCurrentYear() - 5; year--) {



			const listItem = document.createElement('li');

			// Thêm icon trước mỗi năm
			const icon = document.createElement('i');
			icon.style.color = 'green';
			icon.className = 'fas fa-calendar';
			listItem.appendChild(icon);


			// Thêm khoảng cách giữa icon và năm
    		icon.style.marginRight = '10px';
			
			const link = document.createElement('a');
			link.href = '#';
			link.setAttribute('data-year', year);
			link.textContent = `Năm: ${year}`;
			listItem.appendChild(link);

			ulElement.appendChild(listItem);
		}

		// Áp dụng khoảng trống giữa các năm
		const listItemsArray = Array.from(ulElement.children);
		listItemsArray.forEach((item, index) => {
			if (index < listItemsArray.length - 1) {
				item.style.marginRight = '50px';
			}
		});

		listItems.appendChild(ulElement);

		// Lấy năm hiện tại và gọi hàm để tạo biểu đồ từ API 1 và API 2
		const currentYearValue = getCurrentYear();
		createChartFromAPI1(currentYearValue);
		createChartFromAPI2(currentYearValue);
	}

	// Lắng nghe sự kiện khi một năm mới được chọn từ danh sách
	const listItems = document.getElementById('listItems');
	listItems.addEventListener('click', (event) => {
		if (event.target.tagName === 'A') {
			event.preventDefault(); // Ngăn chuyển hướng trang khi nhấp vào liên kết
			const selectedYear = event.target.getAttribute('data-year');

			// Gọi hàm để tạo biểu đồ từ API 1 và API 2
			createChartFromAPI1(selectedYear);
			createChartFromAPI2(selectedYear);
		}
	});

	// Gọi hàm để tạo danh sách năm và biểu đồ khi trang được tải
	createRecentYearsList();




	// Thêm thư viện numeral.js vào trang web của bạn trước khi sử dụng

	function createChartFromAPI1(selectedYear) {
		fetch(`/rest/orders/findyearrevenue/${selectedYear}`)
			.then(response => response.json())
			.then(data => {
				const labels = data.map(item => 'Tháng ' + item.month);
				const newCust = data.map(item => item.sum);

				// Làm mới biểu đồ
				var ctx = document.getElementById('myChart1').getContext('2d');
				myChart = new Chart(ctx, {
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
						],
					},
					options: {
						tooltips: {
							callbacks: {
								label: function(tooltipItem, data) {
									var value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
									return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
								}
							}
						},
						scales: {
							yAxes: [{
								ticks: {
									callback: function(value, index, values) {
										return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
									}
								}
							}]
						}
					}
				});
			})
			.catch(error => console.error('Error:', error));
	}

	// Hàm tạo biểu đồ dựa trên dữ liệu từ API 2
	function createChartFromAPI2(selectedYear) {
		fetch(`/rest/bookings/findbookingyearrevenue/${selectedYear}`)
			.then(response => response.json())
			.then(data => {
				const labels = data.map(item => 'Tháng ' + item.month);
				const newCust = data.map(item => item.sum);
				// Làm mới biểu đồ
				var ctx = document.getElementById('myChart2').getContext('2d');
				var myChart = new Chart(ctx, {
					type: 'line',
					data: {
						labels: labels,
						datasets: [
							{
								label: 'Tổng tiền các tháng (Tour)',
								data: newCust,
								borderColor: '#00ff00', // Đổi màu đường thành màu xanh
								borderWidth: 2,
								fill: false,
							},
						],
					},
					options: {
						tooltips: {
							callbacks: {
								label: function(tooltipItem, data) {
									var value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
									return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
								}
							}
						},
						scales: {
							yAxes: [{
								ticks: {
									callback: function(value, index, values) {
										return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
									}
								}
							}]
						}
					},
				});
			});
	}
	// Biểu đồ miền doanh thu các tháng của năm 


	// Biểu đồ cột thông kê sltt đơn đặt vé
	fetch('/rest/bookings/slbookingstatus') // Thay thế bằng đường dẫn của API
		.then(response => response.json())
		.then(data => {
			const labels = data.map(item => item.group);
			const values = data.map(item => item.count);

			const backgroundColors = generateRandomColors(data.length);

			const chartData = {
				datasets: [{
					data: values,
					backgroundColor: backgroundColors,
					label: 'Trạng thái của đơn đặt vé'
				}],
				labels: labels
			};

			var ctx = document.getElementById('myChart6').getContext('2d');
			new Chart(ctx, {
				data: chartData,
				type: 'polarArea' // Thay đổi loại biểu đồ từ 'polarArea' sang 'bar'
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
	}
	// Biểu đồ cột thông kê sltt đơn đặt vé


	// Biểu đồ cột thông sltt đơn hàng
	fetch('/rest/orders/slstatus')
		.then(response => response.json())
		.then(data => {
			const labels = data.map(item => item.group);
			const values = data.map(item => item.count);

			const backgroundColors = generateRandomColors(data.length);

			const chartData = {
				datasets: [{
					data: values,
					backgroundColor: backgroundColors,
					label: 'Trạng thái của đơn hàng'
				}],
				labels: labels
			};

			var ctx = document.getElementById('myChart5').getContext('2d');
			new Chart(ctx, {
				data: chartData,
				type: 'polarArea', // Chuyển sang biểu đồ kim tự tháp
				options: {
					plugins: {
						legend: {
							display: true,
							position: 'right'
						}
					}
				}
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
	}


	// Biểu đồ cột thông sltt đơn hàng

	// Biểu đồ cột thông kê top10sp banchayj
	fetch('/rest/products/thongke/top10spbanchay') // Thay thế bằng đường dẫn của API
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

			var ctx = document.getElementById('myChart7').getContext('2d');
			new Chart(ctx, {
				data: chartData,
				type: 'horizontalBar'
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
	}
	// Biểu đồ cột thông kê top10sp banchayj

	// Biểu đồ cột thông kê top10sp tồn kho
	fetch('/rest/products/thongke/top10tk')
		.then(response => response.json())
		.then(data => {
			console.log(data); // In ra dữ liệu từ API trong console
			const labels = data.map(item => item.productname);
			const values = data.map(item => item.quantityavailable);

			console.log(data);

			// Tạo mảng màu sắc ngẫu nhiên
			const backgroundColors = generateRandomColors(data.length);

			// Vẽ biểu đồ bằng dữ liệu lấy được và màu sắc tương ứng
			let myChart = document.getElementById('myChart8').getContext('2d');

			// Cấu hình biểu đồ
			let massPopChart = new Chart(myChart, {
				type: 'horizontalBar', // Thay đổi loại biểu đồ từ 'bar' sang 'horizontalBar'
				data: {
					labels: labels,
					datasets: [{
						label: 'Số lượng còn trong kho',
						data: values,
						backgroundColor: backgroundColors, // Sử dụng mảng màu sắc ngẫu nhiên
						borderWidth: 1,
						borderColor: '#777',
						hoverBorderWidth: 3,
						hoverBorderColor: '#000'
					}]
				},
				options: {
					// title: {
					//     display: true,
					//     text: 'Top 10 Sản phẩm dư nhiều nhất',
					//     fontSize: 25
					// },
					legend: {
						display: true,
						position: 'top' // Chuyển chú thích lên trên đầu
					},
					layout: {
						padding: {
							left: 50,
							right: 0,
							bottom: 0,
							top: -10 // Di chuyển biểu đồ lên một chút
						}
					},
					tooltips: {
						enabled: true
					}
				}
			});
		});

	// Biểu đồ cột thông kê top10sp tồn kho

	// Biểu đồ thống kê số loại sản phẩm bán được
	async function fetchData(date) {
		try {
			const response = await fetch(`/rest/orders/getCategorySalesByDate?date=${date}`);
			const data = await response.json();
			return data;
		} catch (error) {
			console.error('Error fetching data:', error);
			throw error;
		}
	}

	// Sử dụng hàm fetchData để lấy dữ liệu và vẽ biểu đồ
	document.getElementById('dateInput').addEventListener('change', function() {
		const selectedDate = this.value;

		fetchData(selectedDate)
			.then(data => {
				console.log(data); // In ra dữ liệu từ API trong console
				const labels = data.map(item => item.group);
				const values = data.map(item => item.count);

				// Tạo mảng màu sắc ngẫu nhiên
				const backgroundColors = generateRandomColors(data.length);

				// Vẽ biểu đồ bằng dữ liệu lấy được và màu sắc tương ứng
				let myChart = document.getElementById('myChart9').getContext('2d');

				// Cấu hình biểu đồ
				let massPopChart = new Chart(myChart, {
					type: 'horizontalBar', // Thay đổi loại biểu đồ từ 'bar' sang 'horizontalBar'
					data: {
						labels: labels,
						datasets: [{
							label: 'Số lượng sản phẩm bán theo danh mục',
							data: values,
							backgroundColor: backgroundColors, // Sử dụng mảng màu sắc ngẫu nhiên
							borderWidth: 1,
							borderColor: '#777',
							hoverBorderWidth: 3,
							hoverBorderColor: '#000'
						}]
					},
					options: {
						legend: {
							display: true,
							position: 'top' // Chuyển chú thích lên trên đầu
						},
						layout: {
							padding: {
								left: 50,
								right: 0,
								bottom: 0,
								top: -10 // Di chuyển biểu đồ lên một chút
							}
						},
						tooltips: {
							enabled: true
						}
					}
				});
			})
			.catch(error => {
				// Xử lý lỗi nếu có
				console.error('Error:', error);
			});
	});


	// Hàm tạo mảng màu sắc ngẫu nhiên
	function generateRandomColors(count) {
		const colors = [];
		for (let i = 0; i < count; i++) {
			const color = `rgba(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, 0.7)`;
			colors.push(color);
		}
		return colors;
	}

	// Sử dụng hàm fetchData để lấy dữ liệu và vẽ biểu đồ
	function updateChartWithDefaultDate() {
		const defaultDate = new Date().toISOString().slice(0, 10);
		const dateInput = document.getElementById('dateInput');
		dateInput.value = defaultDate;

		// Gọi sự kiện change để kích hoạt lấy dữ liệu và vẽ biểu đồ
		const changeEvent = new Event('change');
		dateInput.dispatchEvent(changeEvent);
	}


	// Biểu đồ thống kê số loại sản phẩm bán được
	fetch('/rest/orders/last7days')
		.then(response => response.json())
		.then(data => {
			console.log(data);

			// Sử dụng moment.js để định dạng ngày
			const formattedDates = data.map(item => moment(item.date).format('DD-MM-YYYY'));
			const values = data.map(item => item.sum);

			// Tạo mảng màu sắc ngẫu nhiên
			const backgroundColors = generateRandomColors(data.length);

			// Vẽ biểu đồ bằng dữ liệu lấy được và màu sắc tương ứng
			let myChart = document.getElementById('myChart10').getContext('2d');

			// Cấu hình biểu đồ
			let massPopChart = new Chart(myChart, {
				type: 'bar', // Chuyển sang biểu đồ dạng cột (bar)
				data: {
					labels: formattedDates,
					datasets: [{
						label: 'Tổng Doanh thu 7 ngày trước',
						data: values,
						backgroundColor: backgroundColors, // Sử dụng mảng màu sắc ngẫu nhiên
						borderWidth: 1,
						borderColor: '#777',
						hoverBorderWidth: 3,
						hoverBorderColor: '#000'
					}]
				},
				options: {
					legend: {
						display: true,
						position: 'top'
					},
					layout: {
						padding: {
							left: 50,
							right: 0,
							bottom: 0,
							top: -10
						}
					},

					tooltips: {
						enabled: true,
						callbacks: {
							label: function(tooltipItem, data) {
								var value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
								return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
							}
						}
					},
					scales: {
						yAxes: [{
							ticks: {
								callback: function(value, index, values) {
									return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
								}
							}
						}]
					}

				}
			});
		});


	// Gọi hàm để cập nhật biểu đồ với ngày mặc định
	updateChartWithDefaultDate();



	// Biểu đồ cột thông kê doanh thu 7 ngày trc đó

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