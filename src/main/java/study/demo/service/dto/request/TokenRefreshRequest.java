package study.demo.service.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRefreshRequest {

    private String token;
    
    @NotBlank
    private String refreshToken;

}
