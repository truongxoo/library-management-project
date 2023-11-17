package study.demo.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import study.demo.entity.User;
import study.demo.service.dto.response.UserDto;

public interface UserService {
    
    List<UserDto> findAllUsers(Pageable pageable);

    void increaseFailedAttempts(User user);
    
    void lock(User user);
    
    void unlockUser(User user);

}
