package com.izanaar.digestCalc.web;

import com.izanaar.digestCalc.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    @Autowired
    private JobService taskService;

    @RequestMapping
    public String index() {
        return "index";
    }

    @RequestMapping("/new_job_modal")
    public String addJobModal(){
        return "add-job-modal";
    }
}
