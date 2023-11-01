package study.demo.service.exception;

public class LinkExpirationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public LinkExpirationException( String message) {
        super(message);
    }

}
