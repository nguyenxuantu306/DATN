var cities = document.getElementById("city");
var districts = document.getElementById("district");
var wards = document.getElementById("ward");

var cities_add = document.getElementById("city_add");
var districts_add = document.getElementById("district_add");
var wards_add = document.getElementById("ward_add");

var parameter = {
  url: "https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json",
  method: "GET",
  responseType: "application/json",
};

axios(parameter)
  .then(function (result) {
    renderCity(result.data, cities, districts, wards);
    renderCity(result.data, cities_add, districts_add, wards_add);
  })
  .catch(function (error) {
    console.error("Request error: ", error);
  });

function renderCity(data, cityElement, districtElement, wardElement) {
  for (const city of data) {
    cityElement.options[cityElement.options.length] = new Option(
      city.Name,
      city.Name
    );
  }

  cityElement.onchange = function () {
    districtElement.length = 1;
    wardElement.length = 1;
    if (this.value !== "") {
      const selectedCity = data.find((item) => item.Name === this.value);

      for (const district of selectedCity.Districts) {
        districtElement.options[districtElement.options.length] = new Option(
          district.Name,
          district.Name
        );
      }
    }
  };

  districtElement.onchange = function () {
    wardElement.length = 1;
    const selectedCity = data.find((item) => item.Name === cityElement.value);
    if (this.value !== "") {
      const selectedDistrict = selectedCity.Districts.find(
        (item) => item.Name === this.value
      );

      for (const ward of selectedDistrict.Wards) {
        wardElement.options[wardElement.options.length] = new Option(
          ward.Name,
          ward.Name
        );
      }
    }
  };
}

function renderCityById(addressId, data, cityElement, districtElement, wardElement) {
  const selectedAddress = data.find((item) => item.Name === addressId);

  if (selectedAddress) {
    cityElement.value = selectedAddress.CityId;
    districtElement.value = selectedAddress.DistrictId;
    wardElement.value = selectedAddress.WardId;
  } else {
    // Xử lý trường hợp không tìm thấy địa chỉ
    console.error('Address not found.');
  }
}


function updateAddressStatus(addressId) {
  try {
    $.ajax({
      type: "POST",
      url: "/updateAddressStatus/" + addressId,
      success: function () {
        // Redirect to the checkout page
        window.location.href = "/checkout";
      },
      error: function (xhr, status, error) {
        console.error("Error updating address status:", error);

        // Display an error message to the user if needed
        alert("Có lỗi xảy ra khi cập nhật trạng thái địa chỉ.");
      },
    });
  } catch (exception) {
    console.error("Exception in updateAddressStatus:", exception);
  }
}

$(document).ready(function () {
  $(".update-button").on("click", function () {
    var addressId = parseInt($(this).data("address-id"));
    var userid = parseInt($(this).data("user-id"));
    var street = $(this).data("address-street");
    var ward = $(this).data("address-ward");
    var district = $(this).data("address-district");
    var city = $(this).data("address-city");
    var fullname = $(this).data("address-fullname");
    var phonenumber = $(this).data("address-phonenumber");
    var address = $(this).data("address-all");

    // Đẩy dữ liệu lên modal
    $("#myModalUpdate #addressId").val(parseInt(addressId));
    $("#myModalUpdate #userid").val(parseInt(userid));
    $("#myModalUpdate #street").val(street);
    $("#myModalUpdate #city").val(city);
    $("#myModalUpdate #district").val(district);
    $("#myModalUpdate #ward").val(ward);
    $("#myModalUpdate #fullname").val(fullname);
    $("#myModalUpdate #phonenumber").val(phonenumber);
    $("#myModalUpdate #address").val(address);

    console.log(phonenumber);

  });
});

