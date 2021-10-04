package com.itranzition.alex.service.impl;

import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.repository.UserRepository;
import com.itranzition.alex.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class UserServiceImplTest {
    private final String USER_EMAIL = "testEmail@mail.ru";
    private final String USER_ROLE = "USER";
    private final String USER_PASSWORD = "testPassword";
    private final String USER_NAME = "john";
    private final String USER_SURNAME = "doe";

    private UserService userService;
    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        encoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, encoder);
    }

    @Test
    void shouldReturnTrueOnAddingUserToBase() {
        User expectedUser = new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        User actualUser = userService.addUser(expectedUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldReturnOkOnFindingUserAtBase() {
        User expectedUser = new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.findUserByEmail(USER_EMAIL);
        assertEquals(expectedUser, actualUser);
    }
}
