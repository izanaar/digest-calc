package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.digest.DigestTaskFactory;
import com.izanaar.digestCalc.digest.TaskStatusListener;
import com.izanaar.digestCalc.exception.TaskServiceException;
import com.izanaar.digestCalc.repository.TaskRepository;
import com.izanaar.digestCalc.repository.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements TaskStatusListener{

    @Autowired
    private UUIDKeeper uuidKeeper;

    @Autowired
    private DigestTaskFactory factory;

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAll(){
        return taskRepository.findAll();
    }

    public Task getById(Long id) {
        return null;
    }

    public Task add(Task task){
        return null;
    }

    public Task cancel(Long id) throws TaskServiceException{
        return null;
    }

    public void delete(Long id)  throws TaskServiceException{

    }

    @Override
    public void notifySuccess(Long id, String hex) {

    }

    @Override
    public void notifyFailure(Long id, String stackTrace) {

    }
}
