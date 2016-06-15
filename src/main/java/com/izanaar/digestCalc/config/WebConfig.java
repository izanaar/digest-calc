package com.izanaar.digestCalc.config;

import com.izanaar.digestCalc.web.IndexController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = IndexController.class)
public class WebConfig {

}
