package study.demo.service.dto.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordRequestDto {
    
    @NotBlank(message = "{password.notblank}")
    @Length(min = 8, message = "{password.atleast8char}")
    private String newPassword;
    
    @NotBlank(message = "{password.notblank}")
    private String oldPassword;
    
    
}
