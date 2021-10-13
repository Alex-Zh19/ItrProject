package com.itranzition.alex.controller;

import com.itranzition.alex.model.dto.AuthenticationDto;
import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.SignUpDto;
import com.itranzition.alex.model.dto.impl.ResponseSignUpDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.rabbitmq.Producer;
import com.itranzition.alex.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationControllerTest {
    private final String TEST_USER_EMAIL = "testUserEmail@mail.ru";
    private final String TEST_USER_NAME = "testUserName";
    private final String TEST_USER_PASSWORD = "testUserPassword";
    private final String TEST_USER_SURNAME = "testUserSurname";
    private final String TEST_USER_ROLE = "testUserRole";
    @Autowired
    private AuthenticationController authenticationController;
    @MockBean
    private Producer producer;
    @MockBean
    private AuthenticationManager manager;
    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("test return true when all application works correctly on /auth/signin request")
    void signIn() {
        repository.save(new User(1L, TEST_USER_EMAIL, TEST_USER_NAME,
                TEST_USER_PASSWORD, TEST_USER_SURNAME, TEST_USER_ROLE));
        BaseResponseDto responseDto = authenticationController.signIn(createAuthenticationDto());
        assertNotNull(responseDto);
    }

    @Test
    @DisplayName("test return true when all application works correctly on /auth/signup request")
    void signUp() {
        BaseResponseDto actualResponseDto = authenticationController.signUp(createSignUpDto());
        BaseResponseDto expectedResponseDto = createExpectedSignUpResponseDto();
        assertEquals(expectedResponseDto, actualResponseDto);
    }

    private AuthenticationDto createAuthenticationDto() {
        AuthenticationDto dto = new AuthenticationDto();
        dto.setEmail(TEST_USER_EMAIL);
        dto.setPassword(TEST_USER_PASSWORD);
        return dto;
    }

    private SignUpDto createSignUpDto() {
        SignUpDto dto = new SignUpDto();
        dto.setEmail(TEST_USER_EMAIL);
        dto.setName(TEST_USER_NAME);
        dto.setPassword(TEST_USER_PASSWORD);
        dto.setConfirmPassword(TEST_USER_PASSWORD);
        dto.setSurname(TEST_USER_SURNAME);
        return dto;
    }

    private BaseResponseDto createExpectedSignUpResponseDto() {
        ResponseSignUpDto responseSignUpDto = new ResponseSignUpDto();
        responseSignUpDto.setEmail(TEST_USER_EMAIL);
        responseSignUpDto.setName(TEST_USER_NAME);
        return responseSignUpDto;
    }
}
