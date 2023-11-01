package study.demo.service.impl;

import java.time.Instant;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.UserSession;
import study.demo.repository.UserRepository;
import study.demo.repository.UserSessionRepository;
import study.demo.service.UserSessionService;
import study.demo.service.exception.TokenRefreshException;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final UserSessionRepository userSessionRepository;

    private final UserRepository userRepository;

//    @Override
//    public Optional<UserSession> verifyExpiration(String refreshToken) throws TokenRefreshException {
//        Optional<UserSession> userSession= userSessionRepository.findById(refreshToken);
//        userSession.map(UserSession::getExpirationDate)
//        .filter(exiredDate -> (exiredDate.toEpochMilli() + refreshTokenDurationMs) <= System.currentTimeMillis())
//        .ifPresent(u -> {
//            userSessionRepository.deleteById(refreshToken);
//            throw new TokenRefreshException(refreshToken,
//                    "Refresh token was expired. Please make a new signin request");
//        });
//        return userSession;
//    }

    @Transactional
    public int deleteByUserId(Integer userId) {
        return userSessionRepository.deleteByUser(userRepository.findById(userId).get());
    }

    @Override
    public Optional<UserSession> findByUserId(Integer userId) {
        return userSessionRepository.findByUserId(userId);
    }

    @Override
    public Optional<UserSession> findByUserSessionId(String userSessionID) {
        return userSessionRepository.findById(userSessionID);
    }

    @Override
    public UserSession save(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }

}
