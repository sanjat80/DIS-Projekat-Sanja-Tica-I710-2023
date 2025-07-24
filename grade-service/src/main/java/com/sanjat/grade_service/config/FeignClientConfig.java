package com.sanjat.grade_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignClientConfig {
    @Value("${service.auth.token}")
    private String serviceAuthToken;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("X-Service-Auth", serviceAuthToken);
            }
        };
    }
}
