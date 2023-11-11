package study.demo.service.dto.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Builder;
import lombok.Data;
import study.demo.utils.validation.ValidPassword;

@Data
@Builder
public class PasswordRequestDto {
    
    @NotBlank(message = "{password.notblank}")
    @ValidPassword(message = "{password.invalid}")
    private String newPassword;
    
    @NotBlank(message = "{password.notblank}")
    private String oldPassword;
    
    
}
