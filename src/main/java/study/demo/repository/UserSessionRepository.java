package study.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import study.demo.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    
	Optional<UserSession> findById(String refreshtoken);
	
	@Query("SELECT r FROM UserSession r WHERE r.user.email = ?1")
	Optional<UserSession> findUserSessionByUserName(String userName);
	
	@Modifying
    @Query("UPDATE UserSession u SET u.isExpired= true WHERE u.user.userId= ?1" )
	void deleteUserSessionByUserName(Integer userId);
}
