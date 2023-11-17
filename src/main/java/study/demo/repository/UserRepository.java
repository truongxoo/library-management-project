package study.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import study.demo.entity.Member;
import study.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.email = ?2")
    void updateFailedAttempts(Integer failedAttempt, String email);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.otpVerification WHERE u.email= ?1")
    Optional<User> getUserWithOtp(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userSession WHERE u.email= ?1")
    Optional<User> getUserWithUserSession(String email);
   
    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = 0,u.locked = false,u.lockTime = null WHERE u.email = ?1")
    void unlockUser(String email);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

}
