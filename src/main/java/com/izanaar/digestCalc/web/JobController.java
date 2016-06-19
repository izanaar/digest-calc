package com.izanaar.digestCalc.web;

import com.izanaar.digestCalc.repository.entity.Job;
import com.izanaar.digestCalc.repository.enums.Algo;
import com.izanaar.digestCalc.repository.enums.JobStatus;
import com.izanaar.digestCalc.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService taskService;

    @RequestMapping("/all")
    public @ResponseBody ApiResponse<List<Job>> getAll(){
        return new ApiResponse<>(true, taskService.getAll());
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ApiResponse<Job> getJob(Long id){
        return new ApiResponse<>(true, taskService.getById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ApiResponse<?> addJob(@Valid @RequestBody Job job){
        ApiResponse<?> response;

        try{
            Job addedJob = taskService.add(job);
            response = new ApiResponse<>(true, addedJob);
        }catch (Exception e){
            response = new ApiResponse<>(e.getMessage(), false, job);
        }

        return response;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public @ResponseBody ApiResponse<?> cancelJob(Long id){
        ApiResponse<?> response;

        try{
            taskService.cancel(id);
            response = new ApiResponse<>(true);
        } catch (Exception e) {
            response = new ApiResponse<>(e.getMessage(), false, id);
        }

        return response;
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public @ResponseBody ApiResponse<?> deleteJob(@PathVariable Long id){
        ApiResponse<?> response;

        try{
            taskService.delete(id);
            response = new ApiResponse<>(true);
        }catch (Exception e){
            response = new ApiResponse<>(e.getMessage(), false, id);
        }

        return response;
    }

    @RequestMapping("/algos")
    public @ResponseBody ApiResponse<?> getAlgos(){
        return new ApiResponse<>(true, Algo.values());
    }
}