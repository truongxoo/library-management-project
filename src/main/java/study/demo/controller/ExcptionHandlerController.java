package study.demo.controller;

import java.time.Instant;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.ExpiredJwtException;
import study.demo.service.dto.response.ExceptionMessageDto;
import study.demo.service.exception.CusBadCredentialsException;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.exception.VerifyExpirationException;
@RestControllerAdvice
public class ExcptionHandlerController {
    
    // Handle exception related to user not found,username already in use,...
    @ExceptionHandler(DataInvalidException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessageDto handletDateInvalidException(DataInvalidException e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.FORBIDDEN).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }

    // Handle user global exception
    @ExceptionHandler({Exception.class,RuntimeException.class})
    public ExceptionMessageDto handleGlobalException(Exception e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.BAD_REQUEST).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }
    
    // Handle password fail exception
    @ExceptionHandler(CusBadCredentialsException.class)
    public ExceptionMessageDto handleBadCredentialsException(CusBadCredentialsException e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.FORBIDDEN).timestamp(Instant.now())
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
    
    // handle expiration exception
    @ExceptionHandler({VerifyExpirationException.class})
    public ExceptionMessageDto handleExpirationException(VerifyExpirationException e, WebRequest request) {
        return ExceptionMessageDto.builder().statusCode(HttpStatus.BAD_REQUEST).timestamp(Instant.now())
                .message(e.getMessage()).build();
    }
//    
//    @ExceptionHandler({ExpiredJwtException.class})
//    public ExceptionMessageDto resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
//            ExpiredJwtException e) {
//        return ExceptionMessageDto.builder().statusCode(HttpStatus.BAD_REQUEST).timestamp(Instant.now())
//                .message(e.getMessage()).build();
//    }
    
}
