package study.demo.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.demo.service.UserService;
import study.demo.service.dto.request.PasswordRequestDto;
import study.demo.service.dto.response.MessageResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/password")
public class UserPasswordController {
    
    private final UserService userService;
    
    @GetMapping("/reset")
    public ResponseEntity<MessageResponseDto> resetUserPassword(@RequestParam("userName") String userName){
        return ResponseEntity.ok(userService.resetPassword(userName));
        
    }
    
    @PostMapping("/change")
    public ResponseEntity<MessageResponseDto> changeUserPassword(@Valid @RequestBody PasswordRequestDto request,
            @RequestParam("userName")String userName){
        return ResponseEntity.ok(userService.changePassword(request,userName));
    }
}
