app.controller("product-ctrl", function ($scope, $http) {
  $scope.items = [];
  $scope.cates = [];
  $scope.form = {};
  $scope.field = [];
  $scope.error = ["err"];

  $scope.initialize = function () {
    // Load products
    $http.get("/rest/products").then((resp) => {
      $scope.items = resp.data;
      $scope.items.forEach((item) => {
        item.publication_date = new Date(item.publication_date);
      });
    });

    // Load categories
    $http.get("/rest/categories").then((resp) => {
      $scope.cates = resp.data;
    });
  };
  // Ban đầu, searchTerm rỗng
  $scope.searchTerm = "";

  // Hàm lọc sản phẩm
  $scope.filterProducts = function (product) {
    // Kiểm tra nếu searchTerm là chuỗi con của tên sản phẩm
    return product.productname
      .toLowerCase()
      .includes($scope.searchTerm.toLowerCase());
  };

  $scope.sort = function (keyname) {
    $scope.sortKey = keyname;
    $scope.reverse = !$scope.reverse;
  };

  // Khởi đầu
  $scope.initialize();

  // Xóa form
  $scope.reset = function () {
    $scope.error = ["err"];
    $scope.form = {
      image:
        "https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png",
    };
  };

  // Hiện thị lên form
  $scope.edit = function (item) {
    $scope.form = angular.copy(item);
  };

  // Hiện thị lên for
  $scope.editthemsp = function () {
    $scope.form = angular.copy();
    $scope.form = {
      image:
        "https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png",
    };
  };

  // Thêm sản phẩm mới
  $scope.create = function () {
    var item = angular.copy($scope.form);
    $http({
      method: "POST",
      url: "/rest/products",
      data: item,
      headers: { "Content-Type": undefined },
      transformRequest: angular.identity,
    })
      .then((resp) => {
        resp.data.publication_date = new Date(resp.data.publication_date);
        $scope.items.push(resp.data);
        $scope.reset();
        // Sử dụng SweetAlert2 cho thông báo thành công
        Swal.fire({
          icon: "success",
          title: "Thành công!",
          text: "Thêm sản phẩm thành công!",
        });
      })
      .catch((error) => {
        // Sử dụng SweetAlert2 cho thông báo lỗi
        Swal.fire({
          icon: "error",
          title: "Lỗi!",
          text: "Lỗi thêm sản phẩm",
        });
        console.log("Error", error);
      });
  };

  // Cập nhật sản phẩm
  $scope.update = function () {
    var item = angular.copy($scope.form);
    $http
      .put(`/rest/products/${item.productid}`, item)
      .then((resp) => {
        var index = $scope.items.findIndex(
          (p) => p.productid == item.productid
        );
        $scope.items[index] = item;
        // Sử dụng SweetAlert2 cho thông báo thành công
        Swal.fire({
          icon: "success",
          title: "Thành công!",
          text: "Cập nhật sản phẩm thành công!",
        });
      })
      .catch((error) => {
        // Sử dụng SweetAlert2 cho thông báo lỗi
        Swal.fire({
          icon: "error",
          title: "Lỗi!",
          text: "Lỗi cập nhật sản phẩm",
        });
        console.log("Error", error);
      });
  };

  // Xóa sản phẩm
  $scope.delete = function (item) {
    $http
      .delete(`/rest/products/${item.productid}`)
      .then((resp) => {
        var index = $scope.items.findIndex(
          (p) => p.productid == item.productid
        );
        $scope.items.splice(index, 1);
        $scope.reset();
        // Sử dụng SweetAlert2 cho thông báo thành công
        Swal.fire({
          icon: "success",
          title: "Thành công!",
          text: "Xóa sản phẩm thành công!",
        });
      })
      .catch((error) => {
        // Sử dụng SweetAlert2 cho thông báo lỗi
        Swal.fire({
          icon: "error",
          title: "Lỗi!",
          text: "Lỗi xóa sản phẩm",
        });
        console.log("Error", error);
      });
  };

  $scope.pager = {
    page: 0,
    size: 10,
    get items() {
      var start = this.page * this.size;
      return $scope.items.slice(start, start + this.size);
    },
    get count() {
      return Math.ceil((1.0 * $scope.items.length) / this.size);
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
      if (this.page > this.count) {
        this.first();
      }
    },
    last() {
      this.page = this.count - 1;
    },
  };

  // Trong AngularJS controller hoặc service
  $scope.exportExcel = function () {
    $http
      .get("/excel-product", { responseType: "arraybuffer" })
      .then(function (response) {
        var blob = new Blob([response.data], {
          type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        });
        var link = document.createElement("a");
        link.href = window.URL.createObjectURL(blob);
        link.download = "product.xlsx";
        link.click();
      })
      .catch(function (error) {
        console.error("Error exporting Excel:", error);
      });
  };
  // PDF

  $scope.exportPdf = function () {
    $http
      .get("/pdf-product", { responseType: "arraybuffer" })
      .then(function (response) {
        var blob = new Blob([response.data], { type: "application/pdf" });
        var objectUrl = URL.createObjectURL(blob);
        var a = document.createElement("a");
        a.href = objectUrl;
        a.download = "product.pdf";
        a.click();
        URL.revokeObjectURL(objectUrl);
      })
      .catch(function (error) {
        console.error("Error exporting PDF:", error);
      });
  };

});

// Get references to the file input, URL input, and the preview image element
const fileInput = document.getElementById("imageFile");
const imageUrlInput = document.getElementById("imageUrl");
const previewImage = document.getElementById("previewImage");

// Function to display an image based on the URL
function displayImageUrl() {
    const imageUrl = imageUrlInput.value;
    if (imageUrl) {
        previewImage.src = imageUrl;
    } else {
        // Clear the preview image if URL is empty
        previewImage.src = "";
    }
}

// Function to display an image based on the selected file
function displayImageFile() {
    if (fileInput.files.length > 0) {
        const selectedFile = fileInput.files[0];
        const reader = new FileReader();

        reader.onload = function (e) {
            previewImage.src = e.target.result;
        };

        reader.readAsDataURL(selectedFile);
    } else {
        // Clear the preview image if no file is selected
        previewImage.src = "";
    }
}

// Add event listeners to the URL input and file input for real-time image display
imageUrlInput.addEventListener("input", displayImageUrl);
fileInput.addEventListener("change", displayImageFile);

