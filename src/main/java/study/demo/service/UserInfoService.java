package study.demo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import study.demo.entity.User;
import study.demo.service.dto.request.ChangeMailRequestDto;
import study.demo.service.dto.request.ChangePasswordRequestDto;
import study.demo.service.dto.request.ChangePhoneRequestDto;
import study.demo.service.dto.request.ResetPasswordDto;
import study.demo.service.dto.request.UserFilterDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.dto.response.UserDto;

public interface UserInfoService {

    MessageResponseDto  resetPassword(HttpServletRequest httpServletRequest);
    
    MessageResponseDto  confirmResetPassword(HttpServletRequest httpServletRequest,ResetPasswordDto password);

    MessageResponseDto changePassword(ChangePasswordRequestDto request);

    MessageResponseDto changeMail(ChangeMailRequestDto request);
    
    MessageResponseDto confirmChangeMail(ChangeMailRequestDto request,String otpCode);
    
    MessageResponseDto requestChangePhone();
    
    MessageResponseDto confirmChangePhone(HttpServletRequest httpServletRequest, ChangePhoneRequestDto changePhoneRequestDto);
    
    
    
}
