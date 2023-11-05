package study.demo.service.impl;

import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.UserSession;
import study.demo.repository.UserSessionRepository;
import study.demo.security.jwt.JwtService;
import study.demo.service.UserSessionService;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.exception.VerifyExpirationException;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final UserSessionRepository userSessionRepository;

    private final JwtService jwtService;
    
    private final MessageSource messages;

    @Override
    public UserSession verifyExpiration(String userSessionId) throws VerifyExpirationException {
        
        UserSession userSession = userSessionRepository.findById(userSessionId)
                .orElseThrow(()-> new DataInvalidException(messages.getMessage("refreshtoken.notfound", null, null)));
        if(userSession.isExpired()) {
            throw new VerifyExpirationException(messages.getMessage("refreshtoken.expired", null, Locale.getDefault()));
        }
        Optional.of(userSession)
            .map(UserSession::getCreatedDate)
            .filter(exiredDate -> (
                    exiredDate.toEpochMilli() + refreshTokenDurationMs) <= System.currentTimeMillis())
            .ifPresent(u -> {
                    userSession.setExpired(true);
                    userSessionRepository.save(userSession);
                    throw new VerifyExpirationException(messages.getMessage("refreshtoken.expired", null, null));
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
        }).orElseThrow(() -> new VerifyExpirationException("Invalid token"));
    }

}
