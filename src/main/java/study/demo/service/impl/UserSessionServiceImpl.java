package study.demo.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.UserSession;
import study.demo.repository.UserSessionRepository;
import study.demo.security.jwt.JwtService;
import study.demo.service.UserSessionService;
import study.demo.service.exception.TokenRefreshException;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = RuntimeException.class)
public class UserSessionServiceImpl implements UserSessionService {

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final UserSessionRepository userSessionRepository;

    private final JwtService jwtService;

    @Override
    public Optional<UserSession> verifyExpiration(String userSessionId) throws TokenRefreshException {
        Optional<UserSession> userSession = userSessionRepository.findById(userSessionId);
        if(userSession.get().isExpired()) {
            throw new TokenRefreshException("Refresh token was expired. Please make a new signin request");
        }
        userSession.filter(uss -> !uss.isExpired())
        .map(UserSession::getCreatedDate).filter(
                exiredDate -> (exiredDate.toEpochMilli() + refreshTokenDurationMs) <= System.currentTimeMillis())
                .ifPresent(u -> {
                    userSession.get()
                    .setExpired(true);
                    userSessionRepository.save(userSession.get());
                    throw new TokenRefreshException("Refresh token was expired. Please make a new signin request");
                });
        return userSession;
    }

    @Override
    public Optional<UserSession> findByUserSessionId(String userSessionID) {
        return userSessionRepository.findById(userSessionID);
    }

    @Override
    public UserSession save(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }

    @Override
    public Optional<UserSession> findUserSessionByUserName(String userName) {
        return userSessionRepository.findUserSessionByUserName(userName);
    }

    @Override
    public int deleteByUserSessionId(String userSessionId) {
        return userSessionRepository.findById(userSessionId).map(uss -> {
            uss.setExpired(true);
            userSessionRepository.save(uss);
            return 1;
        }).orElseThrow(() -> new TokenRefreshException("Invalid token"));
    }

}
