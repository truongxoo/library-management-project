package study.demo.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import study.demo.service.dto.response.ExceptionMessageDto;
import study.demo.service.exception.CusBadCredentialsException;
import study.demo.service.exception.TokenRefreshException;
import study.demo.service.exception.UserIsUesdException;
import study.demo.service.exception.UserLockedException;
import study.demo.service.exception.UserNotActivatedException;
import study.demo.service.exception.UserNotFoundException;

@RestControllerAdvice
public class ExcptionHandlerController {
    
    // Handle user not activated exception
    @ExceptionHandler(UserNotActivatedException.class)
    public ExceptionMessageDto handleUserNotActivatedException(UserNotActivatedException e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.FORBIDDEN).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }

    // Handle user not found exception
    @ExceptionHandler(UserNotFoundException.class)
    public ExceptionMessageDto handleUserNotFoundException(UserNotFoundException e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.NOT_FOUND).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }
    
    // Handle user global exception
    @ExceptionHandler({Exception.class,RuntimeException.class})
    public ExceptionMessageDto handleGlobalException(Exception e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.BAD_REQUEST).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }
    
    // Handle refresh token exception
    @ExceptionHandler(TokenRefreshException.class)
    public ExceptionMessageDto handleTokenRefreshException(TokenRefreshException e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.FORBIDDEN).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }
    
    // Handle user locked exception
    @ExceptionHandler(UserLockedException.class)
    public ExceptionMessageDto handleLockedException(UserLockedException e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.FORBIDDEN).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }
    
    // Handle password fail exception
    @ExceptionHandler(CusBadCredentialsException.class)
    public ExceptionMessageDto handleBadCredentialsException(CusBadCredentialsException e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.FORBIDDEN).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }
    
    // Handle username is already used exception
    @ExceptionHandler(UserIsUesdException.class)
    public ExceptionMessageDto handlerUserIsUsedException(UserIsUesdException e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.BAD_REQUEST).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }
    
    // Handle validate exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
