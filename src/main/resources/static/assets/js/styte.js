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
				<a th:utext="${product.productname}"
					class="h5 text-decoration-none" href="/tour-detail"></a>
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

// Hàm hiển thị danh sách sản phẩm
function displayProducts(products) {
	var Results = $('#Results');
	Results.empty();

	products.forEach(function(product) {
		var productHTML = createProductHTML(product);
		Results.append(productHTML);
	});
}

// Hàm tìm kiếm sản phẩm
function searchProducts(keyword) {
	$.ajax({
		url: '/rest/products/search',
		type: 'GET',
		data: { keyword: keyword },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		}
	});
}

// Hàm lọc sản phẩm theo khoảng giá
function filterProductsByPrice(minPrice, maxPrice) {
	$.ajax({
		url: '/rest/products/filter',
		type: 'GET',
		data: { minPrice: minPrice, maxPrice: maxPrice },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		}
	});
}

// Hàm sắp xếp sản phẩm theo tên
function sortProductsByName(sortType) {
	$.ajax({
		url: '/rest/products/sort',
		type: 'GET',
		data: { sort: sortType },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		}
	});
}

// Hàm sắp xếp sản phẩm theo giá
function sortProductsByPrice(sortType) {
	$.ajax({
		url: '/rest/products/sortprice',
		type: 'GET',
		data: { sortprice: sortType },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		}
	});
}

// Hàm xử lý sự kiện khi người dùng thay đổi giá trị radio
function handlePriceRangeChange() {
	var priceRange = $('input[name="priceRange"]:checked').val();
	$.ajax({
		url: '/rest/products/filter-by-custom-price-range',
		type: 'GET',
		data: { priceRange: priceRange },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		}
	});
}

$(document).ready(function() {
	// Gắn kết sự kiện khi người dùng nhập từ khóa
	$('#searchKeyword').on('input', function() {
		searchProducts($(this).val());
	});

	// Gắn kết sự kiện submit cho biểu mẫu lọc sản phẩm
	$('#filterForm').submit(function(event) {
		event.preventDefault();
		var minPrice = $('#minPrice').val();
		var maxPrice = $('#maxPrice').val();
		filterProductsByPrice(minPrice, maxPrice);
	});

	// Gắn kết sự kiện click cho các thẻ 'a' sắp xếp theo tên
	$('#productList a[data-sort]').click(function(event) {
		event.preventDefault();
		var sortType = $(this).data('sort');
		sortProductsByName(sortType);
	});

	// Gắn kết sự kiện click cho các thẻ 'a' sắp xếp theo giá
	$('#productList a[data-sortprice]').click(function(event) {
		event.preventDefault();
		var sortType = $(this).data('sortprice');
		sortProductsByPrice(sortType);
	});

	// Gắn kết sự kiện khi người dùng thay đổi giá trị radio
	$('input[type="radio"]').change(handlePriceRangeChange);
});
