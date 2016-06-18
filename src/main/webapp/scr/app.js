var digestApp = angular.module('digestApp',[
    'ui.bootstrap'
]);


digestApp.controller('digestController', ['$scope', 'digestService', digestController]);
digestApp.service('digestService', ['$http', digestService]);