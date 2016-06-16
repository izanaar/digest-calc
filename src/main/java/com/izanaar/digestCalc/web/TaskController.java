package com.izanaar.digestCalc.web;

import com.izanaar.digestCalc.repository.dto.TaskDTO;
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
    public @ResponseBody List<Task> getAll(){
        return taskService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody TaskDTO addTask(@Valid TaskDTO task){
        System.out.println(task);
        return task;
    }
}
