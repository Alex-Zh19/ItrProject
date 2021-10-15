package com.itranzition.alex.facade;

import com.itranzition.alex.exception.UnauthorizedException;
import com.itranzition.alex.model.dto.BaseResponseDto;
import com.itranzition.alex.model.dto.UserHelloDto;
import com.itranzition.alex.model.dto.impl.ResponseHelloDto;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.security.jwt.TokenProvider;
import com.itranzition.alex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserFacade {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final String DEFAULT_MESSAGE = "hello ";


    @Autowired
    public UserFacade(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
                      UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    public BaseResponseDto hello(HttpServletRequest request, UserHelloDto helloDto) {
        String token = tokenProvider.resolveToken(request);
        if (token != null && !token.isBlank()) {
            String email = tokenProvider.getUserEmail(token);
            if (validateUsersCredentials(helloDto, email)) {
                User user = userService.findUserByEmail(email);
                return createHelloResponse(user);
            }
        }
        throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    private BaseResponseDto createHelloResponse(User user) {
        ResponseHelloDto responseHelloDto = new ResponseHelloDto();
        StringBuilder responseMessageBuilder = new StringBuilder(DEFAULT_MESSAGE).
                append(user.getName());
        responseHelloDto.setMessage(responseMessageBuilder.toString());
        return responseHelloDto;
    }

    private boolean validateUsersCredentials(UserHelloDto userHelloDto, String emailFromToken) {
        String emailFromRequest = userHelloDto.getEmail();
        String passwordFromRequest = userHelloDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(emailFromRequest, passwordFromRequest));
        return emailFromRequest.equals(emailFromToken);
    }
}
