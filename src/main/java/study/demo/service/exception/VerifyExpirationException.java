package study.demo.service.exception;

public class VerifyExpirationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public VerifyExpirationException( String message) {
        super(message);
    }

}
