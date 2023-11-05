package study.demo.service;

import org.springframework.data.domain.Page;

import study.demo.entity.User;
import study.demo.service.dto.request.UserFilterDto;

public interface UserService {

    Page<User> findAllUsers(Integer pageIndex);

    Page<User> findUsersByFilter(UserFilterDto userFilter, Integer pageIndex);

    void increaseFailedAttempts(User user);
    
    void lock(User user);
    
    void unlockUser(User user);
    
}
