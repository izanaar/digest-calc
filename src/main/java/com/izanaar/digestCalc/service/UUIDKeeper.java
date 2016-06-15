package com.izanaar.digestCalc.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

@Component(value = "uuid")
@Scope(value= WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UUIDKeeper {

    private String value;

    public UUIDKeeper() {
        value = String.valueOf(UUID.randomUUID());
    }

    public String getValue() {
        return value;
    }
}
