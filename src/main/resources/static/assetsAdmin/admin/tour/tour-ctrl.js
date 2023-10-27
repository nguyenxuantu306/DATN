app.controller("tour-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.form = {};
	
	$scope.initialize = function(){
		// Load tours
		$http.get("/rest/tours").then(resp =>{
			$scope.items = resp.data;
			$scope.items.forEach(item =>{
				item.startdate = new Date(item.startdate),
				item.enddate = new Date(item.enddate)
			})
		});
	
	}
	// Khởi đầu
	$scope.initialize();
	
	// Hiện thị lên form
	$scope.edit = function(item){
		$scope.form = angular.copy(item);		
	}
	
	// Xóa form
	$scope.reset = function(){
		$scope.error = ['err'];
		$scope.form = {
			/*publication_date:new Date(),
			image:'cloud-upload.jpg',
			available:true,*/
		};
		$('#id').attr('readonly', false);
		$('#btn-create').removeAttr('disabled');
		$('#btn-update').attr('disabled', 'disabled');
		$('#btn-delete').attr('disabled', 'disabled');
	}
	$scope.pager = {
		page:0,
		size:4,
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

	
