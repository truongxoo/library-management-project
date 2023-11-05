package study.demo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import study.demo.service.dto.response.MessageResponseDto;

public interface LogoutService {

    MessageResponseDto logout(HttpServletRequest request, HttpServletResponse response);
    
}
