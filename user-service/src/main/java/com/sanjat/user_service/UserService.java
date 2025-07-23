package com.sanjat.user_service;

import org.springframework.stereotype.Service;

import com.sanjat.user_service.repository.UserRepository;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean exstsUserByUsername(String username) {
        return repository.findByUsername(username).isPresent();
    }

}