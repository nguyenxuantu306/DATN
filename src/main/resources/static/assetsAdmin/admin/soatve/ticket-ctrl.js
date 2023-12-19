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
			$scope.ketquaquet = "";
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

				$scope.ketquakiemtra = "Vé đang chờ xác nhận, chưa thể sử dụng";
				break;
			case 2:

				$scope.ketquakiemtra = "Vé hợp lệ có thể sử dụng";

				break;
			case 3:
			case 4:

				$scope.ketquakiemtra = "Vé đã bị hủy";
				break;
			case 5:

				$scope.ketquakiemtra = "Vé đã được sử dụng";
				// Kiểm tra xem tourDate có phải là ngày hôm nay không
				var today = new Date();
				var tourDate = new Date(items.tourDateBooking.tourdate.tourdates);

				if (
					tourDate.getDate() === today.getDate() &&
					tourDate.getMonth() === today.getMonth() &&
					tourDate.getFullYear() === today.getFullYear()
				) {
					// Đây là ngày hôm nay
					console.log("TourDate là ngày hôm nay.");
				} else {
					console.log("TourDate không phải là ngày hôm nay.");
				}
				break;
			default:

				$scope.ketquakiemtra = "Trạng thái vé không hợp lệ";
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
			alert("Hãy chọn camera.");
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
				console.log(resp.data);
				$scope.items.push(resp.data);

				$scope.kiemtrave(resp.data);
				$scope.ketquaquet = "Quét thành công";

			}).catch(error => {
				// Sử dụng SweetAlert2 cho thông báo lỗi
					$scope.ketquaquet = "Quét  thất bại hãy quét lại";
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
