package com.izanaar.digestCalc.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.RecursiveAction;


@Service
public class RecursiveActionExecutionService {

    void executeAction(RecursiveAction action, Long taskID){

    }

    boolean tryCancelActionExecutuion(Long taskId){
        return false;
    }
}
