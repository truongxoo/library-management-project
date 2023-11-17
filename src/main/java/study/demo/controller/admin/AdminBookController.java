package study.demo.controller.admin;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.twilio.http.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.Book;
import study.demo.repository.BookRepository;
import study.demo.repository.UserRepository;
import study.demo.service.BookService;
import study.demo.service.FileUploadingService;
import study.demo.service.UserInfoService;
import study.demo.service.dto.response.BookDto;
import study.demo.service.dto.response.MessageResponseDto;

@Slf4j
@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;

    private final UserRepository userRepository;

    private final BookRepository bookRepository;
    
    private final FileUploadingService fileUploadingService;

    
    // create new book
    @PostMapping("/books")
    public ResponseEntity<Void> createNewBook(@Valid @RequestBody BookDto BookDto) {
        log.info("Create new book is processing");
        bookService.createNewBook(BookDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // udpate book by id
    @PutMapping("/books/{id}")
    public ResponseEntity<MessageResponseDto> updateBookById(@PathVariable(value = "id") Integer bookId,
            @Valid @RequestBody BookDto bookDto) {
        log.info("Update book is processing");
        return ResponseEntity.ok(bookService.updateBook(bookDto, bookId));
    }

    // delete book by id
    @DeleteMapping("books/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable(value = "id") Integer bookId) {
        bookRepository.deleteById(bookId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("books/upload")
    public ResponseEntity<MessageResponseDto> uploadFileCSV(@RequestParam(value = "file") MultipartFile file) {
        return  ResponseEntity.ok(fileUploadingService.fileUploading(file));
    }
    
}
