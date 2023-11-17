package study.demo.service.impl;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.LinkVerification;
import study.demo.entity.User;
import study.demo.repository.LinkVerificationRepository;
import study.demo.service.LinkVerificationService;

@Service
@RequiredArgsConstructor
@Transactional
public class LinkVerificationServiceImpl implements LinkVerificationService {
    
    @Value("${app.linkExpirationTime}")
    private Long linkExpirationTime;

    private final LinkVerificationRepository linkRepo;

    // create link verification 
    @Override
    public LinkVerification createLinkVerification(User user) {
        return linkRepo.save(LinkVerification.builder()
                        .expiryDate(Instant.now().plusMillis(linkExpirationTime))
                        .user(user).verificationCode(UUID.randomUUID().toString())
                        .build());
    }
    
    // update link verification when user request new link
    @Override
    public LinkVerification updateLinkVerification(LinkVerification linkVerf, User user) {

        linkVerf.setExpiryDate(Instant.now().plusMillis(linkExpirationTime));
        linkVerf.setVerificationCode(UUID.randomUUID().toString());
        return linkRepo.save(linkVerf);
    }

}
