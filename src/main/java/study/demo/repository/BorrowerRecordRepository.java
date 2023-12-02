package study.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import study.demo.entity.Book;
import study.demo.entity.BorrowerRecord;
import study.demo.entity.User;

@Repository
public interface BorrowerRecordRepository extends JpaRepository<BorrowerRecord, Integer> {

   Optional<BorrowerRecord> findByUserAndBook(User user,Book book);
}
