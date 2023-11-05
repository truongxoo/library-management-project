package study.demo.service;

import javax.servlet.http.HttpServletRequest;

import study.demo.service.dto.request.RegisterRequestDto;
import study.demo.service.dto.response.MessageResponseDto;

public interface RegisterService {
    
    MessageResponseDto register(RegisterRequestDto request,HttpServletRequest httpRequest);

    MessageResponseDto confirmLink(String verifyCode);
    
    MessageResponseDto confirmOtp(String otpCode);
    
    MessageResponseDto resendNewOtp(HttpServletRequest httpRequest,String userName);
    
    MessageResponseDto resendNewLink(HttpServletRequest httpRequest,String userName);
}
