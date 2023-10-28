document.getElementById("statusSelect").addEventListener("change", function() {
    // Lấy giá trị đã chọn trong <select>
    var selectedStatus = this.value;
    
    // Gọi API để lấy danh sách đơn hàng dựa trên tên trạng thái
    fetch("http://localhost:8080/rest/orders/byStatusName?statusName=" + selectedStatus)
        .then(response => response.json())
        .then(data => {
            // Xử lý dữ liệu trả về và hiển thị danh sách đơn hàng
            var orderListDiv = document.getElementById("orderList");
            orderListDiv.innerHTML = ""; // Xóa nội dung cũ

            if (data.length === 0) {
                orderListDiv.innerHTML = "Không có đơn hàng nào ở trạng thái này.";
            } else {
                var ul = document.createElement("ul");
                data.forEach(function(order) {
                    var li = document.createElement("li");
                    li.textContent = "Mã đơn hàng: " + order.orderid + ", Ngày đặt: " + order.orderdate + ", Địa chỉ: " + order.Address;
                    ul.appendChild(li);
                });
                orderListDiv.appendChild(ul);
            }
        })
        .catch(error => console.error("Lỗi khi gọi API: " + error));
});