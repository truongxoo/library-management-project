package study.demo.service.impl;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.User;
import study.demo.enums.EUserStatus;
import study.demo.repository.UserRepository;
import study.demo.service.MailSenderService;
import study.demo.service.UserService;
import study.demo.service.dto.request.PasswordRequestDto;
import study.demo.service.dto.request.UserFilterDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.exception.CusBadCredentialsException;

@Service
@RequiredArgsConstructor
@Transactional(dontRollbackOn = { CusBadCredentialsException.class })
public class UserServiceImpl implements UserService {

    @Value("${app.maxFailedAttempts}")
    private Long maxFailedAttempts;

    @Value("${app.lockTimeDuration}")
    private Long lockTimeDuration;

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final MailSenderService mailSenderService;

    private final MessageSource messages;

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

    // reset password
    @Override
    public MessageResponseDto resetPassword(String userName) {

        Optional<User> user = userRepository.findByEmail(userName);
        Random random = new Random();
        String newPassword = String.valueOf(random.nextInt(999999));

        if (user.isPresent()) {

            user.get().setUserStatus(EUserStatus.UNVERIFY);
            user.get().setPassword(encoder.encode(newPassword));
            userRepository.save(user.get());
            mailSenderService.sendMail(userName, newPassword);

            return new MessageResponseDto(messages.getMessage("resetpassword.success", null, Locale.getDefault()));
        }
        return new MessageResponseDto(messages.getMessage("resetpassword.fail", null, Locale.getDefault()));
    }

    @Override
    public MessageResponseDto changePassword(PasswordRequestDto request, String userName) {
        return userRepository.findByEmail(userName).filter(user -> {
            return !encoder.matches(request.getNewPassword(), user.getPassword());
            }).map(user -> {
                user.setUserStatus(EUserStatus.ACTIVATED);
                user.setPassword(encoder.encode(request.getNewPassword()));
                userRepository.save(user);
                return new MessageResponseDto(messages.getMessage("changepassword.success", null, Locale.getDefault()));
            }).orElse(new MessageResponseDto(messages.getMessage("changepassword.fail", null, Locale.getDefault())));
    }

}