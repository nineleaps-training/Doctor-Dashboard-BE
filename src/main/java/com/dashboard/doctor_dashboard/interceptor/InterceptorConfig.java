package com.dashboard.doctor_dashboard.interceptor;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private static final String[] ALLOWED_URL = {           //  all the allowed Apis which doesn't need any authentication to run
            // Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/", "/csrf",
            "/api/v1/user/login",
            "/api/v1/user/refresh-token",
            "/files/**",
            "/api/receptionist/**",
            "/actuator/**"


    };


    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor).excludePathPatterns(ALLOWED_URL);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
       registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
    }
}
