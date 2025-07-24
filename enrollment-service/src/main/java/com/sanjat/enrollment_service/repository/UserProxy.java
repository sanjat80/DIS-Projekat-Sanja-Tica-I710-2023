package com.sanjat.enrollment_service.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sanjat.enrollment_service.config.FeignClientConfig;
import com.sanjat.enrollment_service.dtos.UserDto;

@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface UserProxy {
    @GetMapping("/users/username/{username}")
    UserDto findUserByUsername(@PathVariable("username") String username);

    @GetMapping("/users/id/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping("/users/email/{email}")
    UserDto getUserByEmail(@PathVariable String email);

}
