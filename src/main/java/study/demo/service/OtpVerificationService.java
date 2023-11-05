package study.demo.service;

import study.demo.entity.OtpVerification;
import study.demo.entity.User;

public interface OtpVerificationService {

    OtpVerification createOtpVerification(User user);
    
    OtpVerification updateOtpVerification(OtpVerification otpVerif,User user);
    
    
}
