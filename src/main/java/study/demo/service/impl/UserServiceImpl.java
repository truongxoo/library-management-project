package study.demo.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import lombok.RequiredArgsConstructor;
import study.demo.entity.OtpVerification;
import study.demo.entity.User;
import study.demo.enums.EOtpType;
import study.demo.enums.EUserStatus;
import study.demo.repository.OtpVerificationRepository;
import study.demo.repository.UserRepository;
import study.demo.service.OtpVerificationService;
import study.demo.service.UserService;
import study.demo.service.dto.request.ChangeMailRequestDto;
import study.demo.service.dto.request.PasswordRequestDto;
import study.demo.service.dto.request.ResetPasswordDto;
import study.demo.service.dto.request.UserFilterDto;
import study.demo.service.dto.response.MailContentDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.dto.response.UserDto;
import study.demo.service.exception.CusBadCredentialsException;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.exception.VerifyExpirationException;
import study.demo.utils.MailSenderUtil;
import study.demo.utils.OtpUtil;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${app.maxFailedAttempts}")
    private Long maxFailedAttempts;

    @Value("${app.lockTimeDuration}")
    private Long lockTimeDuration;

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final MailSenderUtil mailSenderService;

    private final OtpVerificationService otpVrfService;

    private final OtpVerificationRepository otpRepo;

    private final MessageSource messages;
    
    private final ModelMapper modelMapper;
    
    // lock user after 3 failed login attempts
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = { Exception.class })
    public void lock(User user) {
        user.setLocked(true);
        user.setLockTime(Instant.now().plusMillis(lockTimeDuration));
        userRepository.save(user);
    }

    // when login fail, failed attempt will increase by 1
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = { Exception.class })
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    // unlock user when login successfully or lock time is over
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = { Exception.class })
    public void unlockUser(User user) {
        userRepository.unlockUser(user.getEmail());
    }

    // reset password
    @Override
    public MessageResponseDto resetPassword(HttpServletRequest httpServletRequest) {
        String userName = httpServletRequest.getHeader("email");
        Optional<User> user = userRepository.findByEmail(userName);
        if (user.isPresent()) {
            String otp = OtpUtil.generateOtp();
            OtpVerification otpVrf = otpVrfService.createOtpVerification(user.get(), otp, EOtpType.RESET_PASSWORD);
            String content = "<p>Hello " + userName + "</p>" + "<p>This is your otp using to reset your password</p>"
                    + "<b>" + otpVrf.getOtpCode() + "</b>";

            MailContentDto mail = MailContentDto.builder().subject("Reset Password").receiver(userName).content(content)
                    .build();
            mailSenderService.sendMail(mail);

            return new MessageResponseDto(messages.getMessage("sendmail.success", null, Locale.getDefault()),HttpStatus.OK.value());
        }
        return new MessageResponseDto(messages.getMessage("sendmail.fail", null, Locale.getDefault()),HttpStatus.BAD_REQUEST.value());
    }

    // change password
    @Override
    public MessageResponseDto changePassword(PasswordRequestDto request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(userName);
        if (user.isPresent()) {
            if ((!encoder.matches(request.getOldPassword(), user.get().getPassword()))) {
                throw new DataInvalidException(messages.getMessage("password.incorrect", null, Locale.getDefault()));
            }
            if (!encoder.matches(request.getNewPassword(), user.get().getPassword())) {
                throw new DataInvalidException(messages.getMessage("newpassword.issame", null, Locale.getDefault()));
            }
            user.get().setUserStatus(EUserStatus.ACTIVATED);
            user.get().setPassword(encoder.encode(request.getNewPassword()));
            userRepository.save(user.get());

            return new MessageResponseDto(messages.getMessage("changepassword.success", null, Locale.getDefault()));
        }
        return new MessageResponseDto(messages.getMessage("changepassword.fail", null, Locale.getDefault()));
    }

    // confirm change mail
    @Override
    public MessageResponseDto confirmChangeMail(ChangeMailRequestDto request, String otpCode) {

        OtpVerification otp = otpRepo.findUserByOtpCode(otpCode).orElseThrow(() -> new VerifyExpirationException(
                messages.getMessage("otp.notfound", new Object[] { otpCode }, Locale.ENGLISH)));

        if (otp.getExpiryDate().isAfter(Instant.now())) {
            Optional<User> userWithOldEmail = userRepository.findByEmail(request.getCurrentEmail());
            User user = otp.getUser();
            if (userWithOldEmail.isPresent() && userWithOldEmail.get().getEmail().equals(user.getEmail())) {
                user.setEmail(request.getNewEmail());
                userRepository.save(user);
                otpRepo.delete(otp);
            }
            return new MessageResponseDto(messages.getMessage("changemail.success", null, Locale.getDefault()));
        } else {
            throw new VerifyExpirationException(
                    messages.getMessage("otp.expired", new Object[] { otp.getOtpCode() }, Locale.ENGLISH));
        }
    }

    // send otp to new email
    @Override
    public MessageResponseDto changeMail(ChangeMailRequestDto request) {

        Optional<User> user = userRepository.findByEmail(request.getCurrentEmail());
        Optional<User> user1 = userRepository.findByEmail(request.getNewEmail());

        if (request.getCurrentEmail().equals(request.getNewEmail())) {
            throw new DataInvalidException(messages.getMessage("username.issame", null, Locale.getDefault()));
        }
        if (user1.isPresent()) {
            throw new DataInvalidException(messages.getMessage("username.isused",
                    new Object[] { request.getNewEmail() }, Locale.getDefault()));
        }
        if (!user.isPresent()) {
            throw new DataInvalidException(messages.getMessage("username.wrong",
                    new Object[] { request.getCurrentEmail() }, Locale.getDefault()));
        }
        if (!encoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new DataInvalidException(messages.getMessage("password.incorrect",
                    new Object[] { request.getCurrentEmail() }, Locale.getDefault()));
        }
        if (encoder.matches(request.getPassword(), user.get().getPassword())) {
            OtpVerification otp = otpVrfService.createOtpVerification(user.get(), OtpUtil.generateOtp(),
                    EOtpType.CHANGE_PASSWORD);
            String content = "<p>Hello " + request.getNewEmail() + "</p>" + "This is your code to verify:</p>" + "<b>"
                    + otp.getOtpCode() + "</b>";
            MailContentDto mail = MailContentDto.builder().subject("Change Email").receiver(request.getNewEmail())
                    .content(content).build();
            mailSenderService.sendMail(mail);
            return new MessageResponseDto(messages.getMessage("sendmail.success", null, Locale.getDefault()));
        }
        return new MessageResponseDto(messages.getMessage("sendmail.fail", null, Locale.getDefault()));
    }

    // confirm change password
    @Override
    @Transactional
    public MessageResponseDto confirmResetPassword(HttpServletRequest httpServletRequest, ResetPasswordDto password) {
        String userName = httpServletRequest.getHeader("email");
        String otpCode = httpServletRequest.getHeader("otp");

        User user = userRepository.findByEmail(userName).orElseThrow(() -> new DataInvalidException(
                messages.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault())));

        OtpVerification otp = otpRepo.findByOtpCode(otpCode).orElseThrow(() -> new VerifyExpirationException(
                messages.getMessage("otp.notfound", new Object[] { otpCode }, Locale.ENGLISH)));

        if (!otp.getOtpType().equals(EOtpType.RESET_PASSWORD)) {
            throw new DataInvalidException(
                    messages.getMessage("otp.invalid", new Object[] { otp.getOtpCode() }, Locale.ENGLISH));
        }
        if (otp.getExpiryDate().isAfter(Instant.now())) {
            User user1 = otp.getUser();
            if (user1.getEmail().equals(user.getEmail())) {
                user.setPassword(encoder.encode(password.getNewPassword()));
                userRepository.save(user);
                otpRepo.delete(otp);
            }
            return new MessageResponseDto(messages.getMessage("changepassword.success", null, Locale.getDefault()));
        } else {
            throw new VerifyExpirationException(
                    messages.getMessage("otp.expired", new Object[] { otp.getOtpCode() }, Locale.ENGLISH));
        }
    }

    // // find all users
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers(Integer pageIndex, Integer recordPerPage) {
        Pageable pageable = null;
        if  (pageIndex == null && recordPerPage == null) {
            pageable = PageRequest.of(0, 5);
        } else  {
            pageable = PageRequest.of(pageIndex, recordPerPage);
        }
        Page<User> users = userRepository.findAll(pageable);
       
        return  users.stream().map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        
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