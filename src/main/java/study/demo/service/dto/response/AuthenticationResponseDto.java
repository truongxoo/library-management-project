package study.demo.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationResponseDto {
	
	private String token;
	
	private String refreshToken;
}
