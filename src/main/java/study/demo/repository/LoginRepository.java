package study.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import study.demo.entity.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {

}
