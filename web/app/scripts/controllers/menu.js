'use strict';

/**
 * @ngdoc function
 * @name webApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the webApp
 */
angular.module('webApp')
  .controller('MenuCtrl', ['products', '$scope', 'order', '$location', '$routeParams', function (products, $scope, order, $location, $routeParams) {

    $scope.products = products.products;
    $scope.currentOrder = order.currentOrder;
    $scope.ticketNumber = order.ticketNumber;
    $scope.restaurantName = "Vivendi" + $routeParams.rid;

    $scope.getTotal = function() {
      return order.getTotal();
    };

    document.myscope=$scope;

    $scope.addItem = function(i) {
      order.addItem(i);
    };

    $scope.verifyOrder = function() {
      $location.path("/order/pay");
    };

    $scope.payOrder = function() {
      order.submit();
      $location.path("/order/paid");
    };

  }])
;
