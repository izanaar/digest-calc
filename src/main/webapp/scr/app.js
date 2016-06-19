var digestApp = angular.module('jobTrackerApp',[
    'ui.bootstrap',
    'ui-notification'
]);


digestApp.controller('jobController', ['$scope', '$uibModal', 'jobModel', jobController]);
digestApp.controller('addJobModalController', ['$scope', '$uibModalInstance','jobModel', addJobModalController]);

digestApp.service('jobModel', ['$http','Notification', jobModel]);