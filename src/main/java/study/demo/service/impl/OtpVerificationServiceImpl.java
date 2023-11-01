package study.demo.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.User;
import study.demo.service.OtpVerificationService;

@Service
@RequiredArgsConstructor
public class OtpVerificationServiceImpl implements OtpVerificationService {
    @Override
    public void generateOneTimePassword(User User) {

    }

    @Override
    public void sendOTPEmail(User User, String OTP) {

    }

    @Override
    public void clearOTP(User User) {

    }

}
