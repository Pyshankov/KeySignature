angular.module('hello', [ 'ngRoute' ])
    .config(function($routeProvider, $httpProvider) {

        $routeProvider.when('/', {
            templateUrl : 'home.html',
            controller : 'home',
            controllerAs: 'controller'
        }).when('/login', {
            templateUrl : 'login.html',
            controller : 'navigation',
            controllerAs: 'controller'
        }).when('/register', {
                templateUrl : 'register.html',
                controller : 'register',
                controllerAs: 'controller'
            }).otherwise('/');

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

    })

    .controller('navigation',

        function($rootScope, $http, $location, $route) {

            var self = this;

            self.tab = function(route) {
                return $route.current && route === $route.current.controller;
            };

            var authenticate = function(credentials, callback) {

                var headers = credentials ? {
                    authorization : "Basic "
                    + btoa(credentials.username + ":"
                        + credentials.password)
                } : {};

                $http.get('principal', {
                    headers : headers
                }).then(function(response) {
                    if (response.data.principal.username) {
                        $rootScope.authenticated = true;
                    } else {
                        $rootScope.authenticated = false;
                    }
                    callback && callback($rootScope.authenticated);
                }, function() {
                    $rootScope.authenticated = false;
                    callback && callback(false);
                });

            }

            authenticate();

            self.credentials = {};
            self.login = function() {
                authenticate(self.credentials, function(authenticated) {
                    if (authenticated) {
                        console.log("Login succeeded")
                        $location.path("/");
                        self.error = false;
                        $rootScope.authenticated = true;
                    } else {
                        console.log("Login failed")
                        $location.path("/login");
                        self.error = true;
                        $rootScope.authenticated = false;
                    }
                })
            };

            self.logout = function() {
                $http.post('logout', {}).finally(function() {
                    $rootScope.authenticated = false;
                    $location.path("/");
                });

            };
        })

    .controller('home', function($http) {
        var self = this;

        $http.get('user/1').then(function(response) {
            self.greeting = response.data;
        })



    })
    .controller('register', function($http) {
        var self = this;

        $location.path("/register");
        console.log("reg");
    });


