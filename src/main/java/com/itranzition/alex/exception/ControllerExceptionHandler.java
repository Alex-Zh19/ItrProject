package com.itranzition.alex.exception;

import com.itranzition.alex.model.dto.impl.ResponseErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseErrorDto handleBadCredentialsException(BadCredentialsException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseErrorDto handleUsernameNotFoundException(UsernameNotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), e.getMessage());
    }

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseErrorDto handleUnauthorizedException(UnauthorizedException e) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), e.getMessage());
    }

    @ExceptionHandler({JwtAuthenticationException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseErrorDto handleJwtAuthenticationException(JwtAuthenticationException e) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), e.getMessage());
    }

    private ResponseErrorDto createErrorResponse(HttpStatus status, LocalDateTime date, String message) {
        return new ResponseErrorDto(status, date, message);
    }
}
