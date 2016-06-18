function jobService($http) {
    this.getJobs = function () {
        return $http.get("/task/all");
    }
}