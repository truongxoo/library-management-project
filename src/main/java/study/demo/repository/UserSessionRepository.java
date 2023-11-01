package study.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import study.demo.entity.User;
import study.demo.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    
	Optional<UserSession> findById(String refreshtoken);
	
	@Query("Select r FROM UserSession r WHERE r.user.userId = 1")
	Optional<UserSession> findByUserId(Integer userId);
	
	@Modifying
	int deleteByUser(User user);
}
