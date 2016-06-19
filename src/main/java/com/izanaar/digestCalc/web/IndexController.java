package com.izanaar.digestCalc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    @RequestMapping
    public String index() {
        return "index";
    }

    @RequestMapping("/new_job_modal")
    public String addJobModal(){
        return "add-job-modal";
    }

    @RequestMapping("/stack_trace_modal")
    public String stackTraceModal(){
        return "stack-trace-modal";
    }
}
