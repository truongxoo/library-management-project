package study.demo.service;

import javax.servlet.http.HttpServletRequest;

import study.demo.service.dto.request.RegisterRequestDto;
import study.demo.service.dto.response.MessageResponseDto;

public interface RegisterService {
    
    MessageResponseDto register(RegisterRequestDto request);

    MessageResponseDto confirmLink(String verifyCode);
    
    MessageResponseDto confirmOtp(String otpCode);
    
    MessageResponseDto resendNewOtp(String userName);
    
    MessageResponseDto resendNewLink(String userName);
}
