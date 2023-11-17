package study.demo.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.Author;
import study.demo.entity.Book;
import study.demo.entity.Category;
import study.demo.repository.AuthorRepository;
import study.demo.repository.BookRepository;
import study.demo.repository.CategoryRepository;
import study.demo.service.BookService;
import study.demo.service.dto.response.BookDto;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.exception.DataInvalidException;
import study.demo.utils.ConverterUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    
    private final ConverterUtil converter;
    
    private final MessageSource messages;
    
    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public List<BookDto> findAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return books.stream()
                .map(converter::convertToBookDto)
                .toList();
    }

    @Override
    public MessageResponseDto createNewBook(BookDto bookDto) {
        if (bookDto == null) {
            throw new DataInvalidException(messages.getMessage("book.notnull", null, Locale.getDefault()),"book.notnull");
        }
        Book book = converter.convertToBook(bookDto);
        bookRepository.save(book);
        return MessageResponseDto.builder()
                .messageCode("book.added.successfully")
                .message(messages.getMessage("book.added.successfully", null, Locale.getDefault()))
                .build();
    }

    @Override
    public MessageResponseDto updateBook(BookDto bookDto, Integer bookId) {

        if (bookId ==null) {
            throw new DataInvalidException(messages.getMessage("bookid.notnull",null, Locale.getDefault()),"bookid.notnull");
        }
        if (bookDto ==null) {
            throw new DataInvalidException(messages.getMessage("book.notnull",null, Locale.getDefault()),"book.notnull");
        }
        Book bookToUpdate = converter.convertToBook(bookDto);
        bookToUpdate.setBookId(bookId);
         bookRepository.save(bookToUpdate);
         return MessageResponseDto.builder()
                 .messageCode("book.updated.successfully")
                 .message(messages.getMessage("book.updated.successfully", null, Locale.getDefault()))
                 .build();
    }

 
    @Override
    public BookDto findBookById(Integer bookId) {
        if (bookId ==null) {
            throw new DataInvalidException(messages.getMessage("bookid.notnull",null, Locale.getDefault()),"bookid.notnull");
        }
        Book book =bookRepository.findById(bookId).orElseThrow(()-> new DataInvalidException(messages.getMessage("book.notfound",null, Locale.getDefault()),"book.notfound"));
        return converter.convertToBookDto(book);
    }
    
    
//    Book bookToUpdate = bookRepository.getReferenceById(bookDto.getBookID());
//    
//    bookToUpdate.setEdition(bookDto.getEdition());
//    bookToUpdate.setTitle(bookDto.getTitle());
//    bookToUpdate.setPrice(bookDto.getPrice());
//    bookToUpdate.setQuantity(bookDto.getQuantity());
//    bookToUpdate.setImage(bookDto.getImage());
//    bookToUpdate.setReleaseDate(bookDto.getReleaseDate());
//    
//    if (bookDto.getAuthor() != null) {
//        Author author = authorRepository.findByName(bookDto.getAuthor()).orElseThrow(() -> new DataInvalidException(
//                messages.getMessage("author.notfound", new Object[] { bookDto.getAuthor() }, Locale.getDefault())));
//        bookToUpdate.setAuthor(author);
//    }
//
//    if (bookDto.getCategory() != null) {
//        Category category = categoryRepository.findByCategoryName(bookDto.getCategory())
//                .orElseThrow(() -> new DataInvalidException(messages.getMessage("category.notfound",
//                        new Object[] { bookDto.getCategory() }, Locale.getDefault())));
//        bookToUpdate.setCategory(category);
//    }


}
