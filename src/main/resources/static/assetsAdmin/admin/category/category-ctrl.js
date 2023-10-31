app.controller("category-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.cates = [];
	$scope.form = {};
	$scope.field = [];
	$scope.error = ['err'];
	
	
	
	$scope.initialize = function(){
		// Load products
		$http.get("/rest/categories").then(resp =>{
			$scope.items = resp.data;
		});		
	}
	
	// Khởi đầu
	$scope.initialize();
	
	// Xóa form
	$scope.reset = function(){
		/*$scope.error = ['err'];*/
		$scope.form = {
			publication_date:new Date(),
			image:'cloud-upload.jpg',
			available:true
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
	
	
	// Thêm loại sản phẩm mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/categories`,item).then(resp => {
			resp.data.publication_date = new Date(resp.data.publication_date)
			$scope.items.push(resp.data);
			$scope.reset();
			// Sử dụng SweetAlert2 cho thông báo thành công
	        Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: 'Thêm loại sản phẩm thành công!',
	        });
		}).catch(error => {
			// Sử dụng SweetAlert2 cho thông báo lỗi
	        Swal.fire({
	            icon: 'error',
	            title: 'Lỗi!',
	            text: 'Lỗi thêm loại sản phẩm',
	        });
			console.log("Error", error);
		});
	}

	
	// cập loại nhật sản phẩm
	$scope.update = function(){
		var item = angular.copy($scope.form);
		$http.put(`/rest/categories/${item.categoryid}`,item).then(resp =>{
			var index = $scope.items.findIndex(p => p.categoryid == item.categoryid);
			$scope.items[index] = item;				
			// Sử dụng SweetAlert2 cho thông báo thành công
	        Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: 'Cập nhật loại sản phẩm thành công!',
	        });
		})
		.catch(error =>{
			// Sử dụng SweetAlert2 cho thông báo lỗi
	        Swal.fire({
	            icon: 'error',
	            title: 'Lỗi!',
	            text: 'Lỗi Cập nhật loại sản phẩm',
	        });
			console.log("Error",error);
		});
	}
	
	// Xóa loại sản phẩm 
	$scope.delete = function(item){
		$http.delete(`/rest/categories/${item.categoryid}`).then(resp =>{
			var index = $scope.items.findIndex(p => p.categoryid == item.categoryid);
			$scope.items.splice(index,1);
			$scope.reset();				
			// Sử dụng SweetAlert2 cho thông báo thành công
	        Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: 'Xóa loại sản phẩm thành công!',
	        });	  
		})
		.catch(error =>{
			// Sử dụng SweetAlert2 cho thông báo lỗi
	        Swal.fire({
	            icon: 'error',
	            title: 'Lỗi!',
	            text: 'Lỗi loại sản phẩm',
	        });	       
			console.log("Error",error);
		});
	}
	

	
	$scope.pager = {
		page:0,
		size:3,
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
});

	