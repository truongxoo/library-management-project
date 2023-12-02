package study.demo.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.OtpVerification;
import study.demo.entity.User;
import study.demo.entity.UserSession;
import study.demo.enums.EOtpType;
import study.demo.enums.EUserStatus;
import study.demo.repository.OtpVerificationRepository;
import study.demo.repository.UserRepository;
import study.demo.repository.UserSessionRepository;
import study.demo.security.jwt.JwtService;
import study.demo.service.OtpVerificationService;
import study.demo.service.UserInfoService;
import study.demo.service.UserSessionService;
import study.demo.service.dto.request.ChangeMailRequestDto;
import study.demo.service.dto.request.ChangePasswordRequestDto;
import study.demo.service.dto.request.ChangePhoneRequestDto;
import study.demo.service.dto.request.ResetPasswordDto;
import study.demo.service.dto.response.MailContentDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.dto.response.UserDto;
import study.demo.service.exception.CusNotFoundException;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.exception.VerifyExpirationException;
import study.demo.utils.MailSenderUtil;
import study.demo.utils.OtpUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final MailSenderUtil mailSenderService;

    private final OtpVerificationService otpVrfService;

    private final OtpVerificationRepository otpRepo;

    private final UserSessionService userSessionService;
    
    private final UserSessionRepository userSessionRepository;

    private final JwtService jwtService;

    private final MessageSource messages;

    // reset password
    @Override
    public MessageResponseDto resetPassword(HttpServletRequest httpServletRequest) {
        String userName = httpServletRequest.getHeader("email");
        Optional<User> user = userRepository.getUserWithOtp(userName);
        if (user.isEmpty()) {
            log.error("Can not found user with name: {}", userName);
            throw new CusNotFoundException(
                    messages.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault()),
                    "user.notfound");
        }
        user.get().getOtpVerification().stream().forEach(s -> {
            if (s.getOtpType().equals(EOtpType.RESET_PASSWORD)) {
                otpRepo.delete(s);
            }
        });
