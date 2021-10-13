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
        ResponseErrorDto responseDto = createErrorResponse("Error 400 : Bad credentials" + e.getMessage());
        return responseDto;
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseErrorDto handleUsernameNotFoundException(UsernameNotFoundException e) {
        ResponseErrorDto responseDto = createErrorResponse("User with email %s not found" + e.getMessage());
        return responseDto;
    }

    private ResponseErrorDto createErrorResponse(String message) {
        ResponseErrorDto responseErrorDto = new ResponseErrorDto();
        responseErrorDto.setMessage(message);
        return responseErrorDto;
    }
}
