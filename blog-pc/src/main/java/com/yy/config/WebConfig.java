package com.yy.config;

import com.yy.intercepted.ContextIntercept;
import com.yy.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private ContextIntercept contextIntercept;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("登录状态需要判断是否拦截");
            String[] excludePatterns = new String[]{"/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**",
                    "/api", "/api-docs", "/api-docs/**", "/doc.html/**", "/code", "/**/login", "/webjars/**"};
            registry.addInterceptor(contextIntercept).excludePathPatterns(excludePatterns);
    }
}
