var digestApp = angular.module('digestApp',[
    'ui.bootstrap'
]);


digestApp.controller('indexController', ['$scope', 'jobService', indexController]);
digestApp.controller('addTaskModalController', ['$scope', 'jobService', addTaskModalController]);

digestApp.service('jobService', ['$http', jobService]);