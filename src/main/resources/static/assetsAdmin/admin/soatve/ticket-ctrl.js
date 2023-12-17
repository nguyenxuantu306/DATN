app.controller("ticket-ctrl", function($scope, $http, $window) {
	$scope.selectedCameraId = null;
	$scope.items = [];
	$scope.items2 = [];
	$scope.bookings = [];
	$scope.tourlist = [];
	$scope.history = [];
	$scope.tourdatebk = [];

	$scope.initialize = function() {

		$http.get("/rest/tourdates/today").then(resp => {
			$scope.tourlist = resp.data;
			$scope.tourlist.forEach(item => {
				item.tourdates = new Date(item.tourdates)
			})
		});

		$http.get("/rest/tourdates").then(resp => {
			$scope.items2 = resp.data;
			$scope.items2.forEach(item => {
				item.tourdates = new Date(item.tourdates)
			})
		});


		$scope.reset = function() {
			$scope.error = ['err'];
			$scope.form = {
				
			};
		}



		// Lấy danh sách camera từ WebRTC API

		navigator.mediaDevices.enumerateDevices()
			.then(function(devices) {
				const cameraSelect = document.getElementById('cameraSelect');

				devices.forEach(function(device) {
					if (device.kind === 'videoinput') {
						const option = document.createElement('option');
						option.value = device.deviceId;
						option.text = device.label || 'Camera ' + (cameraSelect.length + 1);
						cameraSelect.add(option);
					}
				});
			})
			.catch(function(error) {
				console.error('Error enumerating devices:', error);
			});
	}

	//count booking
	$scope.countbk = function(item) {
		const count = item.tourdatebooking ? item.tourdatebooking.length : 0;
		return count;
	};





	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.selectedItem = item;
		$scope.form = angular.copy(item);
		/*$(".nav-tabs a:eq(0)").tab('show');*/
	}

	$scope.update = function(item) {
		$http.put(`/rest/bookings/${item.bookingid}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.bookingid == item.bookingid);
			$scope.items[index] = item;
			if (item.statusbooking.statusbookingid == '2') {
				$http.get(`/rest/bookings/sendbooking/${item.bookingid}`);
			}

			// Sử dụng SweetAlert2 cho thông báo thành công
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Cập nhật trạng thái thành công !',
			});
			refreshPage();
		})
			.catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Cập nhật trạng thái thất bại !',
				});
				console.log("Error", error);
			});

	}

	$scope.kiemtrave = function(items) {
		console.log("runing");
		const ketqua = items.statusbooking.statusbookingid;
		switch (ketqua) {
			case 1:
				console.log("ko dung duoc");
				$scope.ketquakiemtra = "Ve dang cho xac nhan, chua the su dung";
				break;
			case 2:
				console.log("ok dung duoc");
				$scope.ketquakiemtra = "Ve hop le co the su dung";

				break;
			case 3:
			case 4:
				console.log("ko dung duoc");
				$scope.ketquakiemtra = "Ve da bi huy";
				break;
			case 5:
				console.log("ko dung duoc");
				$scope.ketquakiemtra = "Ve da duoc su dung";
				break;
			default:
				console.log("ko dung duoc");
				$scope.ketquakiemtra = "Trang thai ve khong hop le";
		}
	};

	$scope.selectCamera = function() {
		const selectedCameraId = document.getElementById('cameraSelect').value;
		$scope.selectedCameraId = selectedCameraId;

		// Gọi lại getUserMedia với thiết bị được chọn
		navigator.mediaDevices.getUserMedia({
			video: { deviceId: selectedCameraId }
		})
			.then(function(stream) {
				const videoElement = document.getElementById('camera-view');
				videoElement.srcObject = stream;
			})
			.catch(function(error) {
				console.error('Error accessing camera:', error);
			});
	}

	$scope.captureAndSend = function() {
		$scope.reset();
		if (!$scope.selectedCameraId) {
			alert("Please select a camera.");
			return;
		}

		// Gửi hình ảnh từ camera đến REST API
		const videoElement = document.getElementById('camera-view');
		const canvas = document.createElement('canvas');
		const context = canvas.getContext('2d');
		canvas.width = videoElement.videoWidth;
		canvas.height = videoElement.videoHeight;
		context.drawImage(videoElement, 0, 0, canvas.width, canvas.height);

		const imageData = canvas.toDataURL('image/png');
		console.log("day la du lieu gui di" + imageData);
		$http.post('/checktikcet', { imageData: imageData })
			.then(resp => {
				console.log("cmthành công ");
				console.log(resp.data);
				console.log("cmthành công ");
				$scope.items.push(resp.data);
				
				$scope.kiemtrave(resp.data);

			}).catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi

				console.log("cmt lỗi ");
				console.error("Error", error);
				console.log("Error", error);
			});

		/*function(response) {
		console.log("vua pust xong");
		// Xử lý kết quả từ REST API (nếu cần)
		console.log('Capture successful:', response.data);
	})
	.catch(function(error) {
		console.error('Error capturing and sending image:', error);
	});*/
	}
	$scope.formatPrice = function(price) {
		var priceString = price.toString();
		priceString = priceString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		return priceString + " đ";
	}


	// Khởi đầu
	$scope.initialize();


});
