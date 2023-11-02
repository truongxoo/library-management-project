package study.demo.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.OtpVerification;
import study.demo.entity.User;
import study.demo.repository.OtpRepository;
import study.demo.service.OtpVerificationService;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = RuntimeException.class)
public class OtpVerificationServiceImpl implements OtpVerificationService {

    private final OtpRepository otpRepository;

    @Override
    public OtpVerification createOtpVerification(User user) {
        Random rnd = new Random();
        String otpCode = String.valueOf(rnd.nextInt(999999));
        return otpRepository
                .save(OtpVerification.builder().otpCreateTime(Instant.now()).otpCode(otpCode).user(user).build());
    }

    @Override
    public Optional<OtpVerification> findUserByOtpCode(String verificationCode) {
        return otpRepository.findUserByOtpCode(verificationCode);
    }

    @Override
    public Optional<OtpVerification> findByOtpCode(String verificationCode) {
        return otpRepository.findByOtpCode(verificationCode);
    }

    @Override
    public OtpVerification updateOtp(OtpVerification optVerification) {
        return otpRepository.save(optVerification);
    }

}
