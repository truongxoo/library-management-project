package study.demo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import study.demo.service.dto.request.AuthenticationRequest;
import study.demo.service.dto.request.TokenRefreshRequest;
import study.demo.service.dto.response.AuthenticationResponseDto;

public interface AuthenticationService {
	
	AuthenticationResponseDto authenticate(AuthenticationRequest request) throws UsernameNotFoundException, Exception;
	
	AuthenticationResponseDto refreshtoken(HttpServletRequest request);
	
}
