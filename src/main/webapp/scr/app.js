var digestApp = angular.module('jobTrackerApp',[
    'ui.bootstrap',
    'ui-notification'
]);


digestApp.controller('indexController', ['$scope', '$uibModal', 'jobService', 'Notification', indexController]);
digestApp.controller('addTaskModalController', ['$scope', 'jobService', addTaskModalController]);

digestApp.service('jobService', ['$http', jobService]);