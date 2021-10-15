package com.itranzition.alex.facade;

import com.itranzition.alex.model.dto.UserHelloDto;
import com.itranzition.alex.model.dto.impl.ResponseHelloDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.security.jwt.TokenProvider;
import com.itranzition.alex.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {
    private final String USER_EMAIL = "testEmail@mail.ru";
    private final String USER_ROLE = "USER";
    private final String USER_PASSWORD = "testPassword";
    private final String USER_NAME = "john";
    private final String USER_SURNAME = "doe";
    private final String DEFAULT_MESSAGE = "hello ";

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private UserFacade userFacade;

    @BeforeEach
    void setUp() {
        User userFromBase =
                new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        when(tokenProvider.resolveToken(any())).thenReturn("smth");
        when(tokenProvider.getUserEmail(any())).thenReturn(USER_EMAIL);
        when(userService.findUserByEmail(any(String.class))).thenReturn(userFromBase);
    }

    @Test
    @DisplayName("test should return true when method return expected response for hello endpoint")
    void shouldReturnTrueOnCorrectResponseForHelloEndpoint() {
        UserHelloDto dto = createUserHelloDto();
        userFacade.hello(request, dto);
        ResponseHelloDto responseHelloDtoExpected = createResponseHelloDtoExpected();
        ResponseHelloDto responseHelloDtoActual = (ResponseHelloDto) userFacade.hello(request, dto);
        assertEquals(responseHelloDtoExpected, responseHelloDtoActual);
    }

    @Test
    @DisplayName("test should return true when method return not null response for hello endpoint")
    void shouldReturnTrueOnNotNullResponseForHelloEndpoint() {
        UserHelloDto dto = createUserHelloDto();
        userFacade.hello(request, dto);
        ResponseHelloDto responseHelloDtoActual = (ResponseHelloDto) userFacade.hello(request, dto);
        assertNotNull(responseHelloDtoActual);
    }


    private ResponseHelloDto createResponseHelloDtoExpected() {
        ResponseHelloDto helloDto = new ResponseHelloDto();
        StringBuilder responseMessageBuilder = new StringBuilder(DEFAULT_MESSAGE).
                append(USER_NAME);
        helloDto.setMessage(responseMessageBuilder.toString());
        return helloDto;
    }

    private UserHelloDto createUserHelloDto() {
        UserHelloDto dto = new UserHelloDto();
        dto.setEmail(USER_EMAIL);
        dto.setPassword(USER_PASSWORD);
        return dto;
    }
}
