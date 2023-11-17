package study.demo.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.Author_;
import study.demo.entity.Book;
import study.demo.entity.Book_;
import study.demo.entity.Category;
import study.demo.entity.Category_;
import study.demo.entity.User;
import study.demo.repository.BookRepository;
import study.demo.repository.UserRepository;
import study.demo.service.criteria.BookCriteria;
import study.demo.service.dto.response.BookDto;
import study.demo.utils.ConverterUtil;
import tech.jhipster.service.QueryService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookQueryService extends QueryService<Book> {

    private final BookRepository bookRepository;

    private final ConverterUtil converterUtil;

    public List<BookDto> findByCriteria(BookCriteria criteria, Pageable pageable) {
        log.info("find by criteria : {}", criteria);
    
        final Specification<Book> specification = createSpecification(criteria);
        Page<Book> books = bookRepository.findAll(specification, pageable);
        return books.stream()
                .map(converterUtil::convertToBookDto)
                .toList();
    }

    protected Specification<Book> createSpecification(BookCriteria criteria) {
        Specification<Book> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Book_.bookId));
            }
            if (criteria.getEdition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEdition(), Book_.edition));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Book_.title));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Book_.quantity));
            }
            if (criteria.getReleaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReleaseDate(), Book_.releaseDate));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                        root -> root.join(Book_.category, JoinType.LEFT).get(Category_.categoryId)));
            }
            if (criteria.getAuthorId() != null) {
                specification = specification.and(buildSpecification(criteria.getAuthorId(),
                        root -> root.join(Book_.author, JoinType.LEFT).get(Author_.authorId)));
            }

        }
        return specification;
    }

}
