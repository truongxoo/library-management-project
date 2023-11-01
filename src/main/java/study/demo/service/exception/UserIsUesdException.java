package study.demo.service.exception;

public class UserIsUesdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserIsUesdException(String message) {
        super(message);
    }

}