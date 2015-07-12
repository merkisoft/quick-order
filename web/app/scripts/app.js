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
    'mobile-angular-ui'
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
      .when('/orders', {
        templateUrl: 'views/orders.html',
        controller: 'UserCtrl',
        controllerAs: 'userCtrl'
      })
      .when('/order/paid', {
        templateUrl: 'views/order-paid.html',
        controller: 'MenuCtrl',
        controllerAs: 'menuCtrl'
      })
      .when('/waiter', {
        templateUrl: 'views/waiter.html',
        controller: 'MenuCtrl',
        controllerAs: 'MenuCtrl'
      })
      .when('/user/profile', {
        templateUrl: 'views/userProfile.html',
        controller: 'UserCtrl',
        controllerAs: 'userCtrl'
      })
      .when('/user/paymentMethods', {
        templateUrl: 'views/paymentMethods.html',
        controller: 'UserCtrl',
        controllerAs: 'userCtrl'
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
    $httpProvider.defaults.headers.put = {
      'x-qo-userid': '2',
      'Content-Type': 'application/json'
    };

    var port = document.location.port;
    if (document.location.port == 9000) {
      port = 8080;
    }
    RestangularProvider.setBaseUrl('http://' + document.location.hostname + ':'+port+'/rest');
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
    var tableId = 0;

    function clear() {
      angular.forEach(products, function (val) {
        val.size = 0;
      });
    }

    function load(restaurantId, tid, callback) {

      if (!restaurantId) { return; }

      tableId = tid;
      Restangular.one('/restaurants/id', restaurantId).get()
        .then(function (d) {
          angular.copy(d, restaurant);

          Restangular.all('/restaurants/' + restaurantId + '/products/groups').getList()
            .then(function (data) {
              callback();
                angular.copy(data, products);
            });
        });

    }

    return {
      restaurant: restaurant,
      products: products,
      clear: clear,
      load: load,
      getTableId: function() { return tableId; }
    };

  }])

  .factory('user', ['Restangular', function (Restangular) {
    var orders = [];
    var user = {};

    function load() {
        Restangular.all('/orders/all').getList()
          .then(function (data) {
              angular.copy(data, orders);
          });

      Restangular.one('/users/id', 2).get()
        .then(function (d) {
          angular.copy(d, user);
        });

    }

    return {
      orders: orders,
      user: user,
      load: load
    };

  }])

  .factory('order', ['Restangular', function (Restangular) {
    var currentOrder = [];
    var ticketNumber = 0;
    var createdOrder = {};

    function getTotal() {
      var total = 0;
      angular.forEach(currentOrder, function (val) {
        total += val.count * val.price;
      });

      return total;
    }

    function clear() {
      angular.copy([], currentOrder);
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

    function verify(restaurantId, table, callback) {

      var items = [];
      var newCurrentOrder = [];
      angular.forEach(currentOrder, function (val) {
        if (val.count > 0) {
          items.push({productId: val.id, count: val.count});
          newCurrentOrder.push(val);
        }
      });

      angular.copy(newCurrentOrder, currentOrder);

      var order = {"restaurant": restaurantId, "table": table, "items": items};

      Restangular.all('/orders/create').post(order)
        .then(function (data) {
          ticketNumber = data.ticketNumber;
          angular.copy(data, createdOrder);
          callback();
        });

    }

    function submit(restaurantId, table, callback) {

      var items = [];
      angular.forEach(currentOrder, function (val) {
        items.push({productId: val.id, count: val.count});
      });

      Restangular.one('/orders/markAsPaid', createdOrder.id).put()
        .then(function (data) {
          angular.copy([], currentOrder);
          callback();
        });

    }

    return {
      clear: clear,
      verify: verify,
      submit: submit,
      addItem: addItem,
      currentOrder: currentOrder,
      getTotal: getTotal,
      getTicketNumber: function() { return ticketNumber; }
    };

    // return Restangular.service('order');
  }])

;
