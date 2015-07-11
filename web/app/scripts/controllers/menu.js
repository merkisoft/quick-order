'use strict';

/**
 * @ngdoc function
 * @name webApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the webApp
 */
angular.module('webApp')
  .controller('MenuCtrl', ['restaurant', '$scope', 'order', '$location', function (restaurant, $scope, order, $location) {

    $scope.products = restaurant.products;
    $scope.currentOrder = order.currentOrder;
    $scope.getTicketNumber = order.getTicketNumber;
    $scope.restaurant = restaurant.restaurant;

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
      order.submit($scope.restaurant.id, $scope.tableId);
      $location.path("/order/paid");
    };

  }])
  .controller('MenuLoadingCtrl', ['$scope', 'restaurant', '$location', '$routeParams', function ($scope, restaurant, $location, $routeParams) {
    var restaurantId = $routeParams.rid;
    var tableId = $routeParams.tid;

    restaurant.load(restaurantId, tableId, function() {
      $location.path('/menu');
    });

  }])
;
