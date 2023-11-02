package study.demo.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import study.demo.entity.UserSession;
import study.demo.service.exception.TokenRefreshException;

public interface UserSessionService {
	
	Optional<UserSession> findUserSessionByUserName(String userSessionId);
	
	Optional<UserSession> findByUserSessionId(String userSessionId);
	
	Optional<UserSession> verifyExpiration(String refreshToken) throws TokenRefreshException;
	
	int deleteByUserSessionId(String userSessionId);
	
	UserSession save(UserSession userSessionId);
}
