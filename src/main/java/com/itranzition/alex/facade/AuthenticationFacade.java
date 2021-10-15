package com.itranzition.alex.facade;

import com.itranzition.alex.mapper.UserMapper;
import com.itranzition.alex.model.dto.*;
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
            String errorMessage = new StringBuilder(HttpStatus.BAD_REQUEST.value())
                    .append(" ")
                    .append(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .toString();
            throw new BadCredentialsException(errorMessage);
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
            throw new BadCredentialsException("Fill in required fields");
        }
        if (userService.existsByEmail(signUpDto.getEmail())) {
            throw new BadCredentialsException("User with email " + signUpDto.getEmail() + " is already exist");
        }
        User user = userMapper.signUpDtoToUser(signUpDto);
        User userRegistered = userService.addUser(user);
        producer.send(createLogMessageDto(userRegistered));
        ResponseSignUpDto responseSignUpDto = userMapper.signUpDtoToResponseSignUpDto(signUpDto);
        return responseSignUpDto;
    }

    private RabbitConsumerMessageDto createLogMessageDto(User user) {
        RegisteredUserDto registeredUserDto = userMapper.userToRegisteredUserDto(user);
        RabbitConsumerMessageDto dto = new RabbitConsumerMessageDto(registeredUserDto, LocalDateTime.now());
        return dto;
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
}
