package com.izanaar.digestCalc.service;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class SecurityAspect {

    @Autowired
    private UUIDKeeper uuidKeeper;

}
