package com.sanjat.user_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sanjat.user_service.UserService;
import com.sanjat.user_service.model.User;
import com.sanjat.user_service.repository.UserRepository;

public class UserServiceTest {

    private UserRepository repository;
    private UserService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        service = new UserService(repository);
    }

    @Test
    void testExistsUserByUsernameExists() {
        String username = "existingUser";
        User user = new User();
        user.setUsername(username);

        when(repository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = service.exstsUserByUsername(username);

        assertTrue(result);
        verify(repository).findByUsername(username);
    }

    @Test
    void testExistsUserByUsernameNotExists() {
        String username = "nonExistingUser";

        when(repository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = service.exstsUserByUsername(username);

        assertFalse(result);
        verify(repository).findByUsername(username);
    }
}
