app.controller('revenueindex-ctrl', function($scope, $http) {
	$scope.form = {};
	$scope.itemsThongKeSTstatus = [];
	$scope.itemsThongKestats = [];
	
	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}
	
	$scope.initialize = function() {
		
		$http.get('/rest/orders/slstatus').then(response => {
			$scope.itemsThongKeSTstatus = response.data;
			console.log($scope.itemsThongKeSTstatus);
			
		})	
		
		$http.get('/rest/rating/stats').then(response => {
			$scope.itemsThongKestats = response.data;
			console.log($scope.itemsThongKestats);
			
		})	
		
	}
	
	$scope.calculateTotalCount = function() {
    let totalCount = 0;
    for (let i = 0; i < $scope.itemsThongKeSTstatus.length; i++) {
        totalCount += $scope.itemsThongKeSTstatus[i].count;
    }
    return totalCount;
	};
	
	$scope.calculateTotalCountstats = function() {
    let totalCount = 0;
    for (let i = 0; i < $scope.itemsThongKestats.length; i++) {
        totalCount += $scope.itemsThongKestats[i].count;
    }
    return totalCount;
	};
	
			$(function() {
			var newCust = [ [ 4, 1 ], [ 5, 3 ], [ 6, 6 ], [ 7, 5 ], [ 8, 7 ],
					[ 9, 8 ], [ 10, 10 ], ];
			var retCust = [ [ 4, 1 ], [ 5, 2 ], [ 6, 5 ], [ 7, 3 ], [ 8, 5 ],
					[ 9, 6 ], [ 10, 9 ], ];

			var plot = $.plot($("#flotLine1"), [ {
				data : newCust,
				label : "New Customer",
				color : "#ff5e5e",
			}, {
				data : retCust,
				label : "Returning Customer",
				color : "#7571F9",
			}, ], {
				series : {
					lines : {
						show : true,
						lineWidth : 1,
					},
					shadowSize : 0,
				},
				points : {
					show : false,
				},
				legend : {
					noColumns : 1,
					position : "nw",
				},
				grid : {
					hoverable : true,
					clickable : true,
					borderColor : "#ddd",
					borderWidth : 0,
					labelMargin : 5,
					backgroundColor : "transparent",
				},
				yaxis : {
					min : 0,
					max : 15,
					color : "transparent",
					font : {
						size : 10,
						color : "#999",
					},
				},
				xaxis : {
					color : "transparent",
					font : {
						size : 10,
						color : "#999",
					},
				},
			});
		});
			
	$scope.initialize();
})