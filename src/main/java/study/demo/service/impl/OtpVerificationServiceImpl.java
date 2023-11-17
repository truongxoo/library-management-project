package study.demo.service.impl;

import java.time.Instant;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.OtpVerification;
import study.demo.entity.User;
import study.demo.enums.EOtpType;
import study.demo.repository.OtpVerificationRepository;
import study.demo.service.OtpVerificationService;

@Service
@RequiredArgsConstructor
@Transactional
public class OtpVerificationServiceImpl implements OtpVerificationService {

    @Value("${app.otpExpirationTime}")
    private Long otpExpirationTime;

    private final OtpVerificationRepository otpRepository;

    // create otp for verify account
    @Override
    public OtpVerification createOtpVerification(User user,String otp,EOtpType otpType) {
        
        return otpRepository.save(OtpVerification.builder()
                .expiryDate(Instant.now().plusMillis(otpExpirationTime))
                .otpCode(otp)
                .otpType(otpType)
                .user(user)
                .build());
    }

    // update otp verification when user request new otp 
    @Override
    public OtpVerification updateOtpVerification(OtpVerification otpVerif,User user,String otpCode) {
        
        otpVerif.setExpiryDate(Instant.now().plusMillis(otpExpirationTime));
        otpVerif.setOtpCode(otpCode);
        return otpRepository
                .save(otpVerif);
    }
}
