package study.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.Book;
import study.demo.repository.BookRepository;
import study.demo.service.BookService;
import study.demo.service.dto.request.BookFilter;
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
	
	private final BookRepository bookRepository;
	
//	@Override
//	public List<Book> findBooksByFilter(BookFilter bookFilter) {
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
//		Root<Book> root = cq.from(Book.class);
//		List<Predicate> searchCriterias = new ArrayList<>();
//		
//		return null;
//	}

	@Override
	public Page<Book> findBooksByFilter(BookFilter bookFilter, Integer pageIndex) {
		
		return null;
	}

	@Override
	public Page<Book> findAllBooks(Integer pageIndex) {
		Pageable pageable = null;
		if(pageIndex == 0) {
			pageable = PageRequest.of(0, 10);
		}else {
			pageable = PageRequest.of(pageIndex, 10);
		}
		return bookRepository.findAll(pageable);
	}

	@Override
	public Book createNewBook(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public Book updateBook(Book book,Integer bookId) {
		Book bookToUpdate = bookRepository.getReferenceById(book.getBookId());
		bookToUpdate.setTitle(book.getTitle());
		bookToUpdate.setAuthor(book.getAuthor());
		bookToUpdate.setBookStatus(book.getBookStatus());
		bookToUpdate.setImage(book.getImage());
		bookToUpdate.setPrice(book.getPrice());
		bookToUpdate.setQuantity(book.getQuantity());
		bookToUpdate.setCategory(book.getCategory());
		bookToUpdate.setEdition(book.getEdition());
		return bookRepository.save(bookToUpdate);
	}

	@Override
	public void deleteBookById(Integer booId) {
		bookRepository.deleteById(booId);;
	}


}
