package com.sanjat.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

        @Bean
        public RouteLocator routes(RouteLocatorBuilder builder) {
                return builder.routes()
                                .route("user-service-route", r -> r
                                                .path("/user-service/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://user-service")) // koristi Eureka i load balancer
                                .route("course-service-route", r -> r
                                                .path("/course-service/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://course-service"))
                                .route("enrollment-service-route", r -> r
                                                .path("/enrollment-service/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://enrollment-service"))
                                .route("grade-service-route", r -> r
                                                .path("/grade-service/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://grade-service"))
                                .build();
        }

}
