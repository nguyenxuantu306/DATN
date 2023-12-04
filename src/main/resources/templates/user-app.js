app = angular.module("user-app", ["ngRoute"]);

app.config(function($routeProvider) {
    $routeProvider
        .when("/address", {
            templateUrl: "/layout/address/address.html",
            controller: "address-ctrl"
        })
        .otherwise({
            redirectTo: "/address" // Chuyển hướng mặc định đến "/address"
        });
});
