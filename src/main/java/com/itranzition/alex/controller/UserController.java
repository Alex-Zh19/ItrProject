package com.itranzition.alex.controller;

import com.itranzition.alex.facade.UserFacade;
import com.itranzition.alex.model.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/")
public class UserController {

    private final UserFacade facade;

    @GetMapping("/hello")
    public BaseResponseDto hello() {
        return facade.hello();
    }
}
