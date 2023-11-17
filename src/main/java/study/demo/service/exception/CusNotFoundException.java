package study.demo.service.exception;

import lombok.Builder;
import lombok.Data;

@Data
public class CusNotFoundException extends RuntimeException {
    
    private final String messageCode;
    
    private final String message;

    private static final long serialVersionUID = 1L;

    public CusNotFoundException(String message ,String messageCode) {
        super(message);
        this.message=message;
        this.messageCode =messageCode;
    }
    
    
}
