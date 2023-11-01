package study.demo.service;

import java.util.Optional;

import study.demo.entity.UserSession;
import study.demo.service.exception.TokenRefreshException;

public interface UserSessionService {
	
	Optional<UserSession> findByUserId(Integer userSessionId);
	
	Optional<UserSession> findByUserSessionId(String userSessionID);
	
//	Optional<UserSession> verifyExpiration(String refreshToken) throws TokenRefreshException;
	
	int deleteByUserId(Integer userId);
	
	UserSession save(UserSession userSession);
}
