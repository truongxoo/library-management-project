package study.demo.service;

import java.util.Optional;

import study.demo.entity.OtpVerification;
import study.demo.entity.User;

public interface OtpVerificationService {

    OtpVerification createOtpVerification(User user);

    Optional<OtpVerification> findUserByOtpCode(String verificationCode);

    Optional<OtpVerification> findByOtpCode(String verificationCode);
    
    OtpVerification updateOtp(OtpVerification optVerification);

}
