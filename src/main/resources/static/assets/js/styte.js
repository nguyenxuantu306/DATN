// Hàm tạo mã HTML cho một sản phẩm
function createProductHTML(product) {
	var productHTML = `
              <div 
		class="col-lg-4 col-md-6 mb-4">
		<div class="package-item bg-white mb-2">
			<a href="/product/detail/${product.productid}"><img
				style="height: 250px" class="img-fluid"
				src="${product.image}" alt=""></a>
			<div class="p-4">
				<a 
					class="h5 text-decoration-none" >${product.productname}</a>
				<div class="border-top mt-4 pt-4">
					<div class="d-flex justify-content-between">
						<h6 class="m-0">
							<i class="fa fa-star text-primary mr-2"></i>4.5 <small>(250)</small>
						</h6>
						<h5 class="m-0">${product.price} đ</h5>

					</div>
				</div>
			</div>
		</div>
	</div>
        `;
	return productHTML;
}

// Hàm tạo mã HTML cho một sản phẩm
function createTourHTML(tour) {
	var tourHTML = `
              <div 
		class="col-lg-4 col-md-6 mb-4">
		<div class="package-item bg-white mb-2">
			<a th:href="@{|/tour/detail/${tour.tourid}|}"><img
				style="height: 350px; width: 100%" class="img-fluid"
				src="${tour.image}" alt=""></a>
			<div class="p-4">
				<a 
					class="h5 text-decoration-none" >${tour.tourname}</a>
				<div class="border-top mt-4 pt-4">
					<div class="d-flex justify-content-between">
						<h6 class="m-0">
							<i class="fa fa-star text-primary mr-2"></i>4.5 <small>(250)</small>
						</h6>
						<h5
				
							class="m-0">${tour.pricings.Adultprice}</h5>
					</div>
				</div>
			</div>
		</div>
	</div>
        `;
	return tourHTML;
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

// Hàm hiển thị danh sách tour
function displayTours(tours) {
	var tourResults = $('#tourResults');
	tourResults.empty();

	tours.forEach(function(tour) {
		var tourHTML = createTourHTML(tour);
		tourResults.append(tourHTML);
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

// Hàm tìm kiếm Tour theo tên
function searchTours(keyword) {
	$.ajax({
		url: '/rest/tours/searchtour',
		type: 'GET',
		data: { keyword: keyword },
		success: function(response) {
			displayTours(response);
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

	// Gắn kết sự kiện khi người dùng nhập từ khóa searchtour
	$('#searchtour').on('input', function() {
		searchTours($(this).val());
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

/*document.getElementById("priceFilter").addEventListener("change", function () {
    var selectedPrice = this.value;
    var tourItems = document.querySelectorAll(".package-item");

    for (var i = 0; i < tourItems.length; i++) {
        var tourPrice = parseFloat(tourItems[i].querySelector(".p-4 h5").innerText.replace('đ', '').replace(/\s/g, '').replace(',', ''));
        var priceRange = selectedPrice.split("-");
        var minPrice = parseFloat(priceRange[0]);
        var maxPrice = parseFloat(priceRange[1]);

        if (selectedPrice === "0" || (tourPrice >= minPrice && tourPrice <= maxPrice)) {
            tourItems[i].style.display = "block";
            tourItems[i].classList.add("hidden");
        } else {
            tourItems[i].style.display = "none";
            
        }
    }
});*/
