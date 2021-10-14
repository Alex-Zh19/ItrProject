package com.itranzition.alex.exception;

import com.itranzition.alex.model.dto.impl.ResponseErrorDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationControllerExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseErrorDto handleBadCredentialsException(BadCredentialsException e) {
        return createErrorResponse("Error 400 : Bad credentials" + e.getMessage());
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseErrorDto handleUsernameNotFoundException(UsernameNotFoundException e) {
        return createErrorResponse("User with email %s not found" + e.getMessage());
    }

    private ResponseErrorDto createErrorResponse(String message) {
        ResponseErrorDto responseErrorDto = new ResponseErrorDto();
        responseErrorDto.setMessage(message);
        return responseErrorDto;
    }
}
