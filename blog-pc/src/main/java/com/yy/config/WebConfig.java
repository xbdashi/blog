package com.yy.config;

import com.yy.intercepted.SaTokenIntercepted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SaTokenIntercepted saTokenIntercepted;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(saTokenIntercepted)
                .excludePathPatterns("/code","/**/login");
    }
}
