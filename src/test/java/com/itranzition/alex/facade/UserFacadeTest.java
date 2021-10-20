package com.itranzition.alex.facade;

import com.itranzition.alex.model.dto.impl.ResponseHelloDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.properties.JwtConfigurationProperties;
import com.itranzition.alex.security.JwtUserDetailsService;
import com.itranzition.alex.security.jwt.JwtUser;
import com.itranzition.alex.security.jwt.JwtUserFactory;
import com.itranzition.alex.security.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {
    private final String USER_EMAIL = "testEmail@mail.ru";
    private final String USER_ROLE = "USER";
    private final String USER_PASSWORD = "testPassword";
    private final String USER_NAME = "john";
    private final String USER_SURNAME = "doe";
    private final String DEFAULT_MESSAGE = "hello ";

    private UserFacade userFacade = new UserFacade();
    @Spy
    private Principal principal;
    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private JwtConfigurationProperties properties;

    @BeforeEach
    void setUp() {
        User userFromBase =
                new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        when(properties.getExpiration()).thenReturn(10000L);
        String keyword = Base64.getEncoder().encodeToString("tttt".getBytes());
        when(properties.getKeyword()).thenReturn(keyword);
        JwtUser jwtUser = JwtUserFactory.create(userFromBase);
        when(jwtUserDetailsService.loadUserByUsername(anyString())).thenReturn(jwtUser);
        TokenProvider tokenProvider = new TokenProvider(jwtUserDetailsService, properties);
        String token = tokenProvider.createToken(USER_EMAIL, USER_ROLE);
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("test should return true when method return expected response for hello endpoint")
    void shouldReturnTrueOnCorrectResponseForHelloEndpoint() {
        ResponseHelloDto responseHelloDtoExpected = createResponseHelloDtoExpected();
        ResponseHelloDto responseHelloDtoActual = (ResponseHelloDto) userFacade.hello();
        assertEquals(responseHelloDtoExpected, responseHelloDtoActual);
    }

    @Test
    @DisplayName("test should return true when method return not null response for hello endpoint")
    void shouldReturnTrueOnNotNullResponseForHelloEndpoint() {
        ResponseHelloDto responseHelloDtoActual = (ResponseHelloDto) userFacade.hello();
        assertNotNull(responseHelloDtoActual);
    }

    private ResponseHelloDto createResponseHelloDtoExpected() {
        ResponseHelloDto helloDto = new ResponseHelloDto();
        StringBuilder responseMessageBuilder = new StringBuilder(DEFAULT_MESSAGE).
                append(USER_EMAIL);
        helloDto.setMessage(responseMessageBuilder.toString());
        return helloDto;
    }
}
