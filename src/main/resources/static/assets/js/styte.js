// Hàm tạo mã HTML cho một sản phẩm
function createProductHTML(product) {
	var productHTML = `
              <div 
		class="col-lg-4 col-md-6 mb-4">
		<div class="package-item bg-white mb-2">
			<a th:href="@{|/product/detail/${product.productid}|}"><img
				style="height: 250px" class="img-fluid"
				src="${product.image}" alt=""></a>
			<div class="p-4">
				<a 
					class="h5 text-decoration-none">${product.productname}</a>
				<div class="border-top mt-4 pt-4">
					<div class="d-flex justify-content-between">
						<h6 class="m-0">
							<i class="fa fa-star text-primary mr-2"></i>4.5 <small>(250)</small>
						</h6>
						<h5
				
							class="m-0">${product.price}</h5>
					</div>
				</div>
			</div>
		</div>
	</div>
        `;
	return productHTML;
}

// chức năng Search theo tên 
$(document).ready(function() {
	// Gắn kết sự kiện khi người dùng nhập từ khóa
	$('#searchKeyword').on('input', function() {
		var keyword = $(this).val();
		searchProducts(keyword);
	});

	// Hàm tìm kiếm sản phẩm
	function searchProducts(keyword) {
		$.ajax({
			url: '/rest/products/search',
			type: 'GET',
			data: { keyword: keyword },
			success: function(response) {
				var Results = $('#Results');
				Results.empty(); // Xóa nội dung hiện tại

				response.forEach(function(product) {
					var productHTML = createProductHTML(product);
					Results.append(productHTML);
				});
			},
			error: function(xhr) {
				console.log(xhr.responseText);
			}
		});
	}


});

// chức năng search trong khoảng giá
$(document).ready(function() {
	// Gắn kết sự kiện submit cho biểu mẫu lọc sản phẩm
	$('#filterForm').submit(function(event) {
		event.preventDefault(); // Ngăn chặn hành vi mặc định của trình duyệt

		var minPrice = $('#minPrice').val();
		var maxPrice = $('#maxPrice').val();

		// Gửi yêu cầu lọc sản phẩm đến API
		$.ajax({
			url: '/rest/products/filter',
			type: 'GET',
			data: { minPrice: minPrice, maxPrice: maxPrice },
			success: function(response) {
				var Results = $('#Results');
				Results.empty(); // Xóa nội dung hiện tại

				response.forEach(function(product) {
					// Tạo và hiển thị sản phẩm lọc
					var productHTML = createProductHTML(product);
					Results.append(productHTML);
				});
			},
			error: function(xhr) {
				console.log(xhr.responseText);
			}
		});
	});
});


// Chức năng sắp xếp theo tên A - Z & Z -A 
$(document).ready(function() {
	// Gắn kết sự kiện click cho các thẻ 'a'
	$('#productList a').click(function(event) {
		event.preventDefault(); // Ngăn chặn hành vi mặc định của trình duyệt

		var sortType = $(this).data('sort'); // Lấy giá trị 'data-sort' của thẻ 'a'

		// Gửi yêu cầu sắp xếp sản phẩm đến API
		$.ajax({
			url: '/rest/products/sort',
			type: 'GET',
			data: { sort: sortType },
			success: function(response) {
				var Results = $('#Results');
				Results.empty(); // Xóa nội dung hiện tại

				response.forEach(function(product) {
					// Tạo và hiển thị sản phẩm lọc
					var productHTML = createProductHTML(product);
					Results.append(productHTML);
				});
			},
			error: function(xhr) {
				console.log(xhr.responseText);
			}
		});
	});
});
$(document).ready(function() {
	// Gắn kết sự kiện click cho các thẻ 'a'
	$('#productList a').click(function(event) {
		event.preventDefault(); // Ngăn chặn hành vi mặc định của trình duyệt

		var sortType = $(this).data('sortprice'); // Lấy giá trị 'data-sortprice' của thẻ 'a'

		// Gửi yêu cầu sắp xếp sản phẩm theo giá đến API
		$.ajax({
			url: '/rest/products/sortprice', // Đảm bảo rằng URL phù hợp với API của bạn
			type: 'GET',
			data: { sortprice: sortType }, // Sử dụng 'sortprice' thay vì 'sort'
			success: function(response) {
				var Results = $('#Results');
				Results.empty(); // Xóa nội dung hiện tại

				response.forEach(function(product) {
					// Tạo và hiển thị sản phẩm lọc
					var productHTML = createProductHTML(product);
					Results.append(productHTML);
				});
			},
			error: function(xhr) {
				console.log(xhr.responseText);
			}
		});
	});
});



// Chức năng giá theo khoảng 
$(document).ready(function() {
	// Gắn kết sự kiện khi người dùng chọn một trong các nút radio
	$('input[type="radio"]').change(function() {
		var priceRange = $('input[name="priceRange"]:checked').val();

		// Gửi yêu cầu lọc sản phẩm theo khoảng giá tùy chỉnh đến controller
		$.ajax({
			url: '/rest/products/filter-by-custom-price-range',
			type: 'GET',
			data: { priceRange: priceRange },
			success: function(response) {
				var Results = $('#Results');
				Results.empty(); // Xóa nội dung hiện tại

				response.forEach(function(product) {
					// Tạo và hiển thị sản phẩm lọc
					var productHTML = createProductHTML(product);
					Results.append(productHTML);
				});
			},
			error: function(xhr) {
				console.log(xhr.responseText);
			}
		});
	});
});

