function indexController($scope, $uibModal, jobService, Notification) {

    $scope.greeting = "HELLO";

    jobService.getJobs().then(function (response) {
        console.log(response);
        $scope.jobs = response.data.content;
    }, function () {
        alert('Tasks haven\'t been loaded');
    });

    Notification.success("Success!");
}