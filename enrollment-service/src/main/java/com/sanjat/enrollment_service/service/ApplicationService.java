package com.sanjat.enrollment_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanjat.enrollment_service.model.Application;
import com.sanjat.enrollment_service.repository.ApplicationRepository;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository repository;

    public ApplicationService(ApplicationRepository repository) {
        this.repository = repository;
    }

    public List<Application> getAllAplications() {
        return repository.findAll();
    }
}
