package com.itranzition.alex.controller;

import com.itranzition.alex.model.dto.AuthenticationDto;
import com.itranzition.alex.model.dto.SignUpDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.model.security.jwt.TokenProvider;
import com.itranzition.alex.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @RequestMapping("/signin")
    public ResponseEntity signIn(@RequestBody AuthenticationDto authenticationDTO) {
        if (authenticationDTO.getEmail() == null || authenticationDTO.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(HttpStatus.BAD_REQUEST.value()+" "+HttpStatus.BAD_REQUEST.getReasonPhrase());
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
            Map<Object, Object> response = new HashMap<>();
            response.put("email", email);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @RequestMapping("/signup")
    public ResponseEntity signUp(@RequestBody SignUpDto signUpDto) {
        if (signUpDto.getEmail() == null || signUpDto.getPassword() == null ||
                signUpDto.getConfirmPassword() == null || signUpDto.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error 400 : \"Fill in required fields\"");
        }
        User user = new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setPassword(signUpDto.getPassword());
        if (signUpDto.getSurname() != null) {
            user.setSurname(signUpDto.getSurname());
        }
        if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            throw new BadCredentialsException("User password and confirm password do not march");

        }
        User userRegistered = userService.addUser(user);
        Map<Object, Object> response = new HashMap<>();
        response.put("email", signUpDto.getEmail());
        response.put("name", signUpDto.getName());
        return ResponseEntity.ok(response);
    }
}