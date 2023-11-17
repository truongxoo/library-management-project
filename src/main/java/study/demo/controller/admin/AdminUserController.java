package study.demo.controller.admin;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

	private final UserQueryService userQueryService;

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> findAllUsers(@PageableDefault(size = 10)Pageable pageable) {
		log.info("Retrieving all users.......");
		return ResponseEntity.ok(userService.findAllUsers(pageable));
	}
	
	@GetMapping("/users/search")
    public ResponseEntity<List<AdminUserDto>> finByCriteria(UserCriteria creCriteria,@PageableDefault(size = 10)Pageable pageable) {
        log.info("Retrieving all users.......");
        return ResponseEntity.ok(userQueryService.findBycriteria(creCriteria,pageable));
    }

}
