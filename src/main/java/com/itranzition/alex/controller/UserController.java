package com.itranzition.alex.controller;

import com.itranzition.alex.facade.UserFacade;
import com.itranzition.alex.model.dto.BaseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/user/")
public class UserController {

    private UserFacade facade;

    @Autowired
    public UserController(UserFacade facade) {
        this.facade = facade;
    }

    @GetMapping("/hello")
    public BaseResponseDto hello(HttpServletRequest request) {
        return facade.hello(request);
    }
}
