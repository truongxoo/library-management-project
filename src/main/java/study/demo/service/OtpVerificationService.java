package study.demo.service;

import study.demo.entity.OtpVerification;
import study.demo.entity.User;
import study.demo.enums.EOtpType;

public interface OtpVerificationService {

    OtpVerification createOtpVerification(User user,String otp,EOtpType otpType);
    
    OtpVerification updateOtpVerification(OtpVerification otpVerif,User user,String otpCode);
    
    
}
