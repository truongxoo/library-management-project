package study.demo.service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailContentDto {
    
    private String subject;
    
    private String content;
    
    private String receiver;
    
}
