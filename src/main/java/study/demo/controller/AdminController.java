package study.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.User;
import study.demo.repository.UserRepository;
import study.demo.service.BookService;
import study.demo.service.UserService;
import study.demo.service.dto.request.UserFilterDto;
import study.demo.service.dto.response.UserDto;

@Slf4j
@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

	private final UserService userService;

	private final BookService bookService;

	private final ModelMapper modelMapper;
	
	private final UserRepository userRepository;

	@GetMapping("/users")
	public List<UserDto> findAllUsers(@RequestParam(name = "page", required = false) Integer pageIndex) {

		log.info("Retrieving all users.......");
		Page<User> users = userService.findAllUsers(pageIndex);
		List<UserDto> userl = new ArrayList<>();
		
		if (users.getSize() == 0) {
			log.warn("No user is retrieved");
			return null;
		}
		return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());

	}

	@GetMapping("/users/filter")
	public List<UserDto> findUserByFilter(@RequestBody UserFilterDto userFilter,
			@RequestParam(name = "page", required = false) Integer pageIndex) {

		log.info("Retrieving users that match the criteria");
		Page<User> users = userService.findUsersByFilter(userFilter, null);

		if (users.isEmpty()) {
			log.warn("No user match");
			return null;
		}
		return users.stream().map(user -> modelMapper.map(users, UserDto.class)).collect(Collectors.toList());

	}

//	@GetMapping("/books")
//	public List<BookDTO> findAllBooks(@RequestParam(name = "page", required = false) Integer pageIndex){
//		
//		log.info("Retrieving all books.......");
//		Page<Book> books = bookService.findAllBooks(pageIndex);
//		
//		if (books.isEmpty()) {
//			log.warn("No book is retrieved");
//			return null;
//		}
//		return books.stream().map(book -> modelMapper.map(book,BookDto.class)).collect(Collectors.toList());
//	}

//	@PostMapping("/book")
//	public ResponseEntity<Void> createNewBook(@RequestBody BookDto BookDto) {
//		if (bookDTO == null) {
//			return ResponseEntity.noContent().build();
//		}
//		Book book = modelMapper.map(bookDTO, Book.class);
//		bookService.createNewBook(book);
//		return ResponseEntity.noContent().build();
//
//	}

//	@PutMapping("/book/{id}")
//	public ResponseEntity<ResponseTransfer> updateBookById(@PathVariable(value = "id") Integer bookId,
//			@RequestBody BookDTO bookDTO) {
//		if (bookDTO == null) {
//			return ResponseEntity.ok(new ResponseTransfer("Book is null"));
//		}
//		Book book = modelMapper.map(BookDto, Book.class);
//		bookService.updateBook(book, bookId);
//		return ResponseEntity.ok(new ResponseTransfer("Book added"));
//
//	}

//	@DeleteMapping("book/{id}")
//	public ResponseEntity<ResponseTransfer> deleteBookById(@PathVariable(value = "id") Integer bookId) {
//		bookService.deleteBookById(bookId);
//		return ResponseEntity.ok(new ResponseTransfer("Deleted successfully"));
//	}

}
