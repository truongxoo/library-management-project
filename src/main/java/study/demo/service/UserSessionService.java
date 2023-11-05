package study.demo.service;

import java.util.Optional;

import study.demo.entity.UserSession;

public interface UserSessionService {
	
	Optional<UserSession> findUserSessionByUserName(String userSessionId);
	
	Optional<UserSession> findByUserSessionId(String userSessionId);
	
	UserSession verifyExpiration(String refreshToken);
	
	int deleteByUserSessionId(String userSessionId);
	
	UserSession save(UserSession userSessionId);
}
