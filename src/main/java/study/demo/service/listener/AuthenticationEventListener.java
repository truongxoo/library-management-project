package study.demo.service.listener;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.User;
import study.demo.repository.UserRepository;
import study.demo.service.UserInfoService;
import study.demo.service.UserService;
import study.demo.service.exception.CusBadCredentialsException;
import study.demo.service.exception.CusNotFoundException;
import study.demo.service.exception.DataInvalidException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

    @Value("${app.maxFailedAttempts}")
    private long maxFailedAttempts;

    @Value("${app.lockTimeDuration}")
    private long lockTimeDuration;

    private final UserService userService;

    private final UserRepository userRepo;

    private final MessageSource messages;

    // called when authentication fail
    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) { 

        String userName = (String) event.getAuthentication().getPrincipal();
        User user = userRepo.findByEmail(userName).orElseThrow(() -> new CusNotFoundException(
                messages.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault()),"user.notfound"));
        // throw exception if user has been locked
        if (user.isLocked()) {
            if (user.getLockTime().isAfter(Instant.now())) {
                log.info("User: {} was locked in {}", user.getEmail(),getUnlockTime(user));
                throw new CusBadCredentialsException(messages.getMessage(
                        "lock.time", new Object[] { getUnlockTime(user) }, Locale.getDefault()),"lock.time");
            }
            userService.lock(user);    
            log.info("User: {} was locked in {}", user.getEmail(),getUnlockTime(user));
            throw new CusBadCredentialsException(messages.getMessage(
                    "lock.time", new Object[] { getUnlockTime(user) }, Locale.getDefault()), "lock.time");
        }
        
        if (user.getFailedAttempt() < maxFailedAttempts) {
            userService.increaseFailedAttempts(user);
        } else {
            log.info("User: {} was locked after 3 failed attemps", user.getEmail(),getUnlockTime(user));
            userService.lock(user);     // user will be locked out after 3 failed login attempts
            throw new CusBadCredentialsException(messages.getMessage(
                    "lock.time", new Object[] { getUnlockTime(user) }, Locale.getDefault()),"lock.time");
        }
         
    }

    // called when authentication success to unlock user
    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        User user = userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CusNotFoundException(messages.getMessage(
                        "user.notfound", new Object[] { userDetails.getUsername() }, Locale.getDefault()), "user.notfound"));
        // unlock user if locked time is up 
        if (user.getLockTime() != null && user.getLockTime().isAfter(Instant.now())) {
            throw new CusBadCredentialsException(messages.getMessage(
                    "lock.time", new Object[] { getUnlockTime(user) }, Locale.getDefault()), "lock.time");
        }
        userService.unlockUser(user);
    }
    
    // get time left to unlock user
    private long getUnlockTime(User user) {
        long timeUntilUnlocked = 0;
        if (user.getLockTime() != null) {
            timeUntilUnlocked = ((user.getLockTime().toEpochMilli()) - (System.currentTimeMillis())) / 60000;
            return timeUntilUnlocked;
        }
        return timeUntilUnlocked;
    }

}
