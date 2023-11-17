package study.demo.controller.common;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.repository.BookRepository;
import study.demo.service.BookQueryService;
import study.demo.service.BookService;
import study.demo.service.LogoutService;
import study.demo.service.criteria.BookCriteria;
import study.demo.service.dto.response.BookDto;
import study.demo.service.impl.AuthenticationServiceImpl;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    
    private final BookRepository bokBookRepository;
    
    private final BookQueryService bookQueryService;
    
    private final BookService bookService;
      
    // search book by filter
    @GetMapping("/books/search")  
    public ResponseEntity<List<BookDto>> findBookByCriteria(BookCriteria criteria, @PageableDefault(size = 10)Pageable pageable) {
        log.info("Retrieving all books.......");
        return ResponseEntity.ok().body(bookQueryService.findByCriteria(criteria,pageable));
    }
    
    // get list of books    
    @GetMapping("/books")
    public  ResponseEntity<List<BookDto>> findAllBooks(@PageableDefault(size = 10)Pageable pageable) {
        log.info("Retrieving all books.......");
        return ResponseEntity.ok(bookService.findAllBooks(pageable));
    }
    
}
