package com.izanaar.digestCalc.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.RecursiveAction;


@Service
public class RecursiveActionExecutionService {

    void executeAction(RecursiveAction action, Long jobId){

    }

    boolean tryCancelActionExecutuion(Long jobId){
        return false;
    }
}
