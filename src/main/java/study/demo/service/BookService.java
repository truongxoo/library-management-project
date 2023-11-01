package study.demo.service;

import org.springframework.data.domain.Page;

import study.demo.entity.Book;
import study.demo.service.dto.request.BookFilter;

public interface BookService {

    Page<Book> findBooksByFilter(BookFilter bookFilter, Integer pageIndex);

    Page<Book> findAllBooks(Integer pageIndex);

    Book createNewBook(Book book);

    Book updateBook(Book book, Integer bookId);

    void deleteBookById(Integer booId);

}
