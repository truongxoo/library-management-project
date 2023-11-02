package study.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;

import study.demo.entity.LinkVerification;
import study.demo.entity.User;

public interface LinkVerificationService {
    
    LinkVerification createVerificationToken(User user);
    
    Optional<LinkVerification> findUserByVerificationCode(String verificationCode);
    
    Optional<LinkVerification> findByVerificationCode(String verificationCode);
    
    LinkVerification saveLinkVerification(LinkVerification linkVerification);
    
}
