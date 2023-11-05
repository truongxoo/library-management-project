package study.demo.service.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequestDto {
   
    @Email
    @NotBlank(message = "{email.notblank}")
    private String email;
   
    @NotBlank(message = "{password.notblank}")
    private String password;
}
