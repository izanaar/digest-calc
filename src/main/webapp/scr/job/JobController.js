function jobController($scope, $uibModal, jobModel) {


    function initController() {
        $scope.jobs = jobModel.getJobs();
    }

    function assignFunctionsToScope() {
        $scope.isAbleToCancel = isAbleToCancel;
        $scope.openJobAddingModal = openJobAddingModal;
        $scope.updateJobs = jobModel.updateJobs;
        $scope.updateJob = jobModel.updateJob;
        $scope.cancelJob = jobModel.cancelJob;
        $scope.deleteJob = jobModel.deleteJob;
        $scope.showStackTrace = showStackTrace;
    }

    function isAbleToCancel(status) {
        return (status === "WAITING") || (status === "PENDING");
    }

    function openJobAddingModal() {
        $uibModal.open({
            controller: 'addJobModalController',
            templateUrl: '/new_job_modal'
        });
    }
    
    function showStackTrace(job) {
        $uibModal.open({
            templateUrl: '/stack_trace_modal',
            controller: 'stackModalController',
            size:'lg',
            resolve: {
                job: job
            }
        });
    }
    
    initController();
    assignFunctionsToScope();
}