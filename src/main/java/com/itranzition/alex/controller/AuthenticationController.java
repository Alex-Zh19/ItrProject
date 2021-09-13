package com.itranzition.alex.controller;

import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.facade.AuthenticationFacade;
import com.itranzition.alex.model.dto.AuthenticationDto;
import com.itranzition.alex.model.dto.SignUpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {

    private AuthenticationFacade facade;

    @Autowired
    public AuthenticationController(AuthenticationFacade facade) {
        this.facade = facade;
    }

    @RequestMapping("/signin")
    public BaseResponseDto signIn(@RequestBody AuthenticationDto authenticationDTO) {
        return facade.signIn(authenticationDTO);
    }

    @RequestMapping("/signup")
    public BaseResponseDto signUp(@RequestBody SignUpDto signUpDto) {
        return facade.signUp(signUpDto);
    }
}
