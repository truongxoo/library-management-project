package study.demo.service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRefreshRequest {

    private String token;

    private String refreshToken;

}
