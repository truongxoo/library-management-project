package study.demo.service.listener;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import study.demo.entity.User;
import study.demo.repository.UserRepository;
import study.demo.service.UserService;
import study.demo.service.exception.DataInvalidException;

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
        User user = userRepo.findByEmail(userName).orElseThrow(() -> new DataInvalidException(
                messages.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault())));
        if (!user.isLocked()) {
            if (user.getFailedAttempt() < maxFailedAttempts) {
                userService.increaseFailedAttempts(user);
            } else {
                userService.lock(user); // user will be locked out after 3 failed login attempts
                throw new LockedException(
                        messages.getMessage("lock.time", new Object[] {  getUnlockTime(user) }, Locale.getDefault()));
            }
        } else if (user.isLocked()) {
            if (user.getLockTime().isAfter(Instant.now())) {
                throw new LockedException(
                        messages.getMessage("lock.time", new Object[] {  getUnlockTime(user) }, Locale.getDefault()));
            } else {
                userService.lock(user);
                throw new LockedException(
                        messages.getMessage("lock.time", new Object[] {  getUnlockTime(user) }, Locale.getDefault()));
            }
        }
    }

    // called when authentication success to unlock user
    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow(() -> new DataInvalidException(
                messages.getMessage("user.notfound", new Object[] { userDetails.getUsername() }, Locale.getDefault())));
        if (user.getLockTime() != null) {
            if (user.getLockTime().isAfter(Instant.now())) {
                throw new LockedException(
                        messages.getMessage("lock.time", new Object[] {  getUnlockTime(user) }, Locale.getDefault()));
            } else {
                    userService.unlockUser(user);
            }
        }
    }

    private long getUnlockTime(User user) {
        long timeUntilUnlocked = 0;
        if (user.getLockTime() != null) {
            timeUntilUnlocked = ((user.getLockTime().toEpochMilli()) - (System.currentTimeMillis())) / 60000;
            return timeUntilUnlocked;
        }
        return timeUntilUnlocked;
    }

}
