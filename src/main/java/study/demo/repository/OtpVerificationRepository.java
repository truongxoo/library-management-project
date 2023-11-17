package study.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import study.demo.entity.OtpVerification;
import study.demo.entity.User;
import study.demo.enums.EOtpType;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Integer>{

    Optional<OtpVerification> findByUser(User user);
    
    @Query("SELECT o FROM OtpVerification o JOIN FETCH o.user WHERE o.otpCode= ?1 AND o.otpType= ?2")
    OtpVerification findByOtpCodeAndOtpType(String otpCode,EOtpType otpType);

}
