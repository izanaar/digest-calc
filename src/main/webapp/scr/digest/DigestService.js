function digestService($http) {
    this.getTasks = function () {
        return $http.get("/task/all");
    }
}