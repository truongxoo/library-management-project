package study.demo.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.LinkVerification;
import study.demo.entity.User;
import study.demo.repository.LinkVerificationRepository;
import study.demo.service.LinkVerificationService;

@Service
@RequiredArgsConstructor
public class LinkVerificationServiceImpl implements LinkVerificationService {

    private final LinkVerificationRepository linkVerificationRepository;

    @Override
    public LinkVerification createVerificationToken(User user) {
        return linkVerificationRepository
                .save(LinkVerification.builder()
                        .linkCreateTime(Instant.now())
                        .user(user).verificationCode(UUID.randomUUID().toString())
                        .build());
    }

    @Override
    public Optional<LinkVerification> findUserByVerificationCode(String verificationCode) {
        return linkVerificationRepository.findUserByVerificationCode(verificationCode);
    }

    @Override
    public Optional<LinkVerification> findByVerificationCode(String verificationCode) {
        return linkVerificationRepository.findByVerificationCode(verificationCode);
    }

    @Override
    public LinkVerification saveLinkVerification(LinkVerification linkVerification) {
        return linkVerificationRepository.save(linkVerification);
    }

}
