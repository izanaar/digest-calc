package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.repository.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private UUIDKeeper uuidKeeper;

   /* @Autowired
    private TaskRepository taskRepository;*/

    public List<Task> getAll(){
        return null;
    }

    public Task getById(Long id) {
        return null;
    }

    public Task cancel(Long id){
        return null;
    }
}