//        user.get().setUserStatus(EUserStatus.VULNERABLE);
//        userRepository.save(user.get());
        String otp = OtpUtil.generateOtp();
        OtpVerification otpVrf = otpVrfService.createOtpVerification(user.get(), otp, EOtpType.RESET_PASSWORD);
        String content = "<p>Hello " + userName + "</p>" + "<p>This is your otp using to reset your password</p>"
                + "<b>" + otpVrf.getOtpCode() + "</b>";

        MailContentDto mail = MailContentDto.builder().subject("Reset Password").receiver(userName).content(content)
                .build();
        mailSenderService.sendMail(mail);

        return MessageResponseDto.builder().message(messages.getMessage("sendmail.success", null, Locale.getDefault()))
                .messageCode("sendmail.success").build();
    }

    // change password
    @Override
    public MessageResponseDto changePassword(ChangePasswordRequestDto request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userRepository.getUserWithUserSession(userName);
        if (user.isEmpty()) {
            log.error("Can not found user with name: {}", userName);
            throw new CusNotFoundException(
                    messages.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault()),
                    "user.notfound");
        }
        if (!request.getConfirmPassword().equals(request.getNewPassword())) {
            log.error("Confirm password does not match");
            throw new DataInvalidException(messages.getMessage("cfpassword.notmatch", null, Locale.getDefault()),
                    "cfpassword.notmatch");
        }
        if ((!encoder.matches(request.getCurrentPassword(), user.get().getPassword()))) {
            log.error("Password is incorrect");
            throw new DataInvalidException(messages.getMessage("password.incorrect", null, Locale.getDefault()),
                    "password.incorrect");
        }
        if (encoder.matches(request.getNewPassword(), user.get().getPassword())) {
            log.error("New password is the same with current password");
            throw new DataInvalidException(messages.getMessage("newpassword.issame", null, Locale.getDefault()),
                    "newpassword.issame");
        } // custom annotation

        user.get().setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user.get());
        
        userSessionRepository.deleteUserSessionByUserName(user.get().getUserId());
        return MessageResponseDto.builder()
                .message((messages.getMessage("changepassword.success", null, Locale.getDefault())))
                .messageCode("changepassword.success").build();
    }

    // confirm change mail
    @Override
    public MessageResponseDto confirmChangeMail(ChangeMailRequestDto request,HttpServletRequest httpRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String otpCode = httpRequest.getHeader("otp");
        OtpVerification otp = otpRepo.findByOtpCodeAndOtpType(otpCode, EOtpType.CHANGE_MAIL);
        if (otp == null) {
            log.error("Can not found otp with otpCode: {}", otpCode);
            throw new CusNotFoundException(
                    messages.getMessage("otp.notfound", new Object[] { otpCode }, Locale.getDefault()), "otp.notfound");
        }
        if (otp.getExpiryDate().isBefore(Instant.now())) {
            log.error("Otp with otpCode: {} is expired", otpCode);
            throw new VerifyExpirationException(
                    messages.getMessage("otp.expired", new Object[] { otp.getOtpCode() }, Locale.getDefault()),
                    "otp.expired");
        }
        User user = otp.getUser();
        if (!userName.equals(user.getEmail())) {
            log.error("Otp with otpCode: {} is invalid", otpCode);
            throw new VerifyExpirationException(
                    messages.getMessage("otp.invalid", new Object[] { otp.getOtpCode() }, Locale.getDefault()),
                    "otp.invalid");
        }
        user.setEmail(request.getNewEmail());
        userRepository.save(user);
        otpRepo.delete(otp);
        return MessageResponseDto.builder()
                .message(messages.getMessage("changemail.success", null, Locale.getDefault()))
                .messageCode("changemail.success").build();
    }

    // send otp to new email
    @Override
    public MessageResponseDto changeMail(ChangeMailRequestDto request) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> newUser = userRepository.findByEmail(request.getNewEmail());

        Optional<User> currentUser = userRepository.findByEmail(userName);
        if (currentUser.isEmpty()) {
            log.error("Can not found user with name: {}", currentUser);
            throw new CusNotFoundException(
                    messages.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault()),
                    "user.notfound");
        }
        if (userName.equalsIgnoreCase(request.getNewEmail())) {
            log.error("New email {} is the same with current email: {}", userName, request.getNewEmail());
            throw new DataInvalidException(messages.getMessage("username.issame", null, Locale.getDefault()),
                    "username.issame");
        }
        if (newUser.isPresent()) {
            log.error("New email is already in use: {}", request.getNewEmail());
            throw new DataInvalidException(
                    messages.getMessage("username.isused", new Object[] { request.getNewEmail() }, Locale.getDefault()),
                    "username.isused");
        }
        OtpVerification otp = otpVrfService.createOtpVerification(currentUser.get(), OtpUtil.generateOtp(),
                EOtpType.CHANGE_MAIL);
        String content = "<p>Hello " + request.getNewEmail() + "</p>" + "This is your code to verify:</p>" + "<b>"
                + otp.getOtpCode() + "</b>";

        MailContentDto mail = MailContentDto.builder().subject("Change Email").receiver(request.getNewEmail())
                .content(content).build();
        mailSenderService.sendMail(mail);

        return MessageResponseDto.builder().message(messages.getMessage("sendmail.success", null, Locale.getDefault()))
                .messageCode("sendmail.success").build();
    }

    // confirm reset password
    @Override
    @Transactional
    public MessageResponseDto confirmResetPassword(HttpServletRequest httpServletRequest, ResetPasswordDto password) {
        String userName = httpServletRequest.getHeader("email");
        String otpCode = httpServletRequest.getHeader("otp");

        Optional<User> currentUser = userRepository.findByEmail(userName);
        if (!currentUser.isPresent()) {
            log.error("Can not found user with name: {}", currentUser);
            throw new CusNotFoundException(
                    messages.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault()),
                    "user.notfound");
        }
        OtpVerification otp = otpRepo.findByOtpCodeAndOtpType(otpCode, EOtpType.RESET_PASSWORD);
        if (otp == null) {
            log.error("Can not found otp: {}", otpCode);
            throw new CusNotFoundException(
                    messages.getMessage("otp.notfound", new Object[] { otpCode }, Locale.getDefault()), "otp.notfound");
        }
        if (otp.getExpiryDate().isBefore(Instant.now())) {
            log.error("Otp with otpCode: {} is expired", otpCode);
            throw new VerifyExpirationException(
                    messages.getMessage("otp.expired", new Object[] { otpCode }, Locale.getDefault()), "otp.expired");
        }
        User user = otp.getUser();
        user.setPassword(encoder.encode(password.getNewPassword()));
        userRepository.unlockUser(userName);
        userRepository.save(user);
        otpRepo.delete(otp);
        return MessageResponseDto.builder()
                .message(messages.getMessage("resetpassword.success", null, Locale.getDefault()))
                .messageCode("resetpassword.success").build();
    }
   
    // request change phone
    @Override
    public MessageResponseDto requestChangePhone() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new CusNotFoundException(
                        messages.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault()),
                        "user.notfound"));
        OtpVerification otp = otpVrfService.createOtpVerification(user, OtpUtil.generateOtp(), EOtpType.CHANGE_PHONE);

        String content = "<p>Hello " + userName + "</p>" + "This is your code to confirm the phone change:</p>" + "<b>"
                + otp.getOtpCode() + "</b>";

        MailContentDto mail = MailContentDto.builder().subject("Change phone").receiver(userName).content(content)
                .build();
        mailSenderService.sendMail(mail);

        return MessageResponseDto.builder().message(messages.getMessage("sendmail.success", null, Locale.getDefault()))
                .messageCode("sendmail.success").build();
    }

    // confirm change phone
    @Override
    public MessageResponseDto confirmChangePhone(HttpServletRequest httpServletRequest,ChangePhoneRequestDto newPhone) {
        
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String otpCode = httpServletRequest.getHeader("otp");

        OtpVerification otp = otpRepo.findByOtpCodeAndOtpType(otpCode, EOtpType.CHANGE_PHONE);
        if (otp == null) {
            log.error("Can not found otp with otpcode: {}", otpCode);
            throw new CusNotFoundException(
                    messages.getMessage("otp.notfound", new Object[] { otpCode }, Locale.getDefault()), "otp.notfound");
        }
        if (otp.getExpiryDate().isBefore(Instant.now())) {
            throw new VerifyExpirationException(
                    messages.getMessage("otp.expired", new Object[] { otp.getOtpCode() }, Locale.getDefault()),
                    "otp.expired");
        }
        User user = otp.getUser();
        if (!userName.equals(user.getEmail())) {
            log.error("Otp with otpCode: {} is invalid", otpCode);
            throw new VerifyExpirationException(
                    messages.getMessage("otp.invalid", new Object[] { otp.getOtpCode() }, Locale.getDefault()),
                    "otp.invalid");
        }
        user.setPhone(newPhone.getNewPhone());
        otpRepo.delete(otp);
        return MessageResponseDto.builder()
                .message((messages.getMessage("changepassword.success", null, Locale.getDefault())))
                .messageCode("changepassword.success").build();
    }

}