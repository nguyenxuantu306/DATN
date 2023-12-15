app.controller("ticket-ctrl", function($scope, $http, $window) {
    $scope.selectedCameraId = null;
$scope.items = [];
$scope.bookings = [];
$scope.tourlist = [];

    $scope.initialize = function() {
		
		$http.get("/rest/tourdates/today").then(resp => {
			$scope.tourlist = resp.data;
			$scope.tourlist.forEach(item => {
				item.tourdates = new Date(item.tourdates)
			})
		});
		
		
		
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


 $scope.kiemtrave = function (items) {
        switch (items.statusbooking.statusbookingid) {
            case 1:
                $scope.ketquakiemtra = "Ve dang cho xac nhan, chua the su dung";
                break;
            case 2:
                $scope.ketquakiemtra = "Ve hop le co the su dung";
                break;
            case 3:
            case 4:
                $scope.ketquakiemtra = "Ve da bi huy";
                break;
            case 5:
                $scope.ketquakiemtra = "Ve da duoc su dung";
                break;
            default:
                $scope.ketquakiemtra = "Trang thai ve khong hop le";
        }
    };

    $scope.selectCamera = function(){
        const selectedCameraId = document.getElementById('cameraSelect').value;
        $scope.selectedCameraId = selectedCameraId;

        // Gọi lại getUserMedia với thiết bị được chọn
        navigator.mediaDevices.getUserMedia({
            video: { deviceId: selectedCameraId }
        })
        .then(function (stream) {
            const videoElement = document.getElementById('camera-view');
            videoElement.srcObject = stream;
        })
        .catch(function (error) {
            console.error('Error accessing camera:', error);
        });
    }

    $scope.captureAndSend = function() {
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
        console.log("day la du lieu gui di"+imageData);
        $http.post('/checktikcet', { imageData: imageData})
            .then(resp => {
				console.log("cmthành công " ); 
				console.log(resp.data);
				$scope.items.push(resp.data);
				$scope.kiemtrave($scope.items);
				
			}).catch(error => {
			// Sử dụng SweetAlert2 cho thông báo lỗi
			
			console.log("cmt lỗi " ); 
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
    
     

    // Khởi đầu
    $scope.initialize();
    
   
});
