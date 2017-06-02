/*
    JS-файл, описывающий взаимодействие системы с пользователем.
    Код написан для фреймворка Angular JS
*/

(function() {
'use strict';

    function Order(id, firstName, secondName, phone, mail, address, products, price) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.products = products;
        this.price = price;
    }

    var orders = [];

    angular
        .module('Console', ['ngCookies'])
        .controller('ConsoleController', ConsoleController);

    ConsoleController.inject = ['$scope', '$interval', '$http', '$cookies'];
    function ConsoleController ($scope, $interval, $http, $cookies) {
        $scope.setZeroCookie = () => {
            $cookies.remove('status');
        }

        $scope.cancelOrder = (id, mail) => {
            var comment = prompt("Write reason of cancellation of order (256 chars)","OK");
            if(comment != null) {
                $http({
                    method: 'POST',
                    url: 'php-handlers/closeOrder.php',
                    cache: false,
                    params: { id: id.toString(), mail: mail.toString(), comment: comment.toString() }
                });
            }
        }

        $interval(() => {
            orders = [];
            $scope.status = $cookies.get('status');
            if($scope.status == "master") { $scope.master = true; } else { $scope.master = false; }
            $http({
                method: 'GET',
                url: 'php-handlers/getOrders.php',
                cache: false
            })
            .then(function(response) {
                var arr = angular.fromJson(response.data);
                $scope.ordersLimit = arr.length;
                angular.forEach(arr, (value, key) => {
                    orders.push(new Order(value.id, value.firstName, value.secondName, value.phone, value.mail, value.address, value.products, value.price));
                }, this);
                for(var i = 0; i < orders.length; i++) {
                    var pr = "";
                    for(var j = 0; j < angular.fromJson(orders[i].products).length; j++) {
                        pr+=angular.fromJson(orders[i].products)[j] + "\r\n";
                    }
                    //console.log(pr);
                    orders[i].products = pr;
                }
                //console.log(orders[0].products);
            });
            $scope.orders = orders;
            //console.log($scope.orders);
        }, 2000);
    }
})();