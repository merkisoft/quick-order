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
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .otherwise({
        redirectTo: '/'
      });

    $httpProvider.defaults.headers.get = {
      'x-userid' : 'blop'
    };

    RestangularProvider.setBaseUrl('http://192.168.1.47:8080/rest');
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
    var products = [];//[{"guiId":6,"name":"Salads","position":1,"products":[{"id":"1","restaurant":"2","name":"Green salad","category":"1:Salads","price":5}]},{"guiId":4,"name":"Sandwiches","position":2,"products":[{"id":"6","restaurant":"2","name":"Tuna sandwich","category":"2:Sandwiches","price":7}]},{"guiId":2,"name":"Soft drinks","position":3,"products":[{"id":"8","restaurant":"2","name":"Orange lemonade","category":"3:Soft drinks","price":4}]},{"guiId":0,"name":"Desserts","position":4,"products":[{"id":"10","restaurant":"2","name":"Muffin","category":"4:Desserts","price":3.7}]}];

    function load() {
      Restangular.all('/restaurants/2/products/groups').getList()
        .then(function (data) {
          if (data.length>0) {
            angular.copy(data, products);
          }
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
      angular.forEach(currentOrder, function(val) {
        if (val.id === item.id) {
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
          data = data;
          // $location.path("/anmeldung");
        });

    }

    return {
      submit: submit,
      addItem: addItem,
      currentOrder: currentOrder
    };

    // return Restangular.service('order');
  }])
;
