package study.demo.service.dto.response;

import org.springframework.core.io.Resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailContentDto {
    
    private String subject;
    
    private String content;
    
    private String receiver;
    
    private String addInline;
    
    private Resource resource;
}
