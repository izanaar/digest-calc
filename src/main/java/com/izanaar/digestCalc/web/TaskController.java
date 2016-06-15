package com.izanaar.digestCalc.web;

import com.izanaar.digestCalc.repository.entity.Task;
import com.izanaar.digestCalc.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping("/all")
    private @ResponseBody List<Task> getAll(){
        return taskService.getAll();
    }

}
