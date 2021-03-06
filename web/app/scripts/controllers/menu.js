'use strict';

/**
 * @ngdoc function
 * @name webApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the webApp
 */
angular.module('webApp')
  .controller('MenuCtrl', ['restaurant', '$scope', '$rootScope', 'order', '$location', 'ngDialog',
    function (restaurant, $scope, $rootScope, order, $location, ngDialog) {

    if (restaurant.products.length < 1) {
      $location.path("/");
      return;
    }

    $scope.products = restaurant.products;
    $scope.currentOrder = order.currentOrder;
    $scope.getTicketNumber = order.getTicketNumber;
    $scope.restaurant = restaurant.restaurant;
    $scope.getTableId = restaurant.getTableId;

    $scope.getTotal = function() {
      return order.getTotal();
    };
    $scope.getTip = function() {
      return order.getTotal() / 10;
    };

    document.myscope=$scope;

    $scope.addItem = function(i) {
      order.addItem(i);
    };

    $scope.hasTip = true;

    $scope.verifyOrder = function() {
      order.verify($scope.restaurant.id, $scope.getTableId(), function() {
        $location.path("/order/pay");
      });

    };

    $scope.productDetails = function(item) {
      $scope.showDetail = true;
      restaurant.getProductDetails(item.id, function() {
        ngDialog.open({ template: 'views/product-detail.html', data:
        {detail: restaurant.product_details, product: item } }).closePromise.then(function(dialog_return) {
          if (dialog_return.value == 'AddItem') $scope.addItem(item);
        });
      });
    };

    $scope.payOrder = function() {
      order.submit($scope.restaurant.id, $scope.getTableId(), function() {
        $location.path("/order/paid");
      });

    };

  }])
  .controller('MenuLoadingCtrl', ['$scope', 'restaurant', 'order', '$location', '$routeParams', function ($scope, restaurant, order, $location, $routeParams) {
    var restaurantId = $routeParams.rid;
    var tableId = $routeParams.tid;

    restaurant.clear();
    order.clear();

    restaurant.load(restaurantId, tableId, function() {
      $location.path('/menu');
    });

  }])
;
