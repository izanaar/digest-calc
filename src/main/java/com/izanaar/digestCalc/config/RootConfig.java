package com.izanaar.digestCalc.config;

import com.izanaar.digestCalc.config.data.DataSourcesConfig;
import com.izanaar.digestCalc.config.data.JpaConfig;
import com.izanaar.digestCalc.service.Logging;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.ForkJoinPool;

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
    public Logging securityAspect(){
        return new Logging();
    }

}
