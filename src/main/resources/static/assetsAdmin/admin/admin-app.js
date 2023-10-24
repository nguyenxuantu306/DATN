app = angular.module("admin-app", ["ngRoute"]);

app.config(function($routeProvider) {
	$routeProvider
		.when("/product", {
			templateUrl: "/assetsAdmin/admin/product/shopManagement.html",
			controller: "product-ctrl"
		})
		.when("/tour", {
			templateUrl: "/assetsAdmin/admin/tour/tourManagement.html",
			controller: "tour-ctrl"
		})
		.when("/category", {
			templateUrl: "/assetsAdmin/admin/category/category.html",
			controller: "category-ctrl"
		})
		.when("/user", {
			templateUrl: "/assetsAdmin/admin/user/user.html",
			controller: "user-ctrl"
		})
		.when("/order", {
			templateUrl: "/assetsAdmin/admin/order/order.html",
			controller: "order-ctrl"
		}).when("/productS", {
			templateUrl: "/assetsAdmin/admin/statistics/_productstatistics.html",
			controller: 'productstatistics-ctrl'
		}).when("/tyte", {
			templateUrl: "/assetsAdmin/admin/statistics/_tytestatistics.html",
			controller: 'tytestatistics-ctrl'
		}).when("/orderS", {
			templateUrl: "/assetsAdmin/admin/statistics/_orderstatistics.html",
			controller: 'orderstatistics-ctrl'
		}).when("/authorize", {
			templateUrl: "/assetsAdmin/admin/authority/authority.html",
			controller: "authority-ctrl"
		})
		.when("/unauthorized", {
			templateUrl: "/assetsAdmin/admin/authority/index.html",
			controller: "authority-ctrl"
		})
		.when("/inventory", {
			templateUrl: "/assetsAdmin/admin/statistics/_inventorystatistics.html",
			controller: "inventorystatistics-ctrl"
		})
		.when("/revenue", {
			templateUrl: "/assetsAdmin/admin/statistics/_revenuestatistics.html",
			controller: "revenue-statistics-ctrl"
		})
		.when("/dashboard", {
			templateUrl: "/assetsAdmin/admin/homeAdmin.html",
			controller: "thongke-ctrl"
		})

		.otherwise({
			templateUrl: "/assetsAdmin/admin/homeAdmin.html",
			controller: "thongke-ctrl"
		});
});