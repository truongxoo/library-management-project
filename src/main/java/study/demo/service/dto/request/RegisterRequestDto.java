package study.demo.service.dto.request;

import java.time.Instant;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Builder;
import lombok.Data;
import study.demo.enums.EGender;
import study.demo.enums.ERole;
import study.demo.enums.EUserStatus;

@Data
@Builder
public class RegisterRequestDto {

    @Email
    @NotBlank(message = "{email.notblank}")
    private String email;

    @NotBlank(message = "{password.notblank}")
    @Length(min = 8, message = "{password.atleast8char}")
    private String password;
    
    @NotBlank(message = "{firstName.notblank}")
    private String firstName;
    
    private String lastName;

    private EGender gender;

    private Instant birthday;

    private String phone;

    private ERole role;

    private EUserStatus status;

}
