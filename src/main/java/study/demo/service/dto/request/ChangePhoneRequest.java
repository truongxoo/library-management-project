package study.demo.service.dto.request;

import lombok.Builder;
import lombok.Data;
import study.demo.utils.validation.ValidPhone;
@Data
@Builder
public class ChangePhoneRequest {
    @ValidPhone(message = "{phone.invalid}")
    private String phone;
}