$(document).ready(function () {
  $("#myModalUpdate #updateAddress").on("click", function () {
    var addressId = parseInt($("#myModalUpdate #addressId").val());
    var userid = parseInt($("#myModalUpdate #userid").val());
    var street = $("#myModalUpdate #street").val();
    var ward = $("#myModalUpdate #ward").val();
    var district = $("#myModalUpdate #district").val();
    var city = $("#myModalUpdate #city").val();
    var phonenumber = $("#myModalUpdate #phonenumber").val();
    var fullname = $("#myModalUpdate #fullname").val();

    // Kiểm tra dữ liệu null trước khi tạo đối tượng updateData
    if (street && ward && district && city && phonenumber && fullname) {
      // Tạo đối tượng chứa dữ liệu cần cập nhật
      var updateData = {
        street: street,
        ward: ward,
        district: district,
        city: city,
        phonenumber: phonenumber,
        fullname: fullname,
        user: {
          userid: userid,
        },
      };
      console.log(updateData);

      // Gửi yêu cầu cập nhật đến server
      $.ajax({
        type: "PUT",
        url: "/rest/addresses/update/" + addressId,
        contentType: "application/json",
        data: JSON.stringify(updateData),
        success: function (response) {
          // Xử lý phản hồi thành công
          console.log(response);
          // Đóng modal sau khi cập nhật thành công
          $("#myModalUpdate").modal("hide");
          window.location.reload();
        },
        error: function (error) {
          // Xử lý lỗi
          console.error(error);
        },
      });
    } else {
      // Thông báo nếu có dữ liệu null
      alert("Vui lòng điền đầy đủ thông tin.");
    }
  });
});


$(document).ready(function () {
  $("#myModalCreate #createAddress").on("click", function () {
    var street = $("#myModalCreate #street_add").val();
    var district = $("#myModalCreate #district_add").val();
    var city = $("#myModalCreate #city_add").val();
    var ward = $("#myModalCreate #ward_add").val();
    var phonenumber = $("#myModalCreate #phonenumber_add").val();
    var fullname = $("#myModalCreate #fullname_add").val();

    var createData = {
      street: street,
      district: district,
      city: city,
      ward: ward,
      phonenumber: phonenumber,
      fullname: fullname,
    };

    // Gửi yêu cầu tạo địa chỉ mới đến máy chủ
    $.ajax({
      type: "POST",
      url: "/rest/addresses/create",
      contentType: "application/json",
      data: JSON.stringify(createData),
      success: function (response) {
        // Xử lý phản hồi thành công
        console.log(response);
        // Đóng modal sau khi tạo thành công
        $("#myModalCreate").modal("hide");

        // Tải lại trang để cập nhật danh sách địa chỉ
        window.location.reload();
      },
      error: function (error) {
        // Xử lý lỗi
        console.error(error);
      },
    });
  });
});

$(document).ready(function () {
  // Lắng nghe sự kiện onchange từ select box tỉnh
  $("#city").on("change", function () {
    updateAddress(); // Gọi hàm cập nhật địa chỉ
  });

  // Lắng nghe sự kiện onchange từ select box huyện
  $("#district").on("change", function () {
    updateAddress(); // Gọi hàm cập nhật địa chỉ
  });

  // Lắng nghe sự kiện onchange từ select box xã
  $("#ward").on("change", function () {
    updateAddress(); // Gọi hàm cập nhật địa chỉ
  });
});

function updateAddress() {
  // Lấy giá trị từ các select box
  var selectedCity = $("#city").val();
  var selectedDistrict = $("#district").val();
  var selectedWard = $("#ward").val();

  // Tạo địa chỉ từ các giá trị đã chọn
  var addressInfo = selectedCity + ', ' + selectedDistrict + ', ' + selectedWard;

  // Hiển thị địa chỉ đã chọn vào input với id là "address" trong modal
  $("#myModalUpdate #address").val(addressInfo);

  // Kiểm tra thông tin đã được hiển thị vào input hay chưa
  console.log('Updated address:', addressInfo);

  // Kiểm tra nếu tất cả các select box đã được chọn, bật chức năng chỉnh sửa cho input street
  if (selectedCity !== "" && selectedDistrict !== "" && selectedWard !== "") {
    $("#myModalUpdate #street").prop("disabled", false);
  } else {
    // Nếu có bất kỳ select box nào chưa được chọn, vô hiệu hóa input street
    $("#myModalUpdate #street").prop("disabled", true);
  }
}


