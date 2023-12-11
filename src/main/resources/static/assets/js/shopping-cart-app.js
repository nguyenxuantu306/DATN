const app = angular.module("shopping-cart-app", []);


app.controller("shopping-cart-ctrl", function($scope, $http) {
	$scope.selectedStatus = "1";

	$scope.filterOrders = function() {
		alert("ssss");
		// Lặp qua tất cả các hàng của bảng đơn hàng
		var rows = document.querySelectorAll("#orderList .text-center");
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			var statusCell = row.querySelector("td:nth-child(5)"); // Lấy ô chứa trạng thái

			// Kiểm tra xem trạng thái của đơn hàng có khớp với trạng thái đã chọn hoặc là trạng thái 1 hay không
			if (statusCell.textContent.trim() === selectedStatus || selectedStatus === "1") {
				// Hiển thị đơn hàng nếu khớp hoặc nếu đã chọn "Tất cả"
				row.style.display = "table-row";
			} else {
				// Ẩn đơn hàng nếu không khớp
				row.style.display = "none";
			}
		}
	};

	$scope.items = [];
	$scope.form = {};
	// tồn kho
	// Gọi API và truyền tham số
	$http.Updateslsp({
		method: 'PUT',  // Sử dụng phương thức PUT
		url: '/rest/products/purchase?productId=' + productId + '&quantityBought=' + quantityBought
	}).then(function(resp) {
		// Xử lý kết quả từ API ở đây
		$scope.items = resp.data;
		console.log($scope.items);
	}, function(error) {
		// Xử lý lỗi nếu có
		console.error("Lỗi khi gọi API: " + error);
	});

});

