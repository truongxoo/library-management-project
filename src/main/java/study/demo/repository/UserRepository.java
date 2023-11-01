package study.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import study.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

//	List<User> findAll(Specification<User> spec);

//	Page<User> findAll(Specification<User> spec, Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.email = ?2")
    public void updateFailedAttempts(int failAttempts, String email);

    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = 0,u.locked = false,u.lockTime = null WHERE u.email = ?1")
    public void unlockUser(String email);

}
