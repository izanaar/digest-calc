function jobService($http) {
    this.getJobs = function () {
        return $http.get("/job/all");
    }
}