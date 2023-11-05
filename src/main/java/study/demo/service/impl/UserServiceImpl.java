package study.demo.service.impl;

import java.time.Instant;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.User;
import study.demo.repository.UserRepository;
import study.demo.service.UserService;
import study.demo.service.dto.request.UserFilterDto;
import study.demo.service.exception.CusBadCredentialsException;

@Service
@RequiredArgsConstructor
@Transactional(dontRollbackOn = {CusBadCredentialsException.class})
public class UserServiceImpl implements UserService {

    @Value("${app.maxFailedAttempts}")
    private Long maxFailedAttempts;

    @Value("${app.lockTimeDuration}")
    private Long lockTimeDuration;

    private final UserRepository userRepository;
    
    // lock user after 3 failed login attempts
    @Override
    public void lock(User user) {
        user.setLocked(true);
        user.setLockTime(Instant.now().plusMillis(lockTimeDuration));
        userRepository.save(user);
    }
    
    // when login fail, failed attempt will increase by 1
    @Override
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }
    
    // unlock user when login successfully or lock time is over
    @Override
    public void unlockUser(User user) {
        userRepository.unlockUser(user.getEmail());
    }
    
    @Override
    public Page<User> findAllUsers(Integer pageIndex) {
        Pageable pageable = null;
        if (pageIndex == 0) {
            pageable = PageRequest.of(0, 10);
        } else {
            pageable = PageRequest.of(pageIndex, 10);
        }
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findUsersByFilter(UserFilterDto userFilter, Integer pageIndex) {
        Pageable pageable = null;
        if (pageIndex == 0) {
            pageable = PageRequest.of(0, 10);
        } else {
            pageable = PageRequest.of(pageIndex, 10);
        }

        return null;
    }
}
