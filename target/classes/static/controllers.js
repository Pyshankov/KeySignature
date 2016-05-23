'use strict';

/* Controllers */

var handWritingController = angular.module('handWritingController', []);

handWritingController.controller('welcomePageCtrl', ['$scope', '$http' ,'$rootScope','$location','$cookies',
  function($scope, $http , $rootScope,$location,$cookies) {

    $rootScope.authenticated = $cookies.get('isAuth');
    if($rootScope.authenticated==false) $location.path('login');


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
      $scope.keyEventsTime.push(new Date().getTime()-self.lastKeyUpTime);   ;
      self.lastKeyUpTime=new Date().getTime();
      console.log(new Date().getTime());
      $scope.user.keyHandWriting=$scope.keyEventsTime

    };
    
    $scope.registerUser=function(){
      if($scope.textToVerify!=self.textToReproduce){
        $scope.errorMes=true;
        $scope.errorMessage="text dosen't match";
        return;
      }

      $scope.user={
        "userName" : $scope.user.userName
        ,"keyHandWriting" : $scope.user.keyHandWriting.slice(1).toString()
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


handWritingController.controller('loginCtrl', ['$scope',  '$http' , '$location' , '$rootScope','$cookies',
  function($scope,$http,$location,$rootScope,$cookies) {

    
    var self = this;
    self.textToReproduce="I certify this action as my own original act in accordance with site Honor Code.";
    $scope.keyEventsLoginTime=[];
    $scope.textToVerifyLogin="";
    $scope.errorMessageLogin="";
    self.lastKeyUpTimeLogin=new Date().getTime();
    $scope.credentials ={};
    $scope.keyUpEventLogin = function(){
      $scope.keyEventsLoginTime.push(new Date().getTime()-self.lastKeyUpTimeLogin);   ;
      self.lastKeyUpTimeLogin=new Date().getTime();
      console.log(new Date().getTime());
      $scope.credentials.keyHandWriting=$scope.keyEventsLoginTime
    };

    $scope.authenticate = function () {

      if($scope.textToVerifyLogin!=self.textToReproduce){
        $scope.errorMesLogin=true;
        $scope.errorMessageLogin="text dosen't match";
        return;
      }
      self.user={
        "userName" : $scope.credentials.username
        ,"keyHandWriting" : $scope.credentials.keyHandWriting.slice(1).toString()
      }
      var usr = JSON.stringify(self.user);
      var res = $http.post("http://localhost:9001/api/logIn",usr);

      res.success(function(data, status, headers, config) {
        $rootScope.authenticated=data.authenticated;
        if( $rootScope.authenticated===true){
          $cookies.put("isAuth",$rootScope.authenticated);
          $location.path('/account');
        }
        console.log($rootScope.authenticated);
      });

      res.error(function(data, status, headers, config) {
        $scope.errorMesLogin=true;
        $scope.errorMessageLogin=data.message;
      });
    }
    
    $rootScope.logout = function(){
      var res = $http.post("http://localhost:9001/api/logOut");
      
      res.success(function(data, status, headers, config) {
        $rootScope.authenticated=data.authenticated;
        if( $rootScope.authenticated===false){
          $cookies.put("isAuth",$rootScope.authenticated);
          $location.path('/login');
        }
        console.log($rootScope.authenticated);
      });

    }

    
  }]);