package study.demo.controller.admin;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.User;
import study.demo.repository.UserRepository;
import study.demo.service.BookService;
import study.demo.service.UserQueryService;
import study.demo.service.UserService;
import study.demo.service.criteria.UserCriteria;
import study.demo.service.dto.response.AdminUserDto;
import study.demo.service.dto.response.UserDto;

@Slf4j
@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    private final BookService bookService;

    private final UserRepository userRepository;

    private final UserQueryService userQueryService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findAllUsers(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Retrieving all users.......");
        return ResponseEntity.ok(userService.findAllUsers(pageable));
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<AdminUserDto>> finByCriteria(UserCriteria creCriteria,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Retrieving all users.......");
        return ResponseEntity.ok(userQueryService.findBycriteria(creCriteria, pageable));
    }

    @GetMapping("")
    public ResponseEntity<User> getBookDetail(@RequestParam("status") String status) {
        log.info("Request borrow book is processing");
        return ResponseEntity.ok(userRepository.findByUserStatus(status));
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Integer userId) {
        log.info("Delete user with id: {}",userId);
        userRepository.deleteById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
