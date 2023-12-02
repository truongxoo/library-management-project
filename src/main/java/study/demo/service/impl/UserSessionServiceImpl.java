package study.demo.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import study.demo.entity.UserSession;
import study.demo.repository.UserSessionRepository;
import study.demo.service.UserSessionService;
import study.demo.service.exception.CusNotFoundException;
import study.demo.service.exception.DataInvalidException;
import study.demo.service.exception.VerifyExpirationException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSessionServiceImpl implements UserSessionService {

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final UserSessionRepository userSessionRepository;
    
    private final MessageSource messages;

    @Override
    public UserSession verifyExpiration(String userSessionId) throws VerifyExpirationException {
        
        UserSession userSession = userSessionRepository.findById(userSessionId)
                .orElseThrow(()-> new CusNotFoundException(messages.getMessage(
                        "refreshtoken.notfound", null, Locale.getDefault()),"refreshtoken.notfound"));
        if(userSession.isExpired()) {
            throw new VerifyExpirationException(messages.getMessage(
                    "refreshtoken.expired", null, Locale.getDefault()),"refreshtoken.expired");
        }
        
        if ( userSession.getExpiryDate().isBefore(Instant.now())) {
            userSession.setExpired(true);
            userSessionRepository.save(userSession);
            throw new VerifyExpirationException(messages.getMessage(
                    "refreshtoken.expired", null, Locale.getDefault()),"refreshtoken.expired");            
        }
        userSession.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        userSessionRepository.save(userSession);
        return userSession;
    }

    @Override
    @Transactional(readOnly = true)
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
    public void deleteByUserSessionId(String userSessionId) {
        userSessionRepository.findById(userSessionId).ifPresentOrElse(userSessionRepository::delete,
                () -> new VerifyExpirationException(messages.getMessage(
                        "token.invalid", null, Locale.getDefault()),"token.invalid"));
    }
}
