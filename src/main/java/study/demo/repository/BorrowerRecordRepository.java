package study.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import study.demo.entity.BorrowerRecord;

@Repository
public interface BorrowerRecordRepository extends JpaRepository<BorrowerRecord, Integer> {

}
