package study.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import study.demo.entity.User;
import study.demo.service.dto.request.UserFilter;

public interface UserService {

    Page<User> findAllUsers(Integer pageIndex);

    Page<User> findUsersByFilter(UserFilter userFilter, Integer pageIndex);

    void increaseFailedAttempts(User user);

    void unlockUser(User user);

    Optional<User> findByEmail(String email);
    
    
}
