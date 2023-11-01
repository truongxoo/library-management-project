package study.demo.service.impl;

import java.util.Optional;

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
    public void createVerificationToken(User user, String verificationCode) {
        linkVerificationRepository.save(LinkVerification
                .builder()
                .user(user)
                .verificationCode(verificationCode)
                .build());
    }

    @Override
    public Optional<User> findUserByVerificationCode(String verificationCode) {
        return linkVerificationRepository.findUserById(verificationCode) ;
    }

    @Override
    public Optional<LinkVerification> findById(String verificationCode) {
        return Optional.empty();
    }

}
