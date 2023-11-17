package study.demo.service.dto.request;

import java.time.Instant;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;
import study.demo.enums.Constants;
import study.demo.enums.EGender;
import study.demo.utils.validation.ValidDate;
import study.demo.utils.validation.ValidEmail;
import study.demo.utils.validation.ValidEnum;
import study.demo.utils.validation.ValidPhone;

@Data
@Builder
public class RegisterRequestDto {

    @Email(message = "{email.invalid}")
    @NotBlank(message = "{email.notblank}")
    private String email;

    @Pattern(regexp = Constants.LOGIN_REGEX,message = "{password.invalid}")
    private String password;

    @NotBlank(message = "{firstName.notblank}")
    @Size(min = 1, max = 50,message = "{firstName.length.invalid}")
    private String firstName;

    @Size(min = 1, max = 50,message = "{lastName.length.invalid}")
    private String lastName;
    
    @ValidEnum(enumClass = EGender.class)
    private String gender;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") 
    @ValidDate(message = "{birthday.invalid}")
    private Instant birthday;

    @ValidPhone(message = "{phone.invalid}")
    @NotBlank(message = "{phone.notblank}")
    private String phone;

}
