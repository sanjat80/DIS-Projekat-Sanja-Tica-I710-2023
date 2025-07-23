package com.sanjat.user_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sanjat.user_service.model.User;
import com.sanjat.user_service.repository.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = repository.findAll();
        return ResponseEntity.ok(users);
    }

    // this endpoint is being used by enrollment-service
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // this endopoint is being used by enrollment-service
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        return repository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // this endpoint is being used by enrollment-service
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return repository.findByEmail(email)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return repository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setRole(user.getRole());
                    // Dodaj ostala polja ako ima još
                    User updatedUser = repository.save(existingUser);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
