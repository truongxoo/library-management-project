package study.demo.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import study.demo.entity.User;
import study.demo.service.dto.request.RegisterRequest;
import study.demo.service.dto.response.MessageResponseDto;

public interface RegisterService {
    
    MessageResponseDto register(RegisterRequest request,HttpServletRequest httpRequest);

    Object confirmRegistration(String verifyCode);
}
