package study.demo.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.LinkVerification;
import study.demo.entity.Member;
import study.demo.entity.OtpVerification;
import study.demo.entity.User;
import study.demo.enums.EUserStatus;
import study.demo.repository.RoleRepository;
import study.demo.repository.UserRepository;
import study.demo.service.LinkVerificationService;
import study.demo.service.OtpVerificationService;
import study.demo.service.RegisterService;
import study.demo.service.dto.request.RegisterRequest;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.event.OnRegistrationCompleteEvent;
import study.demo.service.exception.InvalidLinkException;
import study.demo.service.exception.VerifyExpirationException;
import study.demo.service.exception.UserIsUesdException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = { RuntimeException.class, Throwable.class })
public class RegisterSerivceImpl implements RegisterService {

    @Value("${app.linkExpirationTime}")
    private Long linkExpirationTime;

    @Value("${app.otpExpirationTime}")
    private Long otpExpirationTime;
    
    private final ApplicationEventPublisher eventPublisher;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final LinkVerificationService linkVerificationService;

    private final OtpVerificationService otpVerificationService;

    private final PasswordEncoder encode;

    @Override
    public MessageResponseDto register(RegisterRequest request, HttpServletRequest httpRequest) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.error("Register fail!");
            throw new UserIsUesdException("Username is already taken");
        }

        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(encode.encode(request.getPassword()));
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setGender(request.getGender());
        member.setBirthday(request.getBirthday());
        member.setPhone(request.getPhone());

        if (request.getRole() != null) {
            member.setRole(roleRepository.findByRoleName(request.getRole().name()).get());
        } else {
            member.setRole(roleRepository.findById(1).get());
        }

        if (request.getStatus() != null) {
            member.setUserStatus(request.getStatus());
        } else {
            member.setUserStatus(EUserStatus.UNVERIFY);
        }

        User user = userRepository.save(member);
        log.info("Register success!");

        String appUrl = httpRequest.getRequestURL().toString();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(appUrl, user));
        return new MessageResponseDto("Sign up success! Check your gmail to complete the verification process");
    }

    @Override
    public MessageResponseDto confirmLink(String verifyCode) {
        return linkVerificationService.findUserByVerificationCode(verifyCode).map(link -> {
            checkLinkExpiration(verifyCode);
            User user = link.getUser();
            user.setUserStatus(EUserStatus.ACTIVATED);
            userRepository.save(user);
            return new MessageResponseDto("Verified successfully, Sign in to explore");
        }).orElseThrow(() -> new InvalidLinkException("Invalid link, please make a new request"));
    }

    public LinkVerification checkLinkExpiration(String verifyCode) {
        return linkVerificationService.findByVerificationCode(verifyCode).filter(lvc -> {
            if (lvc.isExpired()) {
                throw new VerifyExpirationException("Your link has expired, please make a new request");
            }
            Long timeToExpired = lvc.getLinkCreateTime().toEpochMilli() + linkExpirationTime;
            return timeToExpired > System.currentTimeMillis();
        }).map(lvc -> {
            lvc.setExpired(true);
            return linkVerificationService.saveLinkVerification(lvc);
        }).orElseThrow(() -> new VerifyExpirationException("Your link has expired, please make a new request"));
    }

    @Override
    public MessageResponseDto confirmOtp(String otpCode) {
        return otpVerificationService.findUserByOtpCode(otpCode).map(otp -> {
            checkOtpExpiration(otpCode);
            User user = otp.getUser();
            user.setUserStatus(EUserStatus.ACTIVATED);
            userRepository.save(user);
            return new MessageResponseDto("Verified successfully, Sign in to explore");
        }).orElseThrow(() -> new InvalidLinkException("Invalid OTP, please make a new request"));
    }

    public OtpVerification checkOtpExpiration(String otpCode) {
        return otpVerificationService.findUserByOtpCode(otpCode).filter(otp -> {
            if (otp.isExpired()) {
                throw new VerifyExpirationException("Your OTP has expired, please make a new request");
            }
            Long timeToExpired = otp.getOtpCreateTime().toEpochMilli() + otpExpirationTime;
            return timeToExpired > System.currentTimeMillis();
        }).map(otp -> {
            otp.setExpired(true);
            return otpVerificationService.updateOtp(otp);
        }).orElseThrow(() -> new VerifyExpirationException("Your OTP has expired, please make a new request"));
    }
}
