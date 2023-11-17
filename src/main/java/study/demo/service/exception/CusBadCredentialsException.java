package study.demo.service.exception;

import org.springframework.security.authentication.BadCredentialsException;

import lombok.Data;

@Data
public class CusBadCredentialsException extends RuntimeException {

 private final String messageCode;
    
    private final String message;

    private static final long serialVersionUID = 1L;

    public CusBadCredentialsException(String message ,String messageCode) {
        super(message);
        this.message=message;
        this.messageCode =messageCode;
    }
    
}
