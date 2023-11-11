package study.demo.service.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;
import study.demo.utils.validation.ValidEmail;

@Data
@Builder
public class ChangeMailRequestDto {
    
    @ValidEmail(message = "{email.invalid}")
    @NotBlank(message = "{email.notblank}")
    private String newEmail;
    
    @ValidEmail(message = "{email.invalid}")
    @NotBlank(message = "{email.notblank}")
    private String currentEmail;
    
    @NotBlank(message = "{email.notblank}")
    private String password;
    

}
