package com.itranzition.alex.facade;

import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.impl.ResponseHelloDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserFacade {
    private static final String DEFAULT_MESSAGE = "hello ";

    public BaseResponseDto hello() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        return createHelloResponse(principal.getName());
    }

    private BaseResponseDto createHelloResponse(String userEmail) {
        ResponseHelloDto responseHelloDto = new ResponseHelloDto();
        StringBuilder responseMessageBuilder = new StringBuilder(DEFAULT_MESSAGE).
                append(userEmail);
        responseHelloDto.setMessage(responseMessageBuilder.toString());
        return responseHelloDto;
    }
}
