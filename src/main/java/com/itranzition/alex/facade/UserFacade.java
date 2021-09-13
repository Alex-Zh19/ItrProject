package com.itranzition.alex.facade;

import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.impl.ResponseHelloDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.security.jwt.TokenProvider;
import com.itranzition.alex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserFacade {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final String DEFAULT_MESSAGE = "hello";

    @Autowired
    public UserFacade(TokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    public BaseResponseDto hello(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        User user = null;
        if (token != null) {
            String email = tokenProvider.getUserEmail(token);
            user = userService.findUserByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("User with email %s not found", email));
            }
        }
        return createHelloResponse(user);
    }

    private BaseResponseDto createHelloResponse(User user) {
        ResponseHelloDto responseHelloDto = new ResponseHelloDto();
        StringBuilder responseMessageBuilder = new StringBuilder(DEFAULT_MESSAGE).
                append(user.getName());
        responseHelloDto.setMessage(responseMessageBuilder.toString());
        return responseHelloDto;
    }
}
