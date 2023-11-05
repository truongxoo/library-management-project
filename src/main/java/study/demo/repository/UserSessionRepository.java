package study.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import study.demo.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    
	Optional<UserSession> findById(String refreshtoken);
	
	@Query("SELECT r FROM UserSession r WHERE r.user.email = ?1")
	Optional<UserSession> findUserSessionByUserName(String userName);
	
//	@Modifying
//    @Query("Delete u FROM UserSession u INNER JOIN User s ON s.userSession.userSessionId "
//            + "= u.userSessionId WHERE s.email= ?1")
//	int deleteByUserName(String userName);
}
