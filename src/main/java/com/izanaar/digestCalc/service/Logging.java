package com.izanaar.digestCalc.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class Logging {

    private Logger logger = LoggerFactory.getLogger("DigestCalcLogger");

    @Pointcut("execution (* com.izanaar.digestCalc.digest.JobStatusListener.notifyStart(Long)) && args(id)")
    public void logStart(Long id){}

    @Pointcut("execution (* com.izanaar.digestCalc.digest.JobStatusListener.notifySuccess(Long, String)) && args(id, hex)")
    public void logSuccess(Long id, String hex){}

    @Pointcut("execution (* com.izanaar.digestCalc.digest.JobStatusListener.notifyFailure(Long, String)) && args(id, stackTrace)")
    public void logFailure(Long id, String stackTrace){}

    @Before("logStart(id)")
    public void logJobStart(Long id){
        logger.trace("Digest calculation for job {} has started", id);
    }

    @Before("logSuccess(id, hex)")
    public void logJobSuccess(Long id, String hex){
        logger.trace("Digest calculation for job {} has ended: {}", id, hex);
    }

    @Before("logFailure(id, stackTrace)")
    public void logJobFailure(Long id, String stackTrace){
        logger.trace("Digest calculation for job {} has ended with an exception: \n{}", id, stackTrace);
    }
}
