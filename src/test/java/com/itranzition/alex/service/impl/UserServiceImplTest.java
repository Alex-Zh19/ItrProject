package com.itranzition.alex.service.impl;

import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final String USER_EMAIL = "testEmail@mail.ru";
    private static final String USER_ROLE = "USER";
    private static final String USER_PASSWORD = "testPassword";
    private static final String USER_NAME = "john";
    private static final String USER_SURNAME = "doe";

    @Mock
    private UserRepository userRepository;
    @Spy
    private BCryptPasswordEncoder encoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("test return true when user added to database correctly")
    void addUser() {
        User expectedUser = new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        User actualUser = userService.addUser(expectedUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("test return true when user was found at database correctly")
    void findUserByEmail() {
        User expectedUser = new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
        when(userRepository.findByEmail(any(String.class))).thenReturn(expectedUser);
        User actualUser = userService.findUserByEmail(USER_EMAIL);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("test return true when method throws exception on user absence at db")
    void findUserByEmailWithExceptionThrowing() {
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        assertThrows(BadCredentialsException.class, () -> userService.findUserByEmail(USER_EMAIL));
    }

    @Test
    @DisplayName("test return true when method throws exception on null email")
    void findUserByEmailWithBadCredentialsExceptionThrowingNullEmail() {
        assertThrows(BadCredentialsException.class, () -> userService.findUserByEmail(null));
    }

    @Test
    @DisplayName("test return true when method throws exception on blank email")
    void findUserByEmailWithBadCredentialsExceptionThrowingBlankEmail() {
        assertThrows(BadCredentialsException.class, () -> userService.findUserByEmail("  "));
    }

    @Test
    void existsByEmail() {
        when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(true);
        assertTrue(userService.existsByEmail(USER_EMAIL));
    }
}
