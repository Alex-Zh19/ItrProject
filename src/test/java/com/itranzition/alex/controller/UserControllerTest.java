package com.itranzition.alex.controller;

import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.impl.ResponseErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    void hello() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("email", "testEmail");
        request.addParameter("password", "testPass");
        request.addHeader("Authorization", "kkk");
        BaseResponseDto actualResponseDto = userController.hello(request);
        BaseResponseDto expectedResponseDto = createExpectedResponseUserController();
        //todo assertThrows
    }

    private BaseResponseDto createExpectedResponseUserController() {
        ResponseErrorDto expectedResponse = new ResponseErrorDto();
        expectedResponse.setMessage("Error 401 : \"Full authentication is required to access this resource\"");
        return expectedResponse;
    }
}
