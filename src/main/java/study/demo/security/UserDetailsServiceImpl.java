package study.demo.security;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.User;
import study.demo.enums.EUserStatus;
import study.demo.repository.UserRepository;
import study.demo.service.exception.UserNotActivatedException;
import study.demo.service.exception.UserNotFoundException;
import study.demo.service.impl.UserServiceImpl;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${app.lockTimeDuration}")
    private long lockTimeDuration;

    private final UserRepository userRepository;

    private final UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("Authenticating...");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email + " cannot found"));    // find user with email
      
        if (user.isLocked()) {
            long timeUntilUnlocked = ((user.getLockTime().toEpochMilli() + lockTimeDuration)
                    - (Instant.now().toEpochMilli())) / 60000;
            if (timeUntilUnlocked <= 0) {
                userService.unlockUser(user);    // unlock user if the lock time expires or throw exception
            } else {
                throw new LockedException("Your account will be unlocked in " + timeUntilUnlocked + " min");
            }
        } else if (user.getUserStatus() != EUserStatus.ACTIVATED) {    // Only user with ACTIVATED status can login 
            log.error("User " + email + " has been " + user.getUserStatus());
            throw new UserNotActivatedException("User " + email + " has been " + user.getUserStatus());
        }
        // return new UserDetai instance with username,password and roles
        List<GrantedAuthority> roleNames = List
                .of((GrantedAuthority) new SimpleGrantedAuthority(user.getRole().getRoleName().name()));
        return new UserDetailImpl(user.getUserId(), user.getEmail(), user.getPassword(), roleNames);
    }

}
