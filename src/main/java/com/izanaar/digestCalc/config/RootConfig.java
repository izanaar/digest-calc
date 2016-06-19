package com.izanaar.digestCalc.config;

import com.izanaar.digestCalc.config.data.DataSourcesConfig;
import com.izanaar.digestCalc.config.data.JpaConfig;
import com.izanaar.digestCalc.service.SecurityAspect;
import com.izanaar.digestCalc.service.UUIDKeeper;
import org.springframework.context.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

@Configuration
@ComponentScan(basePackages = {"com.izanaar.digestCalc"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
        })
@Import({DataSourcesConfig.class, JpaConfig.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class RootConfig {

    @Bean
    public ForkJoinPool threadPoolExecutor() {
        return ForkJoinPool.commonPool();
    }

    @Bean
    public SecurityAspect securityAspect(){
        return new SecurityAspect();
    }

}
