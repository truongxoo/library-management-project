package study.demo.service.dto.request;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import study.demo.enums.EGender;
import study.demo.utils.validation.ValidDate;
import study.demo.utils.validation.ValidEmail;
import study.demo.utils.validation.ValidPassword;
import study.demo.utils.validation.ValidPhone;

@Data
@Builder
public class RegisterRequestDto {

    @ValidEmail(message = "{email.invalid}")
    @NotBlank(message = "{email.notblank}")
    private String email;

    @NotBlank(message = "{password.notblank}")
    @ValidPassword(message = "{password.invalid}")
    private String password;

    @NotBlank(message = "{firstName.notblank}")
    @Size(min = 1, max = 30)
    private String firstName;

    @Size(min = 1, max = 30)
    private String lastName;
    
    private EGender gender;
    
    @ValidDate(message = "{date.invalid}")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") 
    private Instant birthday;

    @ValidPhone(message = "{phone.invalid}")
    @NotBlank(message = "{phone.notblank}")
    private String phone;

}
