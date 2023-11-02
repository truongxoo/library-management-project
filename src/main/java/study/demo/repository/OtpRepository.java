package study.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import study.demo.entity.OtpVerification;
import study.demo.entity.User;

public interface OtpRepository extends JpaRepository<OtpVerification, Integer>{

    Optional<OtpVerification> findByOtpCode(String otpCode);

    Optional<OtpVerification> findByUser(User user);

    Optional<OtpVerification> findUserByOtpCode(String otpCode);

}
