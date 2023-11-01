package study.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import study.demo.entity.LinkVerification;
import study.demo.entity.User;

@Repository
public interface LinkVerificationRepository extends JpaRepository<LinkVerification, String>{
    
    Optional<LinkVerification> findById(String id);
    
    Optional<LinkVerification> findByUser(User user);
    
    Optional<User> findUserById(String verificationCode);
    
}
