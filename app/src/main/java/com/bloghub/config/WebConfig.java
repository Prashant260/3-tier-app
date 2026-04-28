package com.bloghub.config;

import com.bloghub.interceptor.SessionAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration// Web configuration class to register interceptors
public class WebConfig implements WebMvcConfigurer {

    private final SessionAuthInterceptor sessionAuthInterceptor;

    public WebConfig(SessionAuthInterceptor sessionAuthInterceptor) {
        this.sessionAuthInterceptor = sessionAuthInterceptor;
    }

    @Override// Register the session authentication interceptor
    public void addInterceptors(InterceptorRegistry registry) {// Add the session authentication interceptor to the registry
        registry.addInterceptor(sessionAuthInterceptor)
                .addPathPatterns("/api/**") // Apply to all /api/ paths
                .excludePathPatterns(
                        "/api/auth/**",
                        "/swagger/**",
                        "/api/docs/**",
                        "/error",
                        "/favicon.ico"
                );


    }
}
