'use strict';

/**
 * @ngdoc overview
 * @name webApp
 * @description
 * # webApp
 *
 * Main module of the application.
 */
angular
  .module('webApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'restangular',
    'mobile-angular-ui.core'
  ])

  .config(function ($routeProvider, RestangularProvider, $httpProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/test', {
        templateUrl: 'views/qr.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/restaurant/:rid/table/:tid', {
        templateUrl: 'views/menu-loading.html',
        controller: 'MenuLoadingCtrl',
        controllerAs: 'menuCtrl'
      })
      .when('/menu', {
        templateUrl: 'views/menu.html',
        controller: 'MenuCtrl',
        controllerAs: 'menuCtrl'
      })
      .when('/order/pay', {
        templateUrl: 'views/order-pay.html',
        controller: 'MenuCtrl',
        controllerAs: 'menuCtrl'
      })
      .when('/order/paid', {
        templateUrl: 'views/order-paid.html',
        controller: 'MenuCtrl',
        controllerAs: 'menuCtrl'
      })
      .when('/waiter', {
        templateUrl: 'views/waiter.html',
        controller: 'MenuCtrl',
        controllerAs: 'menuCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });

    $httpProvider.defaults.headers.get = {
      'x-qo-userid': '2'
    };

    $httpProvider.defaults.headers.post = {
      'x-qo-userid': '2',
      'Content-Type': 'application/json'
    };

    RestangularProvider.setBaseUrl('http://' + document.location.hostname + ':8080/rest');
    // RestangularProvider.setExtraFields(['name']);
    RestangularProvider.setResponseExtractor(function (response) {
      /* if (response.error) {
       alert(response.error);
       return null;
       }
       return response.data;
       */
      return response;
    });

    RestangularProvider.setDefaultHttpFields({cache: false});

    RestangularProvider.setRequestSuffix('');


  })

  .filter('itemCount', function () {
    return function (input) {
      return input.count > 0;
    };
  })

  .factory('restaurant', ['Restangular', function (Restangular) {
    var products = [];
    var restaurant = {};

    function load(restaurantId, tableId, callback) {
      var restaurant = Restangular.one('/restaurants/id', restaurantId).get()
        .then(function (d) {
          angular.copy(d, restaurant);

          Restangular.all('/restaurants/' + restaurantId + '/products/groups').getList()
            .then(function (data) {
              callback();
              if (data.length > 0) {
                angular.copy(data, products);
              }
            });
        });

    }

    return {
      restaurant: restaurant,
      products: products,
      load: load
    };

  }])

  .factory('order', ['Restangular', function (Restangular) {
    var currentOrder = [];
    var ticketNumber = "";

    function getTotal() {
      var total = 0;
      angular.forEach(currentOrder, function (val) {
        total += val.count * val.price;
      });

      return total;
    }

    function addItem(item) {
      var found = false;
      angular.forEach(currentOrder, function (val) {
        if (val.id === item.id) {
          val.count++;
          found = true;
        }
      });

      if (!found) {
        item.count = 1;
        currentOrder.push(item);
      }

    }

    function submit() {

      var items = [];
      angular.forEach(currentOrder, function (val) {
        items.push({productId: val.id, count: val.count});
      });

      var order = {'restaurant': "1", 'table': 11, 'items': items};

      Restangular.all('/orders/create').post(order)
        .then(function (data) {
          ticketNumber = data.ticketNumber;
          // $location.path("/anmeldung");
        });

    }

    return {
      submit: submit,
      addItem: addItem,
      currentOrder: currentOrder,
      getTotal: getTotal,
      ticketNumber: ticketNumber
    };

    // return Restangular.service('order');
  }])
;
