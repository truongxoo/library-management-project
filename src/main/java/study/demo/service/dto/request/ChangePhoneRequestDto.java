package study.demo.service.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;
import study.demo.entity.PhoneNumber;
import study.demo.utils.validation.Phone;
import study.demo.utils.validation.ValidPhone;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePhoneRequestDto {
    
    @ValidPhone(message = "{phone.invalid}")
    @NotBlank(message = "{phone.notblank}")
    private String newPhone;
    
//    @Phone
//    private PhoneNumber phone;
    
}
