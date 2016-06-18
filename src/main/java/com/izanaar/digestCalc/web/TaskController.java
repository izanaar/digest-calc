package com.izanaar.digestCalc.web;

import com.izanaar.digestCalc.repository.entity.Task;
import com.izanaar.digestCalc.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping("/all")
    public @ResponseBody ApiResponse<List<Task>> getAll(){
        return new ApiResponse<>(true, taskService.getAll());
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ApiResponse<Task> getTask(Long id){
        return new ApiResponse<>(true, taskService.getById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ApiResponse<?> addTask(@Valid Task task){
        ApiResponse<?> response;

        try{
            Task addedTask = taskService.add(task);
            response = new ApiResponse<>(true, addedTask);
        }catch (Exception e){
            response = new ApiResponse<>(e.getMessage(), false, task);
        }

        return response;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public @ResponseBody ApiResponse<?> cancelTask(Long id){
        ApiResponse<?> response;

        try{
            Task responseTask = taskService.cancel(id);
            response = new ApiResponse<>(true, responseTask);
        } catch (Exception e) {
            response = new ApiResponse<>(e.getMessage(), false, id);
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody ApiResponse<?> deleteTask(Long id){
        ApiResponse<?> response;

        try{
            taskService.delete(id);
            response = new ApiResponse<>(true);
        }catch (Exception e){
            response = new ApiResponse<>(e.getMessage(), false, id);
        }

        return response;
    }

}
