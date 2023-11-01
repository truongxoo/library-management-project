package study.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.Book;
import study.demo.service.BookService;
import study.demo.service.dto.response.BookDto;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    private final ModelMapper modelMapper;

    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDto> findAllBooks(@RequestParam(name = "page", required = false) Integer pageIndex) {

        log.info("Retrieving all books.......");
        Page<Book> books = bookService.findAllBooks(pageIndex);

        if (books.isEmpty()) {
            log.warn("No book is retrieved");
            return null;
        }
        return books.stream().map(book -> modelMapper.map(book, BookDto.class)).collect(Collectors.toList());
    }

}
