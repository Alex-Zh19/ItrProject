package com.itranzition.alex.controller;

import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.security.jwt.TokenProvider;
import com.itranzition.alex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/user/")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @GetMapping("/hello")
    public ResponseEntity hello(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        String name = null;
        if (token != null) {
            String email = tokenProvider.getUserEmail(token);
            User user = userService.findUserByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("User with email %s not found", email));
            }
            name = user.getName();
        }
        return ResponseEntity.ok("Hello, "+name);
    }
}
