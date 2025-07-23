package com.sanjat.enrollment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.sanjat.enrollment_service.repository")
@EnableAsync
public class EnrollmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnrollmentServiceApplication.class, args);
	}

}
