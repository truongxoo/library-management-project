package study.demo.service.impl;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.LinkVerification;
import study.demo.entity.Member;
import study.demo.entity.User;
import study.demo.enums.EUserStatus;
import study.demo.repository.RoleRepository;
import study.demo.repository.UserRepository;
import study.demo.service.LinkVerificationService;
import study.demo.service.RegisterService;
import study.demo.service.dto.request.RegisterRequest;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.event.OnRegistrationCompleteEvent;
import study.demo.service.exception.InvalidLinkException;
import study.demo.service.exception.LinkExpirationException;
import study.demo.service.exception.UserIsUesdException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterSerivceImpl implements RegisterService {

    @Value("${app.linkExpirationTime}")
    private Long linkExpirationTime;

    private final ApplicationEventPublisher eventPublisher;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final LinkVerificationService linkVerificationService;

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

        String appUrl = httpRequest.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(appUrl, user));
        return new MessageResponseDto("Sign up success! Please login again and explore");
    }

    @Override
    public MessageResponseDto confirmRegistration(String verifyCode) {
        return linkVerificationService.findUserByVerificationCode(verifyCode).map(user -> {
            user.setUserStatus(EUserStatus.ACTIVATED);
            userRepository.save(user);
            return new MessageResponseDto("Verified successfully, Sign in to explore");
        }).orElseThrow(() -> new InvalidLinkException("Invalid link, please make a new request"));
    }

    public LinkVerification checkLinkExpiration(String verifyCode) {
        return linkVerificationService.findById(verifyCode).filter(
                code -> code.getLinkCreateTime().toEpochMilli() + linkExpirationTime > System.currentTimeMillis())
                .orElseThrow(() -> new LinkExpirationException("Your link has expired, please make a new request"));
    }
}
