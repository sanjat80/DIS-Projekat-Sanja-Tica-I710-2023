package com.sanjat.grade_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.sanjat.grade_service.repository")
@EnableAsync
public class GradeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradeServiceApplication.class, args);
	}

}
