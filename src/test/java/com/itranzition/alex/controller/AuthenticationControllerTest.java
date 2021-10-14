package com.itranzition.alex.controller;

import com.itranzition.alex.model.dto.AuthenticationDto;
import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.SignUpDto;
import com.itranzition.alex.model.dto.impl.ResponseSignUpDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.rabbitmq.Producer;
import com.itranzition.alex.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationControllerTest {
    private static final String TEST_EMAIL = "testUserEmail@mail.ru";
    private static final String TEST_NAME = "testUserName";
    private static final String TEST_PASSWORD = "testUserPassword";
    private static final String TEST_SURNAME = "testUserSurname";
    private static final String TEST_ROLE = "testUserRole";
    private static User testUser;
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationController authenticationController;
    @MockBean
    private Producer producer;
    @MockBean
    private AuthenticationManager manager;
    @Autowired
    private UserRepository repository;

    @BeforeAll
    static void setUpUserInformation() {
        System.out.println("66 fuck");
        testUser = new User(1L, TEST_EMAIL, TEST_NAME,
                TEST_PASSWORD, TEST_SURNAME, TEST_ROLE);
    }

    @AfterEach
    void cleanUpDatabase() {
        repository.delete(testUser);
    }

    @Test
    @DisplayName("test return true when all application works correctly on /auth/signin request")
    void signIn() {
        repository.save(testUser);
        try {
            mockMvc.perform(put("/api/auth/signin").contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\"email\": \"" + TEST_EMAIL + "\", \"password\": \"" + TEST_PASSWORD + "\"}")).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // BaseResponseDto responseDto = authenticationController.signIn(createAuthenticationDto());
       // assertNotNull(responseDto);
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
        dto.setEmail(TEST_EMAIL);
        dto.setPassword(TEST_PASSWORD);
        return dto;
    }

    private SignUpDto createSignUpDto() {
        SignUpDto dto = new SignUpDto();
        dto.setEmail(TEST_EMAIL);
        dto.setName(TEST_NAME);
        dto.setPassword(TEST_PASSWORD);
        dto.setConfirmPassword(TEST_PASSWORD);
        dto.setSurname(TEST_SURNAME);
        return dto;
    }

    private BaseResponseDto createExpectedSignUpResponseDto() {
        ResponseSignUpDto responseSignUpDto = new ResponseSignUpDto();
        responseSignUpDto.setEmail(TEST_EMAIL);
        responseSignUpDto.setName(TEST_NAME);
        return responseSignUpDto;
    }
}
