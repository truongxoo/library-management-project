package study.demo.service.exception;

public class ConfirmationInvalidException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    public ConfirmationInvalidException(String message) {
        super(message);
    }

}
