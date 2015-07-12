'use strict';

/**
 * @ngdoc function
 * @name webApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the webApp
 */
angular.module('webApp')
  .controller('UserCtrl', ['user', '$scope', function (user, $scope) {
    user.load();
    $scope.orders = user.orders;
    $scope.user = user.user;

  }])
;
