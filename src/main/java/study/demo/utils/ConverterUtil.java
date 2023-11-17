package study.demo.utils;

import java.time.Instant;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.Author;
import study.demo.entity.Book;
import study.demo.entity.Category;
import study.demo.entity.Member;
import study.demo.entity.User;
import study.demo.repository.AuthorRepository;
import study.demo.repository.BookRepository;
import study.demo.repository.CategoryRepository;
import study.demo.service.dto.response.AdminUserDto;
import study.demo.service.dto.response.BookDto;
import study.demo.service.exception.CusNotFoundException;
import study.demo.service.exception.DataInvalidException;

@Service
@RequiredArgsConstructor
public class ConverterUtil {

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;
    
    private final BookRepository bookRepository;

    private final MessageSource message;

    public Book convertToBook(BookDto bookDto) {
        Book book = Book.builder()
                .edition(bookDto.getEdition())
                .title(bookDto.getTitle())
                .price(bookDto.getPrice())
                .quantity(bookDto.getQuantity())
                .image(bookDto.getImage())
                .releaseDate(bookDto.getReleaseDate())
                .build();

        if (bookDto.getAuthor() != null) {
            Author author = authorRepository.findByName(bookDto.getAuthor()).orElseThrow(() -> new CusNotFoundException(
                    message.getMessage("author.notfound", new Object[] { bookDto.getAuthor() }, Locale.getDefault()),"author.notfound"));
            book.setAuthor(author);
        }

        if (bookDto.getCategory() != null) {
            Category category = categoryRepository.findByCategoryName(bookDto.getCategory())
                    .orElseThrow(() -> new CusNotFoundException(message.getMessage("category.notfound",
                            new Object[] { bookDto.getCategory() }, Locale.getDefault()),"category.notfound"));
            book.setCategory(category);
        }
       
        
        return book;
    }

    public BookDto convertToBookDto(Book book) {
        return BookDto.builder()
                .bookID(book.getBookId())
                .title(book.getTitle())
                .edition(book.getEdition())
                .price(book.getPrice())
                .author(book.getAuthor().getName())
                .image(book.getImage())
                .quantity(book.getQuantity())
                .category(book.getCategory().getCategoryName())
                .releaseDate(book.getReleaseDate())
                .build();
    }
    
    public AdminUserDto convertAdminUserDto(Member user) {
        return AdminUserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .borrowerRecord(user.getBorrowerRecord().size())
                .birthDay(user.getBirthday())
                .phone(user.getPhone())
                .gender(user.getGender().name())
                .locked(user.isLocked())
                .build();
    }
}
