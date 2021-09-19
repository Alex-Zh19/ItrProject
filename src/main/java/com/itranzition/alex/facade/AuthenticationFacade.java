package com.itranzition.alex.facade;

import com.itranzition.alex.mapper.UserMapper;
import com.itranzition.alex.model.dto.AuthenticationDto;
import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.SignUpDto;
import com.itranzition.alex.model.dto.impl.ResponseErrorDto;
import com.itranzition.alex.model.dto.impl.ResponseSignInDto;
import com.itranzition.alex.model.dto.impl.ResponseSignUpDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.rabbitmq.Producer;
import com.itranzition.alex.security.jwt.TokenProvider;
import com.itranzition.alex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthenticationFacade {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserMapper userMapper;
    private final Producer producer;

    @Autowired
    public AuthenticationFacade(AuthenticationManager authenticationManager,
                                TokenProvider tokenProvider, UserService userService,
                                UserMapper userMapper, Producer producer) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.userMapper = userMapper;
        this.producer = producer;
    }

    public BaseResponseDto signIn(AuthenticationDto authenticationDTO) {
        if (authenticationDTO.getEmail() == null || authenticationDTO.getPassword() == null) {
            StringBuilder errorMessage = new StringBuilder(HttpStatus.BAD_REQUEST.value()).
                    append(" ").
                    append(HttpStatus.BAD_REQUEST.getReasonPhrase());
            return createErrorResponse(errorMessage.toString());
        }
        try {
            User user = findUser(authenticationDTO);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("User with email %s not found", user.getEmail()));
            }
            return createSignInResponse(user);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    public BaseResponseDto signUp(SignUpDto signUpDto) {
        if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            throw new BadCredentialsException("User password and confirm password do not march");
        }
        if (signUpDto.getEmail() == null || signUpDto.getPassword() == null ||
                signUpDto.getConfirmPassword() == null || signUpDto.getName() == null) {
            String errorMessage = "Error 400 : \"Fill in required fields\"";
            return createErrorResponse(errorMessage);
        }
        User user = userMapper.signUpDtoToUser(signUpDto);
        User userRegistered = userService.addUser(user);
        producer.send(createLogMessage(userRegistered));
        ResponseSignUpDto responseSignUpDto = userMapper.signUpDtoToResponseSignUpDto(signUpDto);
        return responseSignUpDto;
    }

    private String createLogMessage(User user) {
        StringBuilder builder = new StringBuilder(user.toString()).
                append(" sign up at ").append(LocalDateTime.now());
        return builder.toString();
    }

    private User findUser(AuthenticationDto authenticationDto) {
        String email = authenticationDto.getEmail();
        String password = authenticationDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return userService.findUserByEmail(email);
    }

    private BaseResponseDto createSignInResponse(User user) {
        String email = user.getEmail();
        String token = tokenProvider.createToken(email, user.getRole());
        ResponseSignInDto responseSignInDto = new ResponseSignInDto();
        responseSignInDto.setToken(token);
        responseSignInDto.setEmail(email);
        return responseSignInDto;
    }

    private BaseResponseDto createErrorResponse(String message) {
        ResponseErrorDto responseErrorDto = new ResponseErrorDto();
        responseErrorDto.setMessage(message);
        return responseErrorDto;
    }
}
