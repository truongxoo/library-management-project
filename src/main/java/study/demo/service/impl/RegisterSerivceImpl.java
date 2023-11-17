package study.demo.service.impl;

import java.time.Instant;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.LinkVerification;
import study.demo.entity.Member;
import study.demo.entity.OtpVerification;
import study.demo.entity.Role;
import study.demo.entity.User;
import study.demo.enums.EGender;
import study.demo.enums.EOtpType;
import study.demo.enums.EUserStatus;
import study.demo.repository.LinkVerificationRepository;
import study.demo.repository.OtpVerificationRepository;
import study.demo.repository.RoleRepository;
import study.demo.repository.UserRepository;
import study.demo.service.LinkVerificationService;
import study.demo.service.OtpVerificationService;
import study.demo.service.RegisterService;
import study.demo.service.dto.request.RegisterRequestDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.event.OnRegistrationCompleteEvent;
import study.demo.service.exception.CusNotFoundException;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.exception.VerifyExpirationException;
import study.demo.utils.OtpUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RegisterSerivceImpl implements RegisterService {

    private final ApplicationEventPublisher eventPublisher;

    private final OtpVerificationService otpVrfService;

    private final LinkVerificationService linkService;

    private final LinkVerificationRepository linkRepo;

    private final OtpVerificationRepository otpRepo;

    private final RoleRepository roleRepo;

    private final UserRepository userRepo;

    private final PasswordEncoder encode;

    private final MessageSource message;

    @Value("${mail.url.confirm}")
    private String urlConfirm;

    // register new member with default status: UNVERIFY
    @Override
    public MessageResponseDto register(RegisterRequestDto request) { // ?

       userRepo.findByEmail(request.getEmail()).ifPresent(u -> {
            log.info("Register fail! Username{} is existed in database", request.getEmail());
            throw new DataInvalidException(
                    message.getMessage("username.isused", new Object[] { request.getEmail() }, Locale.getDefault()),
                    "username.isused");
        });

        Role role = roleRepo.findById(1).orElseThrow(() -> new DataInvalidException(
                        message.getMessage("role.notfound", new Object[] { request.getEmail() }, Locale.getDefault()),
                        "role.notfound"));

        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(encode.encode(request.getPassword()));
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setBirthday(request.getBirthday());
        member.setPhone(request.getPhone());
        member.setRole(role);
        member.setUserStatus(EUserStatus.UNVERIFY);

        if (request.getGender() != null) {
            member.setGender(EGender.valueOf((request.getGender())));
        } else {
            member.setGender(EGender.OTHER);
        }

        User userEvent = userRepo.save(member);
        log.info("Register success");

        // publish an event when register successfully
        LinkVerification link = linkService.createLinkVerification(userEvent);
        OtpVerification otp = otpVrfService.createOtpVerification(userEvent, OtpUtil.generateOtp(),
                EOtpType.CONFIRM_ACCOUNT);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(otp, link, userEvent));
        return MessageResponseDto.builder().message(
                message.getMessage("register.success", new Object[] { userEvent.getEmail() }, Locale.getDefault()))
                .messageCode("register.success").build();
    }

    // change user status and delete link after verifying successfully
    @Override
    public MessageResponseDto confirmLink(String verifyCode) {
        LinkVerification linkvr = linkRepo.findUserByVerificationCode(verifyCode);
        if (linkvr == null) {
            throw new CusNotFoundException(message.getMessage("linkvr.notfound", null, Locale.ENGLISH),
                    "linkvr.notfound");
        }
        if (linkvr.getExpiryDate().isBefore(Instant.now())) {
            throw new VerifyExpirationException(message.getMessage("link.expired", null, Locale.getDefault()),
                    "link.expired");
        }
        User user = linkvr.getUser();
        user.setUserStatus(EUserStatus.ACTIVATED);
        userRepo.save(user);
        linkRepo.delete(linkvr);

        // delete both link and otp when verifying successfully
        user.getOtpVerification().forEach(s -> {
            if (s.getOtpType().equals(EOtpType.CONFIRM_ACCOUNT)) {
                otpRepo.delete(s);
            }
        });

        return MessageResponseDto.builder().message(message.getMessage("verify.success", null, Locale.getDefault()))
                .messageCode("verify.success").build();

    }

    // change user status and delete otp after verifying successfully
    @Override
    public MessageResponseDto confirmOtp(String otpCode) {

        OtpVerification otp = otpRepo.findByOtpCodeAndOtpType(otpCode, EOtpType.CONFIRM_ACCOUNT);
        if (otp == null) {
            log.error("Can not found otp with otpcode: {}", otpCode);
            throw new CusNotFoundException(
                    message.getMessage("otp.notfound", new Object[] { otpCode }, Locale.getDefault()), "otp.notfound");
        }
        if (otp.getExpiryDate().isBefore(Instant.now())) {
            log.error("Otp with otpcode {} is expired", otpCode);
            throw new VerifyExpirationException(
                    message.getMessage("otp.expired", new Object[] { otpCode }, Locale.getDefault()), "otp.expired");
        }
        User user = otp.getUser();
        user.setUserStatus(EUserStatus.ACTIVATED);
        userRepo.save(user);
        otpRepo.delete(otp);
        linkRepo.delete(user.getLinkVerification()); // delete both link and otp when verifying successfully

        return MessageResponseDto.builder().message(message.getMessage("verify.success", null, Locale.getDefault()))
                .messageCode("verify.success").build();
    }

    // resend new OTP
    @Override
    public MessageResponseDto resendNewOtp(String userName) {

        User user = userRepo.findByEmail(userName)
                .orElseThrow(() -> new CusNotFoundException(
                        message.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault()),
                        "user.notfound"));
        if (user.getUserStatus().equals(EUserStatus.ACTIVATED)) {
            return MessageResponseDto.builder()
                    .message(message.getMessage("user.activated", new Object[] { userName }, Locale.getDefault()))
                    .messageCode("user.activated").build();
        }
        otpRepo.findByUser(user).map(otp -> {
            otpVrfService.updateOtpVerification(otp, user, OtpUtil.generateOtp()); // update OTP for user to confirm
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(otp, user));
            return MessageResponseDto.builder().message(message.getMessage("send.success", null, Locale.getDefault()))
                    .messageCode("send.success").build();
        }).orElseThrow(() -> new CusNotFoundException(message.getMessage("otp.notfound", null, Locale.getDefault()),
                "otp.notfound"));
        return MessageResponseDto.builder().message(message.getMessage("send.success", null, Locale.getDefault()))
                .messageCode("send.success").build();
    }

    // resend new link
    @Override
    public MessageResponseDto resendNewLink(String userName) {

        User user = userRepo.findByEmail(userName)
                .orElseThrow(() -> new CusNotFoundException(
                        message.getMessage("user.notfound", new Object[] { userName }, Locale.getDefault()),
                        "user.notfound"));

        if (user.getUserStatus().equals(EUserStatus.ACTIVATED)) {
            return MessageResponseDto.builder()
                    .message(message.getMessage("user.activated", new Object[] { userName }, Locale.getDefault()))
                    .messageCode("user.activated").build();
        }
        linkRepo.findByUser(user).map(link -> {
            linkService.updateLinkVerification(link, user); // update link for user to confirm
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(link, user));
            return MessageResponseDto.builder().message(message.getMessage("send.success", null, Locale.getDefault()))
                    .messageCode("send.success").build();
        }).orElseThrow(() -> new CusNotFoundException(message.getMessage("linkvr.notfound", null, Locale.getDefault()),
                "linkvr.notfound"));
        return MessageResponseDto.builder().message(message.getMessage("send.success", null, Locale.getDefault()))
                .messageCode("send.success").build();
    }

}