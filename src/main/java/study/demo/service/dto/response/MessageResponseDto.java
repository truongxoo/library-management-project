package study.demo.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponseDto {

    private String message;
    
    private Integer statusCode;

    public MessageResponseDto(String message) {
        super();
        this.message = message;
    }

    public MessageResponseDto(String message, Integer statusCode) {
        super();
        this.message = message;
        this.statusCode = statusCode;
    }
    
}
