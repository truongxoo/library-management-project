package study.demo.service.exception;

public class JwtExpirationException  extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    public JwtExpirationException( String message) {
        super(message);
    }

}
