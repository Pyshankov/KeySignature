'use strict';

// Declare app level module which depends on views, and components
var handWritingApp = angular.module('handWritingApp', [
  'ngRoute',
  'handWritingController'
]);

handWritingApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
    when('/registration', {
      templateUrl: 'partials/registration.html',
      controller: 'registrationCtrl'
    }).
    when('/login', {
      templateUrl: 'partials/login.html',
      controller: 'loginCtrl'
    }).
    when('/account', {
      templateUrl: 'partials/welcomePage.html',
      controller: 'welcomePageCtrl'
    }).
    otherwise({
      redirectTo: '/login'
    });
    
  }]);
