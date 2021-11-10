package com.itranzition.alex.controller;

import com.itranzition.alex.ItransitionApplicationTests;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.security.jwt.TokenProvider;
import com.itranzition.alex.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest extends ItransitionApplicationTests {
    private final String TEST_EMAIL = "testEmail";
    private final String TEST_NAME = "testName";
    private final String TEST_PASSWORD = "testPass";
    private final String TEST_SURNAME = "testSurname";
    private final String TEST_ROLE = "testRole";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String TOKEN_HEADER = "Authorization";
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private TokenProvider provider;
    @SpyBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService.addUser(new User(1L, TEST_EMAIL, TEST_NAME, TEST_PASSWORD, TEST_SURNAME, TEST_ROLE));
    }

    @Test
    @DisplayName("test return true when all application works correctly on /user/hello request and returns expected response")
    void hello() throws Exception {
        String helloEndpoint = "/api/user/hello";
        String token = createTestToken();
        mockMvc.perform(get(helloEndpoint)
                        .header(TOKEN_HEADER, token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("test return true when all application works correctly on /user/hello request and returns expected response")
    void hello2() throws Exception {
        String helloEndpoint = "/api/user/hello";
        String token = createNonValidTestToken();
        mockMvc.perform(get(helloEndpoint)
                        .header(TOKEN_HEADER, token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private String createTestToken() {
        return new StringBuilder(TOKEN_PREFIX)
                .append(provider.createToken(TEST_EMAIL, TEST_ROLE)).toString();
    }

    private String createNonValidTestToken() {
        return new StringBuilder(TOKEN_PREFIX)
                .append("provider.createToken(TEST_EMAIL, TEST_ROLE)").toString();
    }
}
