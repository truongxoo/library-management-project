package study.demo.service.dto.request;

import java.util.HashMap;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.google.common.collect.Multiset.Entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.demo.entity.User;
import study.demo.utils.validation.ValidPassword;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",message = "{password.invalid}")
    private String newPassword;
       
}
