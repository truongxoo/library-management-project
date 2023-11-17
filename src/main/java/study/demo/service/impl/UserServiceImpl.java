package study.demo.service.impl;

import java.time.Instant;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.User;
import study.demo.repository.UserRepository;
import study.demo.service.UserService;
import study.demo.service.dto.response.UserDto;
import study.demo.utils.ConverterUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService{
    
    @Value("${app.maxFailedAttempts}")
    private Long maxFailedAttempts;

    @Value("${app.lockTimeDuration}")
    private Long lockTimeDuration;

    private final UserRepository userRepository;
    
    private final ModelMapper modelMapper;
    
    private final ConverterUtil converterUtil;
    
    
    // lock user after 3 failed login attempts
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void lock(User user) {
        user.setLocked(true);
        user.setLockTime(Instant.now().plusMillis(lockTimeDuration));
        userRepository.save(user);
    }

    // when login fail, failed attempt will increase by 1
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    // unlock user when login successfully or lock time is over
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void unlockUser(User user) {
        userRepository.unlockUser(user.getEmail());
    }
    
    // find all users
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();

    }

}
