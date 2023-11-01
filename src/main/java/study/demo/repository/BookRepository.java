package study.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import study.demo.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Page<Book> findAll(Pageable pageable);

    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

}
