var digestApp = angular.module('jobTrackerApp', [
    'ui.bootstrap',
    'ui-notification'
]);


digestApp.controller('jobController', ['$scope', '$uibModal', 'jobModel', jobController]);
digestApp.controller('addJobModalController', ['$scope', '$uibModalInstance', 'jobModel', addJobModalController]);
digestApp.controller('stackModalController', ['$scope', '$uibModalInstance', 'job', stackTraceModalController]);

digestApp.service('jobModel', ['$http', '$interval', 'Notification', jobModel]);