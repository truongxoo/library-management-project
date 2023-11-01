package study.demo.service;

import study.demo.entity.User;

public interface OtpVerificationService {
    
     void generateOneTimePassword(User User);
        
     void sendOTPEmail(User User, String OTP);
 
     void clearOTP(User User);
 
}
