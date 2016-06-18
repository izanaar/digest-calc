function digestController($scope, digestService) {

    $scope.greeting = "HELLO";

    digestService.getTasks().then(function (response) {
        console.log(response);
        $scope.tasks = response.data.content;

    }, function () {
        alert('Tasks haven\'t been loaded');
    });
}