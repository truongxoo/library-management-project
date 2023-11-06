package study.demo.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import study.demo.entity.User;
import study.demo.service.dto.request.PasswordRequestDto;
import study.demo.service.dto.request.UserFilterDto;
import study.demo.service.dto.response.MessageResponseDto;

public interface UserService {

    Page<User> findAllUsers(Integer pageIndex);

    Page<User> findUsersByFilter(UserFilterDto userFilter, Integer pageIndex);

    void increaseFailedAttempts(User user);
    
    void lock(User user);
    
    void unlockUser(User user);

    MessageResponseDto  resetPassword(String userName);

    MessageResponseDto changePassword(PasswordRequestDto request, String userName);
    
}
