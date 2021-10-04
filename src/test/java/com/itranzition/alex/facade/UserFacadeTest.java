package com.itranzition.alex.facade;

import com.itranzition.alex.model.dto.impl.ResponseHelloDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.security.jwt.TokenProvider;
import com.itranzition.alex.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserFacadeTest {
    private TokenProvider tokenProvider;
    private UserService userService;
    private UserFacade userFacade;
    private HttpServletRequest request;
    private final String USER_EMAIL = "testEmail@mail.ru";
    private final String USER_ROLE = "USER";
    private final String USER_PASSWORD = "testPassword";
    private final String USER_NAME = "john";
    private final String USER_SURNAME = "doe";
    private final String DEFAULT_MESSAGE = "hello";

    @BeforeEach
    void setUp() {
        tokenProvider = mock(TokenProvider.class);
        userService = mock(UserService.class);
        request = mock(HttpServletRequest.class);
        userFacade = new UserFacade(tokenProvider, userService);
        User userFromBase =
                new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        when(tokenProvider.validateToken(any(String.class))).thenReturn(true);
        when(tokenProvider.resolveToken(any())).thenReturn("smth");
        when(tokenProvider.getUserEmail(any())).thenReturn(USER_EMAIL);
        when(userService.findUserByEmail(any(String.class))).thenReturn(userFromBase);
    }

    @Test
    void shouldReturnTrueOnCorrectResponseForHelloEndpoint() {
        userFacade.hello(request);
        ResponseHelloDto responseHelloDtoExpected = createResponseHelloDtoExpected();
        ResponseHelloDto responseHelloDtoActual = (ResponseHelloDto) userFacade.hello(request);
        assertEquals(responseHelloDtoExpected, responseHelloDtoActual);
    }

    @Test
    void shouldReturnTrueOnNotNullResponseForHelloEndpoint() {
        userFacade.hello(request);
        ResponseHelloDto responseHelloDtoActual = (ResponseHelloDto) userFacade.hello(request);
        assertNotNull(responseHelloDtoActual);
    }


    private ResponseHelloDto createResponseHelloDtoExpected() {
        ResponseHelloDto helloDto = new ResponseHelloDto();
        StringBuilder responseMessageBuilder = new StringBuilder(DEFAULT_MESSAGE).
                append(USER_NAME);
        helloDto.setMessage(responseMessageBuilder.toString());
        return helloDto;
    }
}
