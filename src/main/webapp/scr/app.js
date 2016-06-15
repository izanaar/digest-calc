var digestApp = angular.module('digestApp',[
    'ngMaterial',
    'ngRoute'
]);


digestApp.controller('indexController', function ($scope) {
   $scope.greeting = "Hi there.";
});

