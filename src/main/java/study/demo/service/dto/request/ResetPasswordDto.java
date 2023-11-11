package study.demo.service.dto.request;

import java.util.HashMap;

import javax.validation.constraints.NotBlank;

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
    
    @NotBlank(message = "{password.notblank}")
    @ValidPassword(message = "{password.invalid}")
    private String newPassword;
       
}
