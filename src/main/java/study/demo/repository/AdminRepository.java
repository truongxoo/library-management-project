package study.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import study.demo.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

}
