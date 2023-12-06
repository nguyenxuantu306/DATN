app.controller("bookingdate-ctrl", function($scope, $http) {
	/*alert("Quản lý order")*/
	$scope.items = [];
	$scope.cates = [];
	$scope.items2 = [];
	$scope.form = {};
	$scope.products = [];
	$scope.selectedItem = null;
	$scope.totalPrice = 0;

	$scope.initialize = function() {
		// Load products
		$http.get("/rest/tourdatebookings").then(resp => {
			$scope.items = resp.data;
		});


	}

	// Khởi đầu
	$scope.initialize();


	// Hiện thị lên form
	$scope.edit = function(item) {
		$scope.selectedItem = item;
		$scope.form = angular.copy(item);
		/*$(".nav-tabs a:eq(0)").tab('show');*/
	}
	//Tính tổng
	$scope.calculateTotal = function(item) {
		return (item.booking.adultticketnumber * item.booking.tour.pricings.adultprice) + (item.booking.childticketnumber * item.booking.tour.pricings.childprice);
	}


	$scope.sortType = 'desc';
	$scope.sortKey = 'tourdate';

	$scope.sortBy = function(key) {
		$scope.sortType = ($scope.sortKey === key) ? ($scope.sortType === 'asc' ? 'desc' : 'asc') : 'asc';
		$scope.sortKey = key;
	};




	$scope.selectedStatus = "0";

	$scope.filterBookings = function() {
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


	$scope.pager = {
    page: 0,
    size: 10,
    get items() {
        // Sắp xếp mảng $scope.items theo ngày đặt tour giảm dần (từ mới nhất đến cũ nhất)
        var sortedItems = $scope.items.slice().sort(function(a, b) {
            var dateA = new Date(a.tourdate.tourdates);
            var dateB = new Date(b.tourdate.tourdates);
            return dateB - dateA;
        });

        var start = this.page * this.size;
        var paginatedItems = sortedItems.slice(start, start + this.size);
        return paginatedItems;
    },
    get count() {
        return Math.ceil(1.0 * $scope.items.length / this.size);
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
    }
};




	$scope.formatPrice = function(price) {
		var priceString = price.toString();
		priceString = priceString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		return priceString + " đ";
	}


});

