function createProductHTML(product) {
	// Kiểm tra số lượng sản phẩm
	var outOfStockLabel = (product.quantityavailable <= 0) ? '<span style="position: absolute;font-size: 12px;top:0;right: 0;background-color: rgb(0, 0, 0);color: #ffffff;padding: 5px 10px;border: 1px solid #000;border-radius: 5px 0 0 5px; " class="out-of-stock-label">Hết hàng</span>' : '';

	var productHTML = `
        <div class="col-lg-4 col-md-6 mb-4 text-center">
            <div class=" bg-white mb-2">
                <a  href="/product/detail/${product.productid}" style="display: block; position: relative;">
                    <img style="height: 250px; border: 2px solid #7AB730; border-radius: 5px;" class="img-fluid package-item" src="${product.image}" alt="">
                    ${outOfStockLabel}
                </a>
                <div class="p-2">
                    <a
					style="font-size: 16px; color: #003C2D; text-decoration: none;"
					onmouseover="this.style.color='#FFA500'"
					onmouseout="this.style.color='#003C2D'"
					th:utext="${product.productname}" class="h5 text-decoration-none"
					href="/product/detail/${product.productid}">${product.productname}</a>
                    <div class="mt-2 pt-2">
                        <div class="text-center">                             
                            <span
							style="font-size: 20px; color: #003C2D; font-weight: bold;"
							class="">${formatPrice(product.price)}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;

	return productHTML;
}


function formatPrice(price) {
	// Thay đổi dấu chấm (.) thành dấu phẩy (,)
	const formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price);
	return formattedPrice.replace(/\./g, ',');

}

// Hàm hiển thị danh sách sản phẩm
function displayProducts(products) {
	var Results = $("#Results");
	Results.empty();

	products.forEach(function(product) {
		var productHTML = createProductHTML(product);
		Results.append(productHTML);
	});
}


// Hàm tìm kiếm sản phẩm
function searchProducts(keyword) {
	$.ajax({
		url: "/rest/products/search",
		type: "GET",
		data: { keyword: keyword },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		},
	});
}


// Hàm lọc sản phẩm theo khoảng giá
function filterProductsByPrice(minPrice, maxPrice) {
	$.ajax({
		url: "/rest/products/filter",
		type: "GET",
		data: { minPrice: minPrice, maxPrice: maxPrice },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		},
	});
}

// Hàm sắp xếp sản phẩm theo tên
function sortProductsByName(sortType) {
	$.ajax({
		url: "/rest/products/sort",
		type: "GET",
		data: { sort: sortType },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		},
	});
}

// Hàm sắp xếp sản phẩm theo giá
function sortProductsByPrice(sortType) {
	$.ajax({
		url: "/rest/products/sortprice",
		type: "GET",
		data: { sortprice: sortType },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		},
	});
}

// Hàm xử lý sự kiện khi người dùng thay đổi giá trị radio
/*function handlePriceRangeChange() {
	var priceRange = $('input[name="priceRange"]:checked').val();
	$.ajax({
		url: "/rest/products/filter-by-custom-price-range",
		type: "GET",
		data: { priceRange: priceRange },
		success: function(response) {
			displayProducts(response);
		},
		error: function(xhr) {
			console.log(xhr.responseText);
		},
	});
}
*/


$(document).ready(function() {
	// Gắn kết sự kiện khi người dùng nhập từ khóa
	$("#searchKeyword").on("input", function() {
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
	$("#productList a[data-sort]").click(function(event) {
		event.preventDefault();
		var sortType = $(this).data("sort");
		sortProductsByName(sortType);
	});

	// Gắn kết sự kiện click cho các thẻ 'a' sắp xếp theo giá
	$("#productList a[data-sortprice]").click(function(event) {
		event.preventDefault();
		var sortType = $(this).data("sortprice");
		sortProductsByPrice(sortType);
	});

	/*// Gắn kết sự kiện khi người dùng thay đổi giá trị radio
	$('input[type="radio"]').change(handlePriceRangeChange);*/
});




// rating và chức năng cơ bản tour
$(document).ready(function() {
	$('.slider').slick({
		slidesToShow: 4, // Số lượng phần tử hiển thị
		slidesToScroll: 1, // Số lượng phần tử được trượt khi bạn nhấn nút trượt
		arrows: true, // Hiển thị nút trượt
		speed: 300
		// Tốc độ trượt (ms)
	});

});

// Mini Cart

// JavaScript to handle the mini cart toggle using jQuery
/*$(document).ready(function() {
	const cartToggle = $("#cart-toggle");
	const miniCart = $("#mini-cart");

	// Initially hide the mini cart
	miniCart.hide();

	cartToggle.click(function() {
		miniCart.toggle();
	});
});*/

// AddToCart

function addToCart(productId) {
	var quantity = document.getElementById('quantityInput').value;
	var quantityavailable = parseFloat(document.getElementById('quantityavailable').innerText);

	if (quantity > quantityavailable) {
		Swal.fire({
			icon: 'error',
			title: 'Lỗi!',
			text: 'Số lượng vượt quá số lượng có sẵn',
		});
		return; // Ngừng thực hiện hàm nếu số lượng không hợp lệ
	} else if (isNaN(quantity) || parseFloat(quantity) < 0.5) {
		Swal.fire({
			icon: 'error',
			title: 'Lỗi!',
			text: 'Số lượng phải lớn hơn hoặc bằng 0.5Kg',
		});
		return; // Ngừng thực hiện hàm nếu số lượng không hợp lệ
	}

	$.ajax({
		type: "POST",
		url: "/cart/add/" + productId,
		data: {
			productId: productId,
			quantity: quantity  // Truyền số lượng vào data để gửi đến Controller
		},
		success: function(response) {
			Swal.fire({
				icon: 'success',
				title: 'Thành công!',
				text: 'Thêm sản phẩm thành công!',
			});
		},
		error: function(error) {
			window.location.href = "http://localhost:8080/login";
		},
	});
}


//xóa 1 sản phẩm trong giỏ hàng
function removeCart(productId) {
	$.ajax({
		type: "POST",
		url: "/cart/remove/" + productId,
		data: {
			productId: productId,
		},
		success: function(response) {
			window.location.reload();
		},
		error: function(error) {
			console.error("Error remove product to cart:", error);
		},
	});
}
//xóa tất cả giỏ hàng
function removeAllItemsFromCart() {
	$.ajax({
		type: "POST",
		url: "/cart/remove/all", // Endpoint để xóa tất cả sản phẩm
		success: function(response) {
			window.location.reload();
		},
		error: function(error) {
			console.error("Error removing all items from cart:", error);
		},
	});
}
//-1 số lượng giỏ hàng
function decreaseQuantity(buttonElement) {
	var quantityInput = buttonElement.nextElementSibling;
	var productId = quantityInput.getAttribute('data-productid');
	var newQuantity = parseInt(quantityInput.value) - 1;

	if (newQuantity >= 1) {
		quantityInput.value = newQuantity;
		updateQuantity(productId, newQuantity);
	} else {
		removeCart(productId);
	}
}
//+1 số lượng giỏ hàng
function increaseQuantity(buttonElement) {
	var quantityInput = buttonElement.previousElementSibling;
	var productId = quantityInput.getAttribute('data-productid'); // Sửa đổi ở đây
	var newQuantity = parseInt(quantityInput.value) + 1;

	quantityInput.value = newQuantity;
	updateQuantity(productId, newQuantity);
}

// thay đổi số lượng
function updateQuantity(productId, newQuantity) {
	$.ajax({
		type: "POST",
		url: "/cart/update/" + productId + "?quantity=" + newQuantity,
		success: function(data) {
			window.location.reload();
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log(jqXHR.responseText);
		}
	});
}

// Lấy tất cả các trường số lượng
var quantityInputs = document.querySelectorAll('.cart-quantity');
var quantityavailable = document.getElementById('quantityavailable').innerText;

// Lặp qua từng trường số lượng và thêm bộ lắng nghe sự kiện cho mỗi trường
quantityInputs.forEach(function(quantityInput) {
	quantityInput.addEventListener("keyup", function(event) {
		// Kiểm tra nếu phím Enter đã được nhấn (event.key === "Enter")
		if (event.key === "Enter") {
			var productId = quantityInput.getAttribute('data-productid');
			var newQuantity = parseFloat(quantityInput.value);
			if (!isNaN(newQuantity) && newQuantity >= 0.5) {
				if (newQuantity <= parseFloat(quantityavailable)) {
					updateQuantity(productId, newQuantity);
				} else {
					Swal.fire({
						icon: 'error',
						title: 'Lỗi!',
						text: 'Số lượng vượt quá số lượng có sẵn: ' + quantityavailable + '(Kg)',
					});
					window.location.reload()
				}
			} else {
				Swal.fire({
					icon: 'error',
					title: 'Lỗi!',
					text: 'Số lượng phải lớn hơn hoặc bằng 0.5Kg',
				});
				window.location.reload()
			}
		}
	});
});

//select cod & paypal

function showButton(option) {
	var checkoutButton = document.getElementById('checkout-button');
	var paypalButton = document.getElementById('paypal-button');
	var vnpayButton = document.getElementById('vnpay-button');

	if (option === 'debit') {
		checkoutButton.style.display = 'block';
		paypalButton.style.display = 'none';
		vnpayButton.style.display = 'none';
	} else if (option === 'paypal') {
		checkoutButton.style.display = 'none';
		paypalButton.style.display = 'block';
		vnpayButton.style.display = 'none';
	} else {
		checkoutButton.style.display = 'none';
		vnpayButton.style.display = 'block';
		paypalButton.style.display = 'none';
	}
}


