package study.demo.service;

import javax.servlet.http.HttpServletRequest;

import study.demo.service.dto.request.RegisterRequest;
import study.demo.service.dto.response.MessageResponseDto;

public interface RegisterService {
    
    MessageResponseDto register(RegisterRequest request,HttpServletRequest httpRequest);

    MessageResponseDto confirmLink(String verifyCode);
    
    MessageResponseDto confirmOtp(String otpCode);
}
