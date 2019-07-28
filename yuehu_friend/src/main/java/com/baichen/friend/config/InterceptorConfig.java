package com.baichen.friend.config;

import com.baichen.friend.filter.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Program: ApplicationConfig
 * @Author: baichen
 * @Description: 拦截器配置类
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Autowired
    private JwtInterceptor jwtFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtFilter).
                addPathPatterns("/**").
                excludePathPatterns("/**/login");
    }
}
