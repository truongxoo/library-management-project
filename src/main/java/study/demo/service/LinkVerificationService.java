package study.demo.service;

import study.demo.entity.LinkVerification;
import study.demo.entity.User;

public interface LinkVerificationService {
    
    LinkVerification createLinkVerification(User user);
    
    LinkVerification updateLinkVerification(LinkVerification linkVerification,User user);
}
