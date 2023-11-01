package study.demo.service.exception;

public class InvalidLinkException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    public InvalidLinkException(String message) {
        super(message);
    }

}
