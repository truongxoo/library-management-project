package study.demo.service;

import java.util.Optional;

import study.demo.entity.LinkVerification;
import study.demo.entity.User;

public interface LinkVerificationService {
    
    void createVerificationToken(User user, String verificationCode);
    
    Optional<User> findUserByVerificationCode(String verificationCode);
    
    Optional<LinkVerification> findById(String verificationCode);
    
}
