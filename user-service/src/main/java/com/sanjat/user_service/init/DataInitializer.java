package com.sanjat.user_service.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sanjat.user_service.model.Role;
import com.sanjat.user_service.model.User;
import com.sanjat.user_service.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository repo, PasswordEncoder encoder) {
        this.userRepository = repo;
        this.passwordEncoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setEmail("admin@email.com");
            userRepository.save(admin);

            User professor = new User();
            professor.setUsername("prof");
            professor.setPassword(passwordEncoder.encode("prof123"));
            professor.setRole(Role.PROFESSOR);
            professor.setEmail("prof@email.com");
            userRepository.save(professor);

            User student = new User();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setRole(Role.STUDENT);
            student.setEmail("student@email.com");
            userRepository.save(student);
        }
    }
}
