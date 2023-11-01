package study.demo.service.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class CusBadCredentialsException extends BadCredentialsException {

    private static final long serialVersionUID = 1L;

    public CusBadCredentialsException(String msg) {
        super(msg);
    }

}
