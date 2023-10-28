app.controller("product-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.cates = [];
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	
	$scope.initialize = function(){
		// Load products
		$http.get("/rest/products").then(resp =>{
			$scope.items = resp.data;
			$scope.items.forEach(item =>{
				item.publication_date = new Date(item.publication_date)
			})
		});
		
		// Load categories
		$http.get("/rest/categories").then(resp =>{
			$scope.cates = resp.data;
			
		});
		
	}
	
	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}
	
	// Khởi đầu
	$scope.initialize();
	
	// Xóa form
	$scope.reset = function(){
		$scope.error = ['err'];
		$scope.form = {			
			image:'https://cdn.pixabay.com/photo/2017/01/18/17/39/cloud-computing-1990405_1280.png',			
		};
		$('#id').attr('readonly', false);
		$('#btn-create').removeAttr('disabled');
		$('#btn-update').attr('disabled', 'disabled');
		$('#btn-delete').attr('disabled', 'disabled');
	}
	
	// Hiện thị lên form
	$scope.edit = function(item){
		$scope.form = angular.copy(item);	
		$('#btn-create').attr('disabled', 'disabled');
		$('#btn-delete').removeAttr('disabled');
		$('#btn-update').removeAttr('disabled');				
	}
	
	// Thêm sản phẩm mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/products`, item).then(resp => {
			resp.data.publication_date = new Date(resp.data.publication_date)
			$scope.items.push(resp.data);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
	        Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: 'Thêm sản phẩm thành công!',
	        });
		}).catch(error => {
			// Sử dụng SweetAlert2 cho thông báo lỗi
	        Swal.fire({
	            icon: 'error',
	            title: 'Lỗi!',
	            text: 'Lỗi thêm sản phẩm',
	        });
			console.log("Error", error);
		});
	}

	
	// Cập nhật sản phẩm
	$scope.update = function(){
		var item = angular.copy($scope.form);
		$http.put(`/rest/products/${item.productid}`,item).then(resp =>{
			var index = $scope.items.findIndex(p => p.productid == item.productid);
			$scope.items[index] = item;				
			// Sử dụng SweetAlert2 cho thông báo thành công
	        Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: 'Cập nhật sản phẩm thành công!',
	        });
		})
		.catch(error =>{
			// Sử dụng SweetAlert2 cho thông báo lỗi
	        Swal.fire({
	            icon: 'error',
	            title: 'Lỗi!',
	            text: 'Lỗi cập nhật sản phẩm',
	        });
			console.log("Error", error);
		});
	}
	
	// Xóa sản phẩm 
	$scope.delete = function(item){
		$http.delete(`/rest/products/${item.productid}`).then(resp =>{
			var index = $scope.items.findIndex(p => p.productid == item.productid);
			$scope.items.splice(index,1);
			$scope.reset();				
			// Sử dụng SweetAlert2 cho thông báo thành công
	        Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: 'Xóa sản phẩm thành công!',
	        });
		})
		.catch(error =>{
			// Sử dụng SweetAlert2 cho thông báo lỗi
	        Swal.fire({
	            icon: 'error',
	            title: 'Lỗi!',
	            text: 'Lỗi xóa sản phẩm',
	        });
			console.log("Error", error);
		});
	}
	
	$scope.pager = {
		page:0,
		size:5,
		get items(){
			var start = this.page*this.size;
			 return $scope.items.slice(start,start + this.size);
		},
		get count(){
			return Math.ceil(1.0 * $scope.items.length / this.size);
		},
		first(){
			this.page = 0;
		},
		prev(){
			this.page--;
			if(this.page < 0){
				this.last();
			}
		},
		next(){
			this.page++;
			if(this.page > this.count){
				this.first();
			}
		},
		last(){
			this.page = this.count-1;
		}		
	}
	
	$(function() {
		$("#example1").DataTable({
			"ajax": {
				"url": "/rest/products",
				"dataSrc": ""
			},
			"columns": [
				{ "data": "id" },
				{ "data": "name" },
				{ "data": "price" },
				{ "data": "publication_date" },
				{ "data": "image" },
				{ "data": "author" },
				{ "data": "genres" },
				{ "data": "available" },
				{
					/*"data": null,
					"render": function(data, type, row) {
						return '<button class="btn btn-success" ng-click="edit(' + row.id + ')">Xem chi tiết</button>';
					}*/
				}, 

				// Add more column configurations as needed
			],
			"responsive": true, "lengthChange": false, "autoWidth": false,
		}).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
	});


// Trong AngularJS controller hoặc service
	$scope.exportExcel = function() {
		$http.get('/excel-product', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				link.download = 'product.xlsx';
				link.click();
			})
			.catch(function(error) {
				console.error('Error exporting Excel:', error);
			});
	};
	
	// PDF
		
		$scope.exportPdf = function() {
		$http.get('/pdf-product', { responseType: 'arraybuffer' })
			.then(function(response) {
				var blob = new Blob([response.data], { type: 'application/pdf' });
				var objectUrl = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = objectUrl;
				a.download = 'pdfProduct.pdf';
				a.click();
				URL.revokeObjectURL(objectUrl);
			})
			.catch(function(error) {
				console.error('Error exporting PDF:', error);
			});
	};
	
});

	
