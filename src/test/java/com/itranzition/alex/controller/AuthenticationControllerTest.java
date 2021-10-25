package com.itranzition.alex.controller;

import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.rabbitmq.Producer;
import com.itranzition.alex.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    private static final String TEST_EMAIL = "testUserEmail@mail.ru";
    private static final String TEST_NAME = "testUserName";
    private static final String TEST_PASSWORD = "testUserPassword";
    private static final String TEST_SURNAME = "testUserSurname";
    private static final String TEST_ROLE = "testUserRole";
    private static final String AUTHENTICATION_JSON =
            String.format("{\"email\": \"%s\", \"password\": \"%s\"}", TEST_EMAIL, TEST_PASSWORD);
    private static final String SIGN_UP_JSON =
            String.format("{\"email\": \"%s\", \"name\": \"%s\", \"password\": \"%s\", \"confirmPassword\": \"%s\", \"surname\": \"%s\"}",
                    TEST_EMAIL, TEST_NAME, TEST_PASSWORD, TEST_PASSWORD, TEST_SURNAME);
    private static User testUser;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Producer producer;
    @MockBean
    private AuthenticationManager manager;
    @Autowired
    private UserRepository repository;

    @BeforeAll
    static void setUpUserInformation() {
        testUser = new User(1L, TEST_EMAIL, TEST_NAME,
                TEST_PASSWORD, TEST_SURNAME, TEST_ROLE);
    }

    @AfterEach
    void cleanUpDatabase() {
        repository.delete(testUser);
    }

    @Test
    @DisplayName("test return true when all application works correctly on /auth/signin request")
    void signIn() throws Exception {
        repository.save(testUser);
        String signInEndpoint = "/api/auth/signin";
        mockMvc.perform(get(signInEndpoint).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(AUTHENTICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("test return true when all application works correctly on /auth/signup request")
    void signUp() throws Exception {
        String signUpEndpoint = "/api/auth/signup";
        mockMvc.perform(post(signUpEndpoint).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(SIGN_UP_JSON)).andExpect(status()
                .isOk());
    }
}
