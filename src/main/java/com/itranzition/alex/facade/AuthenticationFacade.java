package com.itranzition.alex.facade;

import com.itranzition.alex.mapper.UserMapper;
import com.itranzition.alex.model.dto.AuthenticationDto;
import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.SignUpDto;
import com.itranzition.alex.model.dto.impl.ResponseErrorDto;
import com.itranzition.alex.model.dto.impl.ResponseSignInDto;
import com.itranzition.alex.model.dto.impl.ResponseSignUpDto;
import com.itranzition.alex.model.entity.User;
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


@Component
public class AuthenticationFacade {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationFacade(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    public BaseResponseDto signIn(AuthenticationDto authenticationDTO) {
        if (authenticationDTO.getEmail() == null || authenticationDTO.getPassword() == null) {
            ResponseErrorDto responseErrorDto = new ResponseErrorDto();
            responseErrorDto.setMessage(HttpStatus.BAD_REQUEST.value() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase());
            return responseErrorDto;
        }
        try {
            String email = authenticationDTO.getEmail();
            String password = authenticationDTO.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userService.findUserByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("User with email %s not found", email));
            }
            String token = tokenProvider.createToken(email, user.getRole());
            ResponseSignInDto responseSignInDto = new ResponseSignInDto();
            responseSignInDto.setToken(token);
            responseSignInDto.setEmail(email);
            return responseSignInDto;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    public BaseResponseDto signUp(SignUpDto signUpDto) {
        if (signUpDto.getEmail() == null || signUpDto.getPassword() == null ||
                signUpDto.getConfirmPassword() == null || signUpDto.getName() == null) {
            ResponseErrorDto responseErrorDto = new ResponseErrorDto();
            responseErrorDto.setMessage("Error 400 : \"Fill in required fields\"");
            return responseErrorDto;
        }
        /*User user = new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setPassword(signUpDto.getPassword());*/
        User user = UserMapper.USER_MAPPER.signUpDtoToUser(signUpDto);
        if (signUpDto.getSurname() != null) {
            user.setSurname(signUpDto.getSurname());
        }
        if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            throw new BadCredentialsException("User password and confirm password do not march");
        }
        User userRegistered = userService.addUser(user);
        ResponseSignUpDto responseSignUpDto = new ResponseSignUpDto();
        responseSignUpDto.setName(signUpDto.getName());
        responseSignUpDto.setEmail(signUpDto.getEmail());
        return responseSignUpDto;
    }

}
