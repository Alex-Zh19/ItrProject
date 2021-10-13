package com.itranzition.alex.facade;

import com.itranzition.alex.mapper.UserMapper;
import com.itranzition.alex.mapper.UserMapperImpl;
import com.itranzition.alex.model.dto.AuthenticationDto;
import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.SignUpDto;
import com.itranzition.alex.model.dto.impl.ResponseSignInDto;
import com.itranzition.alex.model.dto.impl.ResponseSignUpDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.rabbitmq.Producer;
import com.itranzition.alex.security.jwt.TokenProvider;
import com.itranzition.alex.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationFacadeTest {
    private final String USER_EMAIL = "testEmail@mail.ru";
    private final String USER_ROLE = "USER";
    private final String USER_PASSWORD = "testPassword";
    private final String USER_NAME = "john";
    private final String USER_SURNAME = "doe";
    private final String TEST_TOKEN = "token";

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private UserService userService;
    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    @Mock
    private Producer producer;
    @InjectMocks
    private AuthenticationFacade facade;

    @Test
    @DisplayName("test should return true when method return not null response for sign in endpoint")
    void shouldReturnTrueOnNotNullResponseForSignIn() {
        User userFromBase = new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        when(userService.findUserByEmail(any())).thenReturn(userFromBase);
        when(tokenProvider.createToken(any(), any())).thenReturn(TEST_TOKEN);
        AuthenticationDto authenticationDto = createAuthenticationDto();
        BaseResponseDto responseDto = facade.signIn(authenticationDto);
        assertNotNull(responseDto);
    }

    @Test
    @DisplayName("test should return true when sign in passed correctly")
    void shouldReturnTrueOnSuccessfulSignIn() {
        User userFromBase = new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        when(userService.findUserByEmail(any())).thenReturn(userFromBase);
        when(tokenProvider.createToken(any(), any())).thenReturn(TEST_TOKEN);
        AuthenticationDto authenticationDto = createAuthenticationDto();
        ResponseSignInDto responseSignInDtoExpected = createExpectedSignInDto();
        ResponseSignInDto responseSignInDtoActual = (ResponseSignInDto) facade.signIn(authenticationDto);
        assertEquals(responseSignInDtoExpected, responseSignInDtoActual);
    }

    @Test
    @DisplayName("test should return true when registration passed correctly")
    void shouldReturnTrueOnSuccessfulRegistration() {
        SignUpDto dtoFromController = createSignUpDto();
        ResponseSignUpDto responseSignUpDtoExpected = createExpectedSignUpDto();
        ResponseSignUpDto responseSignUpDtoActual = (ResponseSignUpDto) facade.signUp(dtoFromController);
        assertEquals(responseSignUpDtoExpected, responseSignUpDtoActual);
    }

    private AuthenticationDto createAuthenticationDto() {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setPassword(USER_PASSWORD);
        authenticationDto.setEmail(USER_EMAIL);
        return authenticationDto;
    }

    private ResponseSignInDto createExpectedSignInDto() {
        ResponseSignInDto responseSignInDto = new ResponseSignInDto();
        responseSignInDto.setEmail(USER_EMAIL);
        responseSignInDto.setToken(TEST_TOKEN);
        return responseSignInDto;
    }

    private ResponseSignUpDto createExpectedSignUpDto() {
        ResponseSignUpDto responseSignUpDtoExpected = new ResponseSignUpDto();
        responseSignUpDtoExpected.setEmail(USER_EMAIL);
        responseSignUpDtoExpected.setName(USER_NAME);
        return responseSignUpDtoExpected;
    }

    private SignUpDto createSignUpDto() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail(USER_EMAIL);
        signUpDto.setPassword(USER_PASSWORD);
        signUpDto.setConfirmPassword(USER_PASSWORD);
        signUpDto.setName(USER_NAME);
        signUpDto.setSurname(USER_SURNAME);
        return signUpDto;
    }
}
