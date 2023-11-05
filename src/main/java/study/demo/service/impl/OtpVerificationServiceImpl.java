package study.demo.service.impl;

import java.time.Instant;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.OtpVerification;
import study.demo.entity.User;
import study.demo.repository.OtpVerificationRepository;
import study.demo.service.OtpVerificationService;

@Service
@RequiredArgsConstructor
public class OtpVerificationServiceImpl implements OtpVerificationService {

    @Value("${app.otpExpirationTime}")
    private Long otpExpirationTime;

    private final OtpVerificationRepository otpRepository;

    // create otp for verify account
    @Override
    public OtpVerification createOtpVerification(User user) {
        
        Random random = new Random();
        String otpCode = String.valueOf(random.nextInt(999999));
        
        return otpRepository.save(OtpVerification.builder()
                .createTime(Instant.now())
                .expiryDate(Instant.now().plusMillis(otpExpirationTime))
                .otpCode(otpCode)
                .user(user)
                .build());
    }

    // update otp verification when user request new otp 
    @Override
    public OtpVerification updateOtpVerification(OtpVerification otpVerif,User user) {
        
        Random random = new Random();
        String otpCode = String.valueOf(random.nextInt(999999));
        
        otpVerif.setCreateTime(Instant.now());
        otpVerif.setExpiryDate(Instant.now().plusMillis(otpExpirationTime));
        otpVerif.setOtpCode(otpCode);
        otpVerif.setUser(user);
        
        return otpRepository
                .save(otpVerif);
    }
}
