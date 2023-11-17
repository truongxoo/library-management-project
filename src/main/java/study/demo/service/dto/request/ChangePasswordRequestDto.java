package study.demo.service.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequestDto {
    
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",message = "{password.invalid}")
    @NotBlank(message = "{password.notblank}")
    private String newPassword;
    
    @NotBlank(message = "{password.notblank}")
    private String confirmPassword;
    
    @NotBlank(message = "{password.notblank}")
    private String currentPassword;
    
    
}
