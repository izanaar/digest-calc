function jobModel($http, Notification) {

    var jobs = [],
        algos = [];

    updateModel();
    updateAlgos();

    function updateModel() {
        $http.get("/job/all").then(getJobsSuccessCallback, getJobsFailureCallback);
    }

    function getJobsSuccessCallback(response) {
        var apiResponse = response.data;
        if (apiResponse.status) {
            while (jobs.length > 0) {
                jobs.pop();
            }
            apiResponse.content.forEach(function (job) {
                jobs.push(job);
            });

            console.log("job list has been updated. count: " + apiResponse.content.length);
        } else {
            Notification.error("Couldn't get job list. Reason: " + apiResponse.message);
        }
    }

    function getJobsFailureCallback() {
        Notification.error("Unknown error occurred while getting job list.")
    }


    function updateAlgos() {
        $http.get("/job/algos").then(getAlgosSuccessCallback, getAlgosFailureCalback);
    }

    function getAlgosSuccessCallback(response) {
        response.data.content.forEach(function (algo) {
            algos.push(algo);
        });
    }

    function getAlgosFailureCalback(error) {
        Notification.error("Error" + error.status + " occurred while getting algos list.");
    }


    this.getJobs = function () {
        return jobs;
    };

    this.updateJobs = function () {
        updateModel();
    };

    this.updateJob = function (id) {
        Notification.primary("Update job " + id + ".");
    };

    this.cancelJob = function (id) {
        Notification.primary("Cancel job" + id + ".")
    };

    this.deleteJob = function (id) {
        var url = "/job/" + id;
        console.log(url);
        $http.delete(url).then(deleteJobSuccessCallback, deleteJobFailureCallback)
    };

    function deleteJobSuccessCallback(response) {
        var apiResponse = response.data;
        if (apiResponse.status) {
            updateModel();
        } else {
            Notification.error("Couldn't delete job. " + apiResponse.message);
        }
    }

    function deleteJobFailureCallback(response) {
        Notification.error("Error" + response.status + " occurred while deleting job.");
    }

    this.addJob = function (newJob) {
        $http.post("/job", newJob)
            .then(addJobSuccessCallback, addJobFailureCallback);
    };

    function addJobSuccessCallback(response) {
        var apiResponse = response.data;
        if (apiResponse.status) {
            jobs.push(apiResponse.content);
        } else {
            Notification.error("Error occurred while adding job." + apiResponse.message);
        }
    }

    function addJobFailureCallback(error) {
        Notification.error("Error" + error.status + " occurred while adding job.");
    }

    function getJobIndex(jobId) {
        for (var i = 0; i < jobs.length; i++) {
            if (jobs[i].id === jobId) {
                return i;
            }
        }
        return -1;
    }

    this.getAlgos = function () {
        return algos;
    }
}