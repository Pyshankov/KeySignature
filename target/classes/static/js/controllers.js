'use strict';

/* Controllers */

var handWritingController = angular.module('handWritingController', []);

handWritingController.controller('welcomePageCtrl', ['$scope', '$http',
  function($scope, $http) {

    $http.get('http://localhost:9001/user/1').success(function(data) {
      $scope.user = data;
    });

  }]);

handWritingController.controller('registrationCtrl', ['$scope','$http' ,'$routeParams', '$location',
  function($scope,$http, $routeParams,$location) {

    var self=this;

    $scope.textToVerify="";
    $scope.keyEventsTime=[];
    $scope.user={};
    $scope.errorMes=false;
    
    self.textToReproduce="I certify this action as my own original act in accordance with site Honor Code.";
    self.lastKeyUpTime=new Date().getTime();

    $scope.keyUpEvent = function(){
      $scope.keyEventsTime.push(new Date().getTime()-self.lastKeyUpTime);
      self.lastKeyUpTime=new Date().getTime();
      console.log(new Date().getTime());
      $scope.user.keyHandWriting=$scope.keyEventsTime.toString()

    };
    
    $scope.registerUser=function(){
      if($scope.textToVerify!=self.textToReproduce){
        $scope.errorMes=true;
        $scope.errorMessage="text dosen't match";
        return;
      }

      $scope.user={
        "userName" : $scope.user.userName
        ,"keyHandWriting" : $scope.user.keyHandWriting.toString()
      }

      var usr = JSON.stringify($scope.user);
     var res = $http.post("http://localhost:9001/user",usr);

      res.success(function(data, status, headers, config) {
        $location.path('/login');
      //  $scope.message = data;
      });

      res.error(function(data, status, headers, config) {
        $scope.errorMes=true;
        $scope.errorMessage=data.message;
      });

    }

  }]);


handWritingController.controller('loginCtrl', ['$scope',  '$http' , '$location' , '$rootScope',
  function($scope,$http,$location,$rootScope) {

    var self = this;
    self.textToReproduce="I certify this action as my own original act in accordance with site Honor Code.";
    $scope.keyEventsTime=[];
    $scope.textToVerify="";
    self.lastKeyUpTime=new Date().getTime();

    $scope.keyUpEvent = function(){
      $scope.keyEventsTime.push(new Date().getTime()-self.lastKeyUpTime);   ;
      self.lastKeyUpTime=new Date().getTime();
      console.log(new Date().getTime());
      $scope.credentials.keyHandWriting=$scope.keyEventsTime.toString()

    };

    var authenticate = function(credentials, callback) {

      var headers = credentials ? {authorization : "Basic "
      + btoa(credentials.username + ":" + ""),
        userHandWriting : credentials.keyHandWriting
      } : {};

      $http.get('http://localhost:9001/principal', {headers : headers}).then(function(response) {
        if (response.data.name) {
          $rootScope.authenticated = true;
        } else {
          $rootScope.authenticated = false;
        }
        callback && callback();
      }, function() {
        $rootScope.authenticated = false;
        callback && callback();
      });

    }

    authenticate();
    $scope.credentials = {};

   $scope.login = function() {
      authenticate(self.credentials, function() {
        if ($rootScope.authenticated) {
          $location.path("/account");
          self.error = false;
        } else {
          $location.path("/login");
          self.error = true;
        }
      });
    };



  }]);