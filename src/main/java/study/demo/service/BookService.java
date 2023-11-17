package study.demo.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import study.demo.entity.Book;
import study.demo.service.dto.response.BookDto;
import study.demo.service.dto.response.MessageResponseDto;

public interface BookService {

    List<BookDto> findAllBooks(Pageable pageable);

    MessageResponseDto createNewBook(BookDto bookdto);

    MessageResponseDto updateBook(BookDto bookDto, Integer bookId);
    
    BookDto findBookById(Integer bookId);

}
