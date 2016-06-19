function stackTraceModalController($scope, $uibModalInstance, job) {
    $scope.job = job;
    $scope.close = $uibModalInstance.close;
}