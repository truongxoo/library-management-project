package study.demo.security;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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
import study.demo.service.UserService;
import study.demo.service.exception.DataInvalidException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${app.maxFailedAttempts}")
    private long maxFailedAttempts;

    @Value("${app.lockTimeDuration}")
    private long lockTimeDuration;

    private final UserRepository userRepo;

    private final MessageSource messages;

    /*
     * override UserDetailsService method for retrieving a username, a password, and
     * other attributes for authenticating
     */
    @Override
    public UserDetails loadUserByUsername(String userName) {

        log.info("Authenticating " + userName);
        User user = userRepo.findByEmail(userName).orElseThrow(() -> new DataInvalidException(
                messages.getMessage("user.notfound", new Object[] { userName }, Locale.ENGLISH))); // find user with

        if (user.getUserStatus() != EUserStatus.ACTIVATED) { // only user with ACTIVATED status can login
            log.error("User " + user.getEmail() + " has been " + user.getUserStatus());
            throw new DataInvalidException(messages.getMessage("user.notactivated",
                    new Object[] { user.getEmail(), user.getUserStatus() }, Locale.getDefault()));
        }
        List<GrantedAuthority> roleNames = List
                .of((GrantedAuthority) new SimpleGrantedAuthority(user.getRole().getRoleName().name()));
        return new UserDetailImpl(user.getUserId(), user.getEmail(), user.getPassword(), user.getUserStatus(),
                roleNames);
    }
}