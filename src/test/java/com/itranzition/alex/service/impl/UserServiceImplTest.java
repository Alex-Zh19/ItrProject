package com.itranzition.alex.service.impl;

import com.itranzition.alex.config.UserServiceTestConfiguration;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.repository.UserRepository;
import com.itranzition.alex.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserServiceTestConfiguration.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
class UserServiceImplTest {
    private final String USER_EMAIL = "testEmail@mail.ru";
    private final String USER_ROLE = "USER";
    private final String USER_PASSWORD = "testPassword";
    private final String USER_NAME="john";
    private final String USER_SURNAME="doe";

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void addUser() {
        User expectedUser = new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);

        when(encoder.encode(any(CharSequence.class))).thenReturn(USER_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        User actualUser = userService.addUser(expectedUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findUserByEmail() {
        User expectedUser = new User((long) 1,USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);

        when(encoder.encode(any(CharSequence.class))).thenReturn(USER_PASSWORD);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(expectedUser));

        User actualUser=userService.findUserByEmail(USER_EMAIL);
        assertEquals(expectedUser,actualUser);
    }
}