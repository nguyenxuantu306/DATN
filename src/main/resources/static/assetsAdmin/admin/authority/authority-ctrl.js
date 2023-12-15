app.controller("authority-ctrl", function($scope, $http, $location) {
	$scope.roles = [];
	$scope.admins = [];
	$scope.authorities = [];

	$scope.initialize = function() {

		// load all roles
		$http.get("/rest/roles").then(resp => {
			$scope.roles = resp.data;
		})


		// load staffs and directors(administrators)
		$http.get("/rest/users/useradmin?admin=true").then(resp => {
			$scope.admins = resp.data;
		})

		// load authoritites of staff and directors
		$http.get("/rest/authorities?admin=true").then(resp => {
			$scope.authorities = resp.data;
		}).catch(error => {
			$location.path("/unauthorized")
		})
	}

	$scope.authority_of = function(acc, role) {
		if ($scope.authorities) {
			return $scope.authorities.find(ur => ur.user.email == acc.email && ur.role.id == role.id);
		}
	}

	$scope.authority_changed = function(acc, role) {
		var authority = $scope.authority_of(acc, role);
		if (authority) { // đã cấp quyền => thu hồi quyền xóa
			$scope.revoke_authority(authority);
		}
		else {// chưa đc cấp quyền => cấp quyền thêm mới
			authority = { user: acc, role: role };
			$scope.grant_authority(authority);
		}

	}

	// Thêm mới authority
	$scope.grant_authority = function(authority) {
		$http.post(`/rest/authorities`, authority).then(resp => {
			$scope.authorities.push(resp.data);
			// Sử dụng SweetAlert2 cho thông báo thành công
	        Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: 'cấp quyền sử dụng thành công!',
	        });
		}).catch(error => {
			Swal.fire({
	            icon: 'error',
	            title: 'Lỗi!',
	            text: 'cấp quyền sử dụng thất bại!',
	        });
			console.log("Error", error);
		});

	}


	// Xóa authority
	$scope.revoke_authority = function(authority) {
		$http.delete(`/rest/authorities/${authority.userroleid}`).then(resp => {
			var index = $scope.authorities.findIndex(a => a.userroleid == authority.userroleid);
			$scope.authorities.splice(index, 1);
			Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: 'Thu hồi quyền sử dụng thành công!',
	        });
		})
			.catch(error => {				
				Swal.fire({
	            icon: 'error',
	            title: 'Lỗi!',
	            text: 'Thu hồi quyền sử dụng thất bại!',
	        });
				console.log("Error", error);
			});

	}

	$scope.initialize();

});