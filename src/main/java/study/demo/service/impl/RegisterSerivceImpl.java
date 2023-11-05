package study.demo.service.impl;

import java.time.Instant;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.LinkVerification;
import study.demo.entity.Member;
import study.demo.entity.OtpVerification;
import study.demo.entity.User;
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
import study.demo.service.exception.DataInvalidException;
import study.demo.service.exception.VerifyExpirationException;

@Slf4j
@Service
@RequiredArgsConstructor
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

    // register new member with default status: UNVERIFY
    @Override
    public MessageResponseDto register(RegisterRequestDto request, HttpServletRequest httpRequest) {

        userRepo.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new DataInvalidException(u.getEmail() + " is already taken");
        }); // username is unique

        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(encode.encode(request.getPassword()));
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setGender(request.getGender());
        member.setBirthday(request.getBirthday());
        member.setPhone(request.getPhone());

        if (request.getRole() != null) {
            member.setRole(roleRepo.findByRoleName(request.getRole().name()).get());
        } else {
            member.setRole(roleRepo.findById(1).get());
        }

        if (request.getStatus() != null) {
            member.setUserStatus(request.getStatus());
        } else {
            member.setUserStatus(EUserStatus.UNVERIFY);
        }

        User userEvent = userRepo.save(member);
        log.info("Register success!");

        // publish an event when register successfully
        String appUrl = httpRequest.getRequestURL().toString();
        LinkVerification link = linkService.createLinkVerification(userEvent);
        OtpVerification otp = otpVrfService.createOtpVerification(userEvent);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(appUrl, otp, link, userEvent));
        return new MessageResponseDto(
                message.getMessage("register.success", new Object[] { userEvent.getEmail() }, Locale.ENGLISH));
    }

    // change user status and delete link after verifying successfully
    @Override
    public MessageResponseDto confirmLink(String verifyCode) {
        LinkVerification linkvr = linkRepo.findUserByVerificationCode(verifyCode).orElseThrow(
                () -> new VerifyExpirationException(message.getMessage("linkvr.notfound", null, Locale.ENGLISH)));

        if (linkvr.getExpiryDate().isAfter(Instant.now())) {
            User user = linkvr.getUser();
            user.setUserStatus(EUserStatus.ACTIVATED);
            userRepo.save(user);
            linkRepo.delete(linkvr);
            otpRepo.delete(user.getOtpVerification()); // delete both link and otp when verifying successfully
        } else {
            throw new VerifyExpirationException(message.getMessage("link.expired", null, Locale.ENGLISH));
        }
        return new MessageResponseDto(message.getMessage("verify.success", null, Locale.ENGLISH));
    }

    // change user status and delete otp after verifying successfully
    @Override
    public MessageResponseDto confirmOtp(String otpCode) {
        OtpVerification otp = otpRepo.findUserByOtpCode(otpCode).orElseThrow(() -> new VerifyExpirationException(
                message.getMessage("otp.notfound", new Object[] { otpCode }, Locale.ENGLISH)));

        if (otp.getExpiryDate().isAfter(Instant.now())) {
            User user = otp.getUser();
            user.setUserStatus(EUserStatus.ACTIVATED);
            userRepo.save(user);
            otpRepo.delete(otp);
            linkRepo.delete(user.getLinkVerification()); // delete both link and otp when verifying successfully
        } else {
            throw new VerifyExpirationException(message.getMessage("link.expired", null, Locale.ENGLISH));
        }
        return new MessageResponseDto(message.getMessage("verify.success", null, Locale.ENGLISH));
    }

    // resend new OTP
    @Override
    public MessageResponseDto resendNewOtp(HttpServletRequest httpRequest, String userName) {
        User user = userRepo.findByEmail(userName)
                .orElseThrow(() -> new DataInvalidException(message.getMessage("user.notfound",new Object[] {userName}, Locale.ENGLISH)));
        if(user.getUserStatus().equals(EUserStatus.ACTIVATED)) {
            return new MessageResponseDto(message.getMessage("user.activated",new Object[] {userName}, Locale.ENGLISH));
        }
        otpRepo.findByUser(user)
        .map(otp -> {
            otpVrfService.updateOtpVerification(otp, user);    // update OTP for user to confirm
            String appUrl = httpRequest.getRequestURL().toString();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(appUrl, otp, user));
            return new MessageResponseDto(message.getMessage("send.success", null, Locale.ENGLISH));
        })
        .orElseThrow(() -> new DataInvalidException(message.getMessage("otp.notfound",null, Locale.ENGLISH)));
        return new MessageResponseDto(message.getMessage("send.success", null, Locale.ENGLISH));
    }

    // resend new link
    @Override
    public MessageResponseDto resendNewLink(HttpServletRequest httpRequest, String userName) {
        User user = userRepo.findByEmail(userName).orElseThrow(() -> new DataInvalidException(
                message.getMessage("user.notfound", new Object[] { userName }, Locale.ENGLISH)));
        if(user.getUserStatus().equals(EUserStatus.ACTIVATED)) {
            return new MessageResponseDto(message.getMessage("user.activated",new Object[] {userName}, Locale.ENGLISH));
        }
        linkRepo.findByUser(user)
        .map(link -> {
            linkService.updateLinkVerification(link, user);    // update link for user to confirm
            String appUrl = httpRequest.getRequestURL().toString();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(appUrl, link, user));
            
            return new MessageResponseDto(message.getMessage("send.success", null, Locale.ENGLISH));
        })
        .orElseThrow(() -> new DataInvalidException(message.getMessage("linkvr.notfound", null, Locale.ENGLISH)));
        return new MessageResponseDto(message.getMessage("send.success", null, Locale.ENGLISH));
    }
}