package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.exception.JobServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


@Service
public class RecursiveActionExecutionService {

    private Map<Long, RecursiveAction> actionMap;

    @Autowired
    private ForkJoinPool pool;

    public RecursiveActionExecutionService() {
        actionMap = new HashMap<>();
    }

    void executeAction(RecursiveAction action, Long jobId){
        actionMap.put(jobId, action);
        pool.execute(action);
    }

    boolean tryCancelActionExecutuion(Long jobId){
        Optional<RecursiveAction> recursiveActionOptional =
                Optional.ofNullable(actionMap.get(jobId));

        if(recursiveActionOptional.isPresent()){
            return recursiveActionOptional.get().cancel(true);
        }else {
            throw new JobServiceException("Couldn't cancel job. Invalid id: " + jobId);
        }
    }
}
