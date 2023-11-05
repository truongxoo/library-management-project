package study.demo.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import study.demo.service.dto.request.AuthenticationRequestDto;
import study.demo.service.dto.response.AuthenticationResponseDto;

public interface AuthenticationService {
	
	AuthenticationResponseDto authenticate(AuthenticationRequestDto request) throws UsernameNotFoundException, Exception;
	
	AuthenticationResponseDto refreshtoken(HttpServletRequest request);
	
}
