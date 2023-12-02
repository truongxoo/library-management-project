package study.demo.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.service.BookService;
import study.demo.service.BorrowerRecordService;
import study.demo.service.dto.response.BookDto;
import study.demo.service.dto.response.MessageResponseDto;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {


    private final BookService bookService;
    
    private final BorrowerRecordService recordService;
    

    
    @GetMapping("/books/{id}/borrow")
    public ResponseEntity<MessageResponseDto> borrowBook(@PathVariable("id")Integer bookId) {
       log.info("Request borrow book is processing");
        return  ResponseEntity.status(HttpStatus.CREATED).body(recordService.createNewBorrowerRecord(bookId));
    }
    
    @GetMapping("/books/{id}/return")
    public ResponseEntity<MessageResponseDto> returnBook(@PathVariable("id")Integer recordId) {
        log.info("Request borrow book is processing");
        return  ResponseEntity.ok(recordService.returnBook(recordId));
    }
    
    @GetMapping("/books/{id}")
    public ResponseEntity<BookDto> getBookDetail(@PathVariable("id")Integer bookId) {
       log.info("Request borrow book is processing");
        return  ResponseEntity.ok(bookService.findBookById(bookId));
    }
  
}
