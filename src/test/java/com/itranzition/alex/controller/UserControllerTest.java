package com.itranzition.alex.controller;

import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.impl.ResponseHelloDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.security.jwt.TokenProvider;
import com.itranzition.alex.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {
    private final String TEST_EMAIL = "testEmail";
    private final String TEST_NAME = "testName";
    private final String TEST_PASSWORD = "testPass";
    private final String TEST_SURNAME = "testSurname";
    private final String TEST_ROLE = "testRole";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String TOKEN_HEADER = "Authorization";
    @Autowired
    private UserController userController;
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
    void hello() {
        MockHttpServletRequest request = createMockRequest();
        BaseResponseDto actualResponseDto = userController.hello(request);
        BaseResponseDto expectedResponseDto = createExpectedResponseUserController();
        assertEquals(expectedResponseDto, actualResponseDto);
    }

    private BaseResponseDto createExpectedResponseUserController() {
        ResponseHelloDto expectedResponse = new ResponseHelloDto();
        expectedResponse.setMessage("hello " + TEST_NAME);
        return expectedResponse;
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("email", TEST_EMAIL);
        request.addParameter("password", TEST_PASSWORD);
        String testToken = createTestToken();
        request.addHeader(TOKEN_HEADER, testToken);
        return request;
    }

    private String createTestToken() {
        String result = new StringBuilder(TOKEN_PREFIX)
                .append(provider.createToken(TEST_EMAIL, TEST_ROLE)).toString();
        return result;
    }
}
