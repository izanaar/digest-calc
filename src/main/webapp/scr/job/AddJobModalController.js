function addJobModalController($scope, $uibModalInstance, jobModel) {
    $scope.algos = jobModel.getAlgos();
    $scope.job = {
        algo: $scope.algos[0],
        srcUrl: "file:///home/traum/file"
    };

    $scope.add = function () {
        jobModel.addJob($scope.job);
        $uibModalInstance.close();
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    }
}