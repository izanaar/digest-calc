package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.repository.entity.Job;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Aspect
public class SecurityAspect {

    @Pointcut("execution(* com.izanaar.digestCalc.service.JobService.notifyStart(Long)) && args(id)")
    public void notifyStart(Long id){}


    @Before("notifyStart(id)")
    public void appendUUID(Long id){
        System.out.println("Silencing cell phones before the performance.");
    }


}
