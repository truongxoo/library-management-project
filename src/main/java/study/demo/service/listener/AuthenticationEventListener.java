package study.demo.service.listener;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import study.demo.entity.User;
import study.demo.service.impl.UserServiceImpl;

@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

	@Value("${app.maxFailedAttempts}")
	private long maxFailedAttempts;

	@Value("${app.lockTimeDuration}")
	private long lockTimeDuration;

	private final UserServiceImpl userService;
	
	private static int count;

	@EventListener
	public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
		String userName = (String) event.getAuthentication().getPrincipal();
		User user = userService.findByEmail(userName).get();
		if (user != null) {
			if (!user.isLocked()) {
				if (user.getFailedAttempt() < maxFailedAttempts) {
					userService.increaseFailedAttempts(user);
				} else {
					userService.lock(user);
					throw new LockedException("Your account has been locked due to 3 failed attempts."
							+ " It will be unlocked in 30 min.");
				}
			} else if (user.isLocked()) {
			    long timeUntilUnlocked = ((user.getLockTime().toEpochMilli() +lockTimeDuration)-(Instant.now().toEpochMilli()))/ 60000;
				if (timeUntilUnlocked <= 0) {
					throw new LockedException("Your account will be unlocked in " + timeUntilUnlocked + " min");
				}
				userService.unlockUser(user);
				throw new LockedException("Your account has been unlocked. Please try to login again.");
			}
		}
	}

	@EventListener
	public void authenticationSuccess(AuthenticationSuccessEvent event) {
		UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
		User user = userService.findByEmail(userDetails.getUsername()).get();
		if (user.getFailedAttempt() > 0) {
			userService.unlockUser(user);
		}
	}

}
