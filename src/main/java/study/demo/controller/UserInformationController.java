package study.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.service.UserService;
import study.demo.service.dto.request.ChangeMailRequestDto;
import study.demo.service.dto.request.PasswordRequestDto;
import study.demo.service.dto.request.ResetPasswordDto;
import study.demo.service.dto.response.MessageResponseDto;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserInformationController {

    private final UserService userService;

    @GetMapping("/reset-password")
    public ResponseEntity<MessageResponseDto> resetUserPassword(HttpServletRequest httpRequest) {
        log.info("Request reset password is processing...");
        return ResponseEntity.ok(userService.resetPassword(httpRequest));
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDto> confirmResetUserPassword(@RequestBody @Valid ResetPasswordDto password,HttpServletRequest httpRequest) {
        log.info("Confirm reset password is processing...");
        System.out.println(httpRequest.getHeader("email"));
        System.out.println(httpRequest.getHeader("otp"));
        return ResponseEntity.ok(userService.confirmResetPassword(httpRequest,password));
    }
    
    @PostMapping("/password/change")
    public ResponseEntity<MessageResponseDto> changeUserPassword(@Valid @RequestBody PasswordRequestDto request,HttpServletRequest httpRequest) {
        log.info("Request change password is processing...");
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @PostMapping("/email/change")
    public ResponseEntity<MessageResponseDto> changeUserMail(@Valid @RequestBody ChangeMailRequestDto request) {
        log.info("Request change email is processing...");
        return ResponseEntity.ok(userService.changeMail(request));
    }

    @PostMapping("/email/change/confirm")
    public ResponseEntity<MessageResponseDto> confirmChangeMail(@Valid @RequestBody ChangeMailRequestDto request,
            @RequestParam("otp") String otpCode) {
        log.info("Confirm change email...");
        return ResponseEntity.ok(userService.confirmChangeMail(request, otpCode));
    }

}
