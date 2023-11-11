package study.demo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;

import study.demo.entity.User;
import study.demo.service.dto.request.ChangeMailRequestDto;
import study.demo.service.dto.request.PasswordRequestDto;
import study.demo.service.dto.request.ResetPasswordDto;
import study.demo.service.dto.request.UserFilterDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.dto.response.UserDto;

public interface UserService {

    List<UserDto> findAllUsers(Integer pageIndex,Integer recordPerPage);

    Page<User> findUsersByFilter(UserFilterDto userFilter, Integer pageIndex);

    void increaseFailedAttempts(User user);
    
    void lock(User user);
    
    void unlockUser(User user);

    MessageResponseDto  resetPassword(HttpServletRequest httpServletRequest);
    
    MessageResponseDto  confirmResetPassword(HttpServletRequest httpServletRequest,ResetPasswordDto password);

    MessageResponseDto changePassword(PasswordRequestDto request);

    MessageResponseDto changeMail(ChangeMailRequestDto request);
    
    MessageResponseDto confirmChangeMail(ChangeMailRequestDto request,String otpCode);
    
}
