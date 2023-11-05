package study.demo.security;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

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

    private final UserRepository userRepo;

    private final UserService userService;

    private final MessageSource messages;

    /*
     * override UserDetailsService method for retrieving a username, a password, and
     * other attributes for authenticating
     */
    @Override
    public UserDetails loadUserByUsername(String userName) {

        log.info("Authenticating "+userName);
        User user = userRepo.findByEmail(userName).orElseThrow(() -> new DataInvalidException(
                messages.getMessage("user.notfound", new Object[] { userName }, Locale.ENGLISH))); // find user
                                                                                                       // with email
        if (user.isLocked()) {
            long timeUntilUnlocked = ((user.getLockTime().toEpochMilli())
                    - (System.currentTimeMillis()))/60000;    // remaining lock time
            if (user.getLockTime().isBefore(Instant.now())) {
                userService.unlockUser(user);    // unlock user if the lock time expires or throw exception
            } else {
                throw new LockedException(
                        messages.getMessage("lock.time", new Object[] { timeUntilUnlocked }, Locale.ENGLISH));
            }
        } else if (user.getUserStatus() != EUserStatus.ACTIVATED) {    // only user with ACTIVATED status can login
            log.error("User " + userName + " has been " + user.getUserStatus());
            throw new DataInvalidException(messages.getMessage("user.notactivated",
                    new Object[] { userName, user.getUserStatus() }, Locale.getDefault()));
        }

        // return new UserDetai instance with username,password and roles
        List<GrantedAuthority> roleNames = List
                .of((GrantedAuthority) new SimpleGrantedAuthority(user.getRole().getRoleName().name()));
        return new UserDetailImpl(user.getUserId(), user.getEmail(), user.getPassword(), roleNames);
    }

}
