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

  .config(function ($routeProvider, RestangularProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .otherwise({
        redirectTo: '/'
      });


    RestangularProvider.setBaseUrl('http://192.168.1.2:8080/rest');
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

    RestangularProvider.setDefaultHttpFields({cache:false});

    RestangularProvider.setRequestSuffix('');


  })


  .factory('products', ['Restangular', function (Restangular) {
    var products = [];

    function load() {
      Restangular.all('/restaurants/2/products/groups').getList()
        .then(function (data) {
          angular.copy(data, products);
        });

    }

    load();

    return {
      products: products
    };

  }])

  .factory('order', ['Restangular', function(Restangular) {
    var currentOrder = [];

    function addItem(item) {
      var found = false;
      angular.forEach(currentOrder, function(val, i) {
        if (val.id == item.id) {
          val.size++;
          found = true;
        }
      });

      if (!found) {
        item.size = 1;
        currentOrder.push(item);
      }

    }

    function submit() {
      Restangular.all('/order').post(currentOrder)
        .then(function(data) {
          // $location.path("/anmeldung");
        });

    }

    return {
      addItem: addItem,
      currentOrder: currentOrder
    };

    // return Restangular.service('order');
  }])
;
