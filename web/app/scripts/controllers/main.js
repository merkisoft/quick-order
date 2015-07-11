'use strict';

/**
 * @ngdoc function
 * @name webApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the webApp
 */
angular.module('webApp')
  .controller('MainCtrl', ['products', '$scope', 'order', function (products, $scope, order) {

    $scope.products = products.products;
    $scope.currentOrder = order.currentOrder;

    document.myscope=$scope;

    $scope.addItem = function(i) {
      order.addItem(i);

    };


  }])
;
