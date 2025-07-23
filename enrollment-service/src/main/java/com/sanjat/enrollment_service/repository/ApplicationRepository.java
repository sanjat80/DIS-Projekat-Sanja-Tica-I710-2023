package com.sanjat.enrollment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sanjat.enrollment_service.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

}
